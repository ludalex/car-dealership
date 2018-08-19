package integration

import java.sql.Date

import models.FuelType.FuelType
import models.{CarAdvert, FuelType}
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._

import scala.concurrent.ExecutionContext.{global => globalExecutionContext}


@RunWith(classOf[JUnitRunner])
class CarAdvertsSpec extends Specification {

  val application = new GuiceApplicationBuilder().build()
  implicit val ec = globalExecutionContext

  val usedGasolineCarAdvert = CarAdvert(Some(1), "BMW M4", FuelType.Gasoline, 60000000, false, Some(1337), Some(Date.valueOf("2008-09-15")))
  val newDieselCarAdvert    = CarAdvert(Some(2), "BMW 440i", FuelType.Gasoline, 60000000, true, None, None)
  val badDieselCarAdvert    = CarAdvert(Some(3), "BMW 118d", FuelType.Diesel, 60000000, false, None, None)


  "Adverts should be created/persisted (POST) and retrieved (GET) with the same valid JSON" in new WithApplication {
    val Some(postResult) = route(application, FakeRequest(POST, "/adverts").withJsonBody(Json.toJson(usedGasolineCarAdvert)))
    status(postResult) mustEqual CREATED
    contentType(postResult) must beSome("application/json")

    val Some(getResult) = route(application, FakeRequest(GET, s"/adverts/${usedGasolineCarAdvert.id.head}"))
    status(getResult) mustEqual OK
    contentType(getResult) must beSome("application/json")

    val responseNode = Json.parse(contentAsString(getResult))

    (responseNode \ "title").as[String] mustEqual usedGasolineCarAdvert.title
    (responseNode \ "fuel").as[FuelType] mustEqual usedGasolineCarAdvert.fuel
    (responseNode \ "price").as[Int] mustEqual usedGasolineCarAdvert.price
    (responseNode \ "isNew").as[Boolean] mustEqual usedGasolineCarAdvert.isNew
    Some((responseNode \ "mileage").as[Int]) mustEqual usedGasolineCarAdvert.mileage
    Some((responseNode \ "firstRegistration").as[Date]) mustEqual usedGasolineCarAdvert.firstRegistration
  }

  "New Adverts (POST) for used cars (isNew=false) should contain mileage and firstRegistration attributes" in new WithApplication() {
    val Some(postResult) = route(application, FakeRequest(POST, "/adverts").withJsonBody(Json.toJson(badDieselCarAdvert)))
    status(postResult) mustEqual BAD_REQUEST
  }

  "Adverts should be updated successfully (PUT) with different data and be validated accordingly" in new WithApplication() {
    route(application, FakeRequest(POST, "/adverts").withJsonBody(Json.toJson(newDieselCarAdvert)))

    val Some(putResult) = route(application, FakeRequest(PUT, s"/adverts/${newDieselCarAdvert.id.head}").withJsonBody(Json.toJson(newDieselCarAdvert.copy(title = "BMW 430i"))))
    status(putResult) mustEqual OK
    contentType(putResult) must beSome("application/json")

    val responseNode = Json.parse(contentAsString(putResult))

    (responseNode \ "title").as[String] mustEqual "BMW 430i"
    (responseNode \ "fuel").as[FuelType] mustEqual newDieselCarAdvert.fuel
    (responseNode \ "price").as[Int] mustEqual newDieselCarAdvert.price
    (responseNode \ "isNew").as[Boolean] mustEqual newDieselCarAdvert.isNew
  }

}
