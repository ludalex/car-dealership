package dal

import javax.inject.{Inject, Singleton}
import models.FuelType.FuelType
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import models.{CarAdvert, FuelType}
import slick.ast.BaseTypedType

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

  private class CarAdvertTable(tag: Tag) extends Table[CarAdvert](tag, "car_adverts") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def title = column[String]("title")

    def fuel = column[FuelType]("fuel_type")

    def price = column[Int]("price")

    def isNew = column[Boolean]("is_new")

    def mileage = column[Int]("mileage")

    def firstRegistration = column[String]("first_registration")

    def * = (id, title, fuel, price, isNew, mileage, firstRegistration) <> ((CarAdvert.apply _).tupled, CarAdvert.unapply)
  }


  private val carAdverts = TableQuery[CarAdvertTable]


  def insert(carAdvert: CarAdvert): Future[Unit] = db.run(carAdverts += carAdvert).map(_ => "Success")

  def list(): Future[Seq[CarAdvert]] = db.run {
    carAdverts.result
  }
}