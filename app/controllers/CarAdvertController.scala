package controllers

import models._

import dal.CarAdvertRepository
import javax.inject._
import play.api.libs.json.{JsError, Json}
import play.api.mvc.{Action, Controller}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class CarAdvertController @Inject() (carAdverts: CarAdvertRepository) extends Controller {

  def create = Action.async(parse.json) { request =>
    request.body.validate[CarAdvert].map { carAdvert =>
      carAdverts.insert(carAdvert).map {
        result => Created(Json.obj("status" -> "success")).as(JSON)
      }.recoverWith {
        case e => Future { InternalServerError("ERROR: " + e )}
      }
    }.recoverTotal {
      e => Future { BadRequest( Json.obj("status" -> "fail", "data" -> JsError.toFlatJson(e)) ) }
    }
  }

  def index = Action.async { request =>
    carAdverts.list().map { data =>
      Ok(Json.toJson(data)).as(JSON)
    }
  }
}