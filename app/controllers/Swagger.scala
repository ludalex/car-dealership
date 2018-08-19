package controllers

import io.swagger.models.properties.RefProperty
import play.api.mvc.Action

import scala.collection.JavaConversions._


/*
  We serve a slightly customized version of SwaggerBaseApiController to remove #/definitions/void refs in order to prevent a swagger-ui bug
 */
class Swagger extends SwaggerBaseApiController {

  def json = Action { implicit request =>
    val swagger = getResourceListing(request.host)
    fixSwagger(swagger)

    returnValue(request, toJsonString(swagger))
  }

  private[this] def fixSwagger(swagger: io.swagger.models.Swagger): Unit = {

    swagger.getPaths.values.foreach { value =>

      value.getOperations.foreach { oper =>

        oper.getResponses.values.foreach { resp =>
          resp.getSchema() match {
            case schema: RefProperty if schema.get$ref() == "#/definitions/void" => resp.setSchema(null)
            case _ => ()
          }
        }
      }
    }
  }
}