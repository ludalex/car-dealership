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

  @ApiOperation(value = "Retrieve all Car Adverts", response = classOf[models.CarAdvert], responseContainer = "List")
  def index(@ApiParam(value = "String used for sorting (fieldName:direction)", required = true,
    defaultValue = "id:asc") sortBy: Option[String]) = Action.async {
    carAdverts.findAll(sortBy).map { data =>
      Ok(Json.toJson(data)).as(JSON)
    }
  }

  @ApiOperation(value = "Retrieve a single Car Adverts by ID", response = classOf[models.CarAdvert])
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "Car Advert not found.")))
  def read(@ApiParam(value = "ID of the Car Advert to retrieve") id: Int) = Action.async {
    carAdverts.findById(id).map {
      case Some(carAdvert) => Ok(Json.toJson(carAdvert))
      case None => UnprocessableEntity("Car Advert not found.")
    }
  }

  @ApiOperation(value = "Create new Car Advert", response = classOf[models.CarAdvert], responseReference = "void")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "CarAdvert object that needs to be added to the store", required = true, dataType = "models.CarAdvert", paramType = "body")))
  def create = Action.async(parse.json) { request =>
    request.body.validate[CarAdvert].map { carAdvert =>
      carAdverts.insert(carAdvert).map {
        result => Created(Json.toJson(result))
      }.recoverWith {
        case e => Future { InternalServerError("Error: " + e )}
      }
    }.recoverTotal {
      e => Future { BadRequest( Json.obj("status" -> "error", "data" -> JsError.toJson(e)) ) }
    }
  }

  @ApiOperation(value = "Update existing Car Advert", response = classOf[Void], responseReference = "void")
  def update(@ApiParam(value = "ID of the Car Advert to update") id: Int) = Action.async(parse.json) { request =>
    request.body.validate[CarAdvert].map { carAdvert =>
      carAdverts.update(id, carAdvert).map {
        result => Ok(Json.toJson(result))
      }.recoverWith {
        case e => Future { InternalServerError("Error: " + e ) }
      }
    }.recoverTotal {
      e => Future { BadRequest( Json.obj("status" -> "error", "data" -> JsError.toJson(e)) ) }
    }
  }

  @ApiOperation(value = "Delete a Car Advert", response = classOf[Void], responseReference = "void")
  def delete(@ApiParam(value = "ID of the Car Advert to delete") id: Int) = Action.async { request =>
    carAdverts.delete(id).map {
      case true => Ok(Json.obj("status" -> "success")).as(JSON)
      case false => UnprocessableEntity
    }.recoverWith {
      case e => Future { InternalServerError("Error: " + e ) }
    }
  }
}