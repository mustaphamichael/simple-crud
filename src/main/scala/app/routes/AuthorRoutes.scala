package app.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import app.JsonSupport
import app.data.persistence.Schema
import app.data.{Author, Authors, ResponseBody}

import scala.concurrent.ExecutionContext

/*
 * @created - 26/10/2020
 * @project - simple-crud
 * @author  - Michael Mustapha
 */
class AuthorRoutes(repo: Schema#AuthorRepo)(implicit ec: ExecutionContext) extends JsonSupport {

  val routes: Route = authors ~ author

  // routes with `/authors`
  def authors: Route = path("authors") {
    post {
      entity(as[Author]) { author =>
        onSuccess(repo.insert(author)) { d =>
          complete(StatusCodes.Created, ResponseBody("Author created successfully", Some(d)))
        }
      }
    } ~ get {
      onSuccess(repo.all) { d =>
        complete(ResponseBody("Get authors successful", Some(Authors(d))))
      }
    } ~ put {
      entity(as[Author]) { author =>
        onSuccess(repo.update(author)) {
          case data@Some(_) => complete(ResponseBody("A new author was created", data)) // Insert
          case None => complete(ResponseBody("Request to update author was successful")) // Update
        }
      }
    }
  }

  // routes with `authors/{id}`
  def author: Route = path("authors" / IntNumber) { id =>
    get {
      onSuccess(repo.findById(id)) {
        case data@Some(_) => complete(ResponseBody("Get author successful", data))
        case None => complete(StatusCodes.NotFound, ResponseBody("The author does not exist"))
      }
    } ~ delete {
      onSuccess(repo.delete(id)) { d =>
        if (d) complete(ResponseBody("Delete author successful"))
        else complete(StatusCodes.NotFound, ResponseBody("The author does not exist"))
      }
    }
  }
}
