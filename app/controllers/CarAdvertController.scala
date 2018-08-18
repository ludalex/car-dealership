package controllers

import models._
import dal.CarAdvertRepository
import io.swagger.annotations._
import javax.inject._
import play.api.libs.json.{JsError, Json}
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Api(value = "adverts")
class CarAdvertController @Inject() (carAdverts: CarAdvertRepository) extends Controller {

  def index(sortBy: Option[String]) = Action.async {
    carAdverts.findAll(sortBy).map { data =>
      Ok(Json.toJson(data)).as(JSON)
    }
  }

  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "Car Advert not found.")))
  def read(@ApiParam(value = "ID of the Car Advert to retrieve") id: Int) = Action.async {
    carAdverts.findById(id).map {
      case Some(carAdvert) => Ok(Json.toJson(carAdvert))
      case None => UnprocessableEntity("Car Advert not found.")
    }
  }

  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "CarAdvert object that needs to be added to the store", required = true, dataType = "models.CarAdvert", paramType = "body")))
  def create = Action.async(parse.json) { request =>
    request.body.validate[CarAdvert].map { carAdvert =>
      carAdverts.insert(carAdvert).map {
        result => Created(Json.obj("status" -> "success"))
      }.recoverWith {
        case e => Future { InternalServerError("ERROR: " + e )}
      }
    }.recoverTotal {
      e => Future { BadRequest( Json.obj("status" -> "error", "data" -> JsError.toJson(e)) ) }
    }
  }

  def update(@ApiParam(value = "ID of the Car Advert to update") id: Int) = Action.async(parse.json) { request =>
    request.body.validate[CarAdvert].map { carAdvert =>
      carAdverts.update(id, carAdvert).map {
        case true => Ok(Json.obj("status" -> "success")).as(JSON)
        case false => UnprocessableEntity
      }.recoverWith {
        case e => Future { InternalServerError("ERROR: " + e ) }
      }
    }.recoverTotal {
      e => Future { BadRequest( Json.obj("status" -> "error", "data" -> JsError.toJson(e)) ) }
    }
  }

  def delete(@ApiParam(value = "ID of the Car Advert to delete") id: Int) = Action.async { request =>
    carAdverts.delete(id).map {
      case true => Ok(Json.obj("status" -> "success")).as(JSON)
      case false => UnprocessableEntity
    }.recoverWith {
      case e => Future { InternalServerError("ERROR: " + e ) }
    }
  }
}