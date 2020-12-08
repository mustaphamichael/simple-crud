package app

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.ExceptionHandler
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

  import ActorService._

  implicit def rejectionHandler: RejectionHandler =
    RejectionHandler.default
      .mapRejectionResponse {
        case res@HttpResponse(_, _, ent: HttpEntity.Strict, _) =>
          // since all Akka default rejection responses are Strict this will handle all rejections
          val message = ent.data.utf8String.replaceAll("\"", """\"""")

          // TODO: Use json marshalling library here
          res.withEntity(HttpEntity(ContentTypes.`application/json`, s"""{"message": "$message"}"""))

        case x => x // pass through all other types of responses
      }

  implicit def exceptionHandler: ExceptionHandler =
    ExceptionHandler {
      case ex: PSQLException =>
        complete(InternalServerError, ResponseBody(ex.getServerErrorMessage.getDetail))
      case ex: Exception =>
        log.error(s"System Error not Handled ::: {}", ex)
        complete(InternalServerError, ResponseBody(ex.getMessage))
    }
}
