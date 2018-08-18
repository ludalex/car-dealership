package controllers

import models._

import dal.CarAdvertRepository
import javax.inject._
import play.api.libs.json.{JsError, Json}
import play.api.mvc.{Action, Controller}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class CarAdvertController @Inject() (carAdverts: CarAdvertRepository) extends Controller {

  def index(sortBy: Option[String]) = Action.async { request =>
    carAdverts.findAll(sortBy).map { data =>
      Ok(Json.toJson(data)).as(JSON)
    }
  }

  def read(id: Int) = Action.async { request =>
    carAdverts.findById(id).map { data =>
      data match {
        case Some(carAdvert) => Ok(Json.toJson(carAdvert)).as(JSON)
        case None => UnprocessableEntity
      }
    }
  }

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

  def update(id: Int) = Action.async(parse.json) { request =>
    request.body.validate[CarAdvert].map { carAdvert =>
      carAdverts.update(id, carAdvert).map { result =>
        result match {
          case true => Ok(Json.obj("status" -> "success")).as(JSON)
          case false => UnprocessableEntity
        }
      }.recoverWith {
        case e => Future { InternalServerError("ERROR: " + e ) }
      }
    }.recoverTotal {
      e => Future { BadRequest( Json.obj("status" -> "fail", "data" -> JsError.toFlatJson(e)) ) }
    }
  }

  def delete(id: Int) = Action.async { request =>
    carAdverts.delete(id).map { result =>
      result match {
        case true => Ok(Json.obj("status" -> "success")).as(JSON)
        case false => UnprocessableEntity
      }
    }.recoverWith {
      case e => Future { InternalServerError("ERROR: " + e ) }
    }
  }
}