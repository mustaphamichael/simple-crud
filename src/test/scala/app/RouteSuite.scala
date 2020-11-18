package app

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import app.data.ResponseBody
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec


/*
 * @created - 08/11/2020
 * @project - simple-crud
 * @author  - Michael Mustapha
 */
class RouteSuite
  extends AnyWordSpec
    with Matchers
    with ErrorHandler //important
    with ScalatestRouteTest {

  // Error Handling Test

  private val route = Route.seal(path("test") {
    get {
      complete("Test completed")
    }
  })

  "The api service" should {
    "return a 404 error when the route does not exist" in {
      Get() ~> route ~> check {
        status shouldEqual NotFound
        responseAs[ResponseBody] shouldEqual ResponseBody("The resource was not found")
      }
    }

    "return a 405 error for method not allowed" in {
      Patch("/test") ~> route ~> check {
        status shouldEqual MethodNotAllowed
        responseAs[ResponseBody].message shouldEqual "PATCH is not allowed. Supported methods: GET!"
      }
    }
  }
}
