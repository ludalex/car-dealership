package dal

import javax.inject.{Inject, Singleton}
import models.FuelType.FuelType
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import models.{CarAdvert, DynamicSortBySupport, FuelType}
import slick.ast.{BaseTypedType, Ordering}
import slick.ast.Ordering.Direction

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

  private class CarAdvertTable(tag: Tag) extends Table[CarAdvert](tag, "car_adverts") with DynamicSortBySupport.ColumnSelector {

    def id = column[Option[Int]]("id", O.PrimaryKey, O.AutoInc)

    def title = column[String]("title")

    def fuel = column[FuelType]("fuel_type")

    def price = column[Int]("price")

    def isNew = column[Boolean]("is_new")

    def mileage = column[Option[Int]]("mileage")

    def firstRegistration = column[Option[String]]("first_registration")

    def * = (id, title, fuel, price, isNew, mileage, firstRegistration) <> ((CarAdvert.apply _).tupled, CarAdvert.unapply)

    val select = Map(
      "id" -> (this.id),
      "title" -> (this.title)
    )
  }


  private val carAdverts = TableQuery[CarAdvertTable]


  def insert(carAdvert: CarAdvert): Future[Unit] = db.run(carAdverts += carAdvert).map(_ => "Success")

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

  def update(id: Int, carAdvert: CarAdvert): Future[Boolean] = {
    val newCarAdvert: CarAdvert = carAdvert.copy(Some(id))
    db.run(carAdverts.filter(_.id === id).update(newCarAdvert)).map { affectedRows =>
      affectedRows > 0
    }
  }

  def delete(id: Int): Future[Boolean] = {
    db.run(carAdverts.filter(_.id === id).delete).map { affectedRows =>
      affectedRows > 0
    }
  }
}