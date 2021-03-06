package dal

import java.sql.Date

import javax.inject.{Inject, Singleton}
import models.FuelType.FuelType
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import models.{CarAdvert, FuelType}
import slick.ast.{BaseTypedType, Ordering}
import slick.ast.Ordering.Direction
import utils.DynamicSortBySupport

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CarAdvertRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._

  implicit def fuelTypeCT: BaseTypedType[FuelType.Value] =
    MappedColumnType.base[FuelType.Value, String](
      enum => enum.toString, str => FuelType.withName(str)
    )

  /*
     CarAdvert table definition
  */
  private class CarAdvertTable(tag: Tag) extends Table[CarAdvert](tag, "car_adverts") with DynamicSortBySupport.ColumnSelector {

    def id = column[Option[Int]]("id", O.PrimaryKey, O.AutoInc)

    def title = column[String]("title")

    def fuel = column[FuelType]("fuel_type")

    def price = column[Int]("price")

    def isNew = column[Boolean]("is_new")

    def mileage = column[Option[Int]]("mileage")

    def firstRegistration = column[Option[Date]]("first_registration")

    def * = (id, title, fuel, price, isNew, mileage, firstRegistration) <> ((CarAdvert.apply _).tupled, CarAdvert.unapply)

    // Mapping used by the dynamic "sort by field" logic
    val select = Map(
      "id" -> (this.id),
      "title" -> (this.title),
      "fuel" -> (this.fuel),
      "price" -> (this.price),
      "mileage" -> (this.mileage),
      "firstRegistration" -> (this.firstRegistration)
    )
  }


  private val carAdverts = TableQuery[CarAdvertTable]

  /*
     Operations
  */
  def insert(carAdvert: CarAdvert): Future[CarAdvert] = db.run(carAdverts += carAdvert).map(_ => carAdvert)

  def findAll(sortBy: Option[String]): Future[Seq[CarAdvert]] = {
    if(sortBy.nonEmpty) {
      val keyAndDir = sortBy.map(_.split(':')).toList
      val dir = if (keyAndDir(0)(1) == "asc") Ordering.Asc else Ordering.Desc
      val sortsBy = Seq[(String, Direction)]((keyAndDir(0)(0), dir) , ("id", Ordering.Asc))
      val query = carAdverts.dynamicSortBy(sortsBy).result
      db.run(query)
    } else {
      val query = carAdverts.result
      db.run(query)
    }
  }

  def findById(id: Int): Future[Option[CarAdvert]] = {
    db.run(carAdverts.filter(_.id === id).result.headOption)
  }

  def update(id: Int, carAdvert: CarAdvert): Future[CarAdvert] = {
    val newCarAdvert: CarAdvert = carAdvert.copy(Some(id))
    db.run(carAdverts.filter(_.id === id).update(newCarAdvert)).map { affectedRows =>
      affectedRows > 0
    }.map(_ => carAdvert)
  }

  def delete(id: Int): Future[Boolean] = {
    db.run(carAdverts.filter(_.id === id).delete).map { affectedRows =>
      affectedRows > 0
    }
  }
}