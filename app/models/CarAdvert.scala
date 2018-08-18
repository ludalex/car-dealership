package models

import models.FuelType.FuelType
import play.api.data.validation.ValidationError
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._


case class CarAdvert(id: Option[Int], title: String, fuel: FuelType, price: Int, isNew: Boolean, mileage: Option[Int], firstRegistration: Option[String])

object FuelType extends Enumeration {

  type FuelType = Value
  val Gasoline, Diesel = Value

  implicit val enumReads: Reads[FuelType] = EnumerationHelpers.enumReads(FuelType)
  implicit val enumWrites: Writes[FuelType] = EnumerationHelpers.enumWrites
}

object CarAdvert {

  implicit val carAdvertRead: Reads[CarAdvert] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "title").read[String](minLength[String](3)) and
      (JsPath \ "fuel").read[FuelType] and
      (JsPath \ "price").read[Int] and
      (JsPath \ "isNew").read[Boolean] and
      (JsPath \ "mileage").readNullable[Int] and
      (JsPath \ "firstRegistration").readNullable[String]
    )(CarAdvert.apply _)

  implicit val carAdvertWrite = Json.writes[CarAdvert]

//  implicit val jsonValidatedReads = Reads[CarAdvert] {
//    json =>
//      for {
//        id <- (json \ "price").validate[Int]
//
//        title <- (json \ "title").validate[String]
//          .filter(JsError(ValidationError("must be more than 2 characters")))(fname => fname.length > 2)
//
//        fuel <- (json \ "fuel").validate[FuelType]
//
//        price <- (json \ "price").validate[Int]
//
//        isNew <- (json \ "isNew").validate[Boolean]
//
//        mileage <- (json \ "mileage").get.as[Option[Int]]
//
//        firstRegistration <- (json \ "firstRegistration").get.as[Option[String]]
//
//      } yield CarAdvert(id, title, fuel, price, isNew, mileage, firstRegistration)
//  }



}
