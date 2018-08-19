package models

import java.sql.Date

import io.swagger.annotations.{ApiModel, ApiModelProperty}
import models.FuelType.FuelType
import play.api.data.validation.ValidationError
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import utils.EnumerationHelpers

import scala.annotation.meta.field

@ApiModel("CarAdvert")
case class CarAdvert(
                      @(ApiModelProperty @field)(value = "id", dataType = "int") id: Option[Int],
                      @(ApiModelProperty @field) title: String,
                      @(ApiModelProperty @field)(dataType = "string", allowableValues = "Diesel, Gasoline") fuel: FuelType,
                      @(ApiModelProperty @field) price: Int,
                      @(ApiModelProperty @field) isNew: Boolean,
                      @(ApiModelProperty @field)(dataType = "int") mileage: Option[Int],
                      @(ApiModelProperty @field) firstRegistration: Option[Date]
                    )

object FuelType extends Enumeration {

  type FuelType = Value
  val Gasoline, Diesel = Value

  implicit val enumReads: Reads[FuelType] = EnumerationHelpers.enumReads(FuelType)
  implicit val enumWrites: Writes[FuelType] = EnumerationHelpers.enumWrites
}

object CarAdvert {

  implicit val carAdvertRead: Reads[CarAdvert] =  (
    (__ \ "id").readNullable[Int] and
      (__ \ "title").read[String](minLength[String](3)) and
      (__ \ "fuel").read[FuelType] and
      (__ \ "price").read[Int] and
      (__ \ "isNew").read[Boolean] and
      (__ \ "isNew").read[Boolean].flatMap {
        case false => {
          (__ \ "mileage").readNullable[Int].filter(ValidationError(s"'mileage' property is required when 'isNew' is set to false."))(_.isDefined)
        }
        case _ => {
          (__ \ "mileage").readNullable[Int]
        }
      } and
      (__ \ "isNew").read[Boolean].flatMap {
        case false => {
          (__ \ "firstRegistration").readNullable[Date].filter(ValidationError(s"'firstRegistration' property is required when 'isNew' is set to false."))(_.isDefined)
        }
        case _ => {
          (__ \ "firstRegistration").readNullable[Date]

        }
      }
    )(CarAdvert.apply _)

  implicit val carAdvertWrite = Json.writes[CarAdvert]
}
