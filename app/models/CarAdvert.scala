package models

import models.FuelType.FuelType
import play.api.data.validation.ValidationError
import play.api.libs.json._

case class CarAdvert(id: Int, title: String, fuel: FuelType, price: Int, isNew: Boolean, mileage: Int, firstRegistration: String)

object FuelType extends Enumeration {

  type FuelType = Value
  val Gasoline, Diesel = Value

  implicit val enumReads: Reads[FuelType] = EnumerationHelpers.enumReads(FuelType)
  implicit val enumWrites: Writes[FuelType] = EnumerationHelpers.enumWrites
}

object CarAdvert {

  implicit val jsonWrites = Json.writes[CarAdvert]

  implicit val jsonValidatedReads = Reads[CarAdvert] {
    json =>
      for {
        id <- (json \ "price").validate[Int]

        title <- (json \ "title").validate[String]
          .filter(JsError(ValidationError("must be more than 2 characters")))(fname => fname.length > 2)

        fuel <- (json \ "fuel").validate[FuelType]

        price <- (json \ "price").validate[Int]

        isNew <- (json \ "isNew").validate[Boolean]

        mileage <- (json \ "mileage").validate[Int]

        firstRegistration <- (json \ "firstRegistration").validate[String]

      } yield CarAdvert(id, title, fuel, price, isNew, mileage, firstRegistration)
  }
}
