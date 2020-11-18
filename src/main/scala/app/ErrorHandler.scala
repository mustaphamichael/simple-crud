package app

import akka.http.scaladsl.server.ExceptionHandler
import akka.http.scaladsl.server.MethodRejection
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.RejectionHandler
import akka.http.scaladsl.server.Directives._
import app.data.ResponseBody
import org.postgresql.util.PSQLException

/*
 * @created - 06/11/2020
 * @project - simple-crud
 * @author  - Michael Mustapha
 */
trait ErrorHandler extends JsonSupport {
  implicit def rejectionHandler: RejectionHandler =
    RejectionHandler.newBuilder()
      .handleAll[MethodRejection] { rejections =>
        extractMethod { method =>
          val methods = rejections.map(_.supported.name)
          complete(MethodNotAllowed, ResponseBody(s"${method.name} is not allowed. Supported methods: ${methods.mkString(" or ")}!"))
        }
      }
      .handleNotFound {
        complete(NotFound, ResponseBody("The resource was not found"))
      }
      .result()

  implicit def exceptionHandler: ExceptionHandler =
    ExceptionHandler {
      case ex: PSQLException =>
        complete(InternalServerError, ResponseBody(ex.getServerErrorMessage.getDetail))
      case ex: Exception =>
        println(s"System Error not Handled ::: $ex")
        complete(InternalServerError, ResponseBody(ex.getMessage))
    }
}
