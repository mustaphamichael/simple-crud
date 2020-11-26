package app.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import app.JsonSupport
import app.data.persistence.Schema
import app.data.{Book, Books, ResponseBody}

import scala.concurrent.ExecutionContext

/*
 * @created - 26/10/2020
 * @project - simple-crud
 * @author  - Michael Mustapha
 */
class BookRoutes(repo: Schema#BookRepo)(implicit ec: ExecutionContext) extends JsonSupport {

  val routes: Route = books ~ book

  // routes with `/books`
  def books: Route = path("books") {
    post {
      entity(as[Book]) { book =>
        onSuccess(repo.insert(book)) { d =>
          complete(StatusCodes.Created, ResponseBody("Book created successfully", Some(d)))
        }
      }
    } ~ get {
      onSuccess(repo.all) { d =>
        complete(ResponseBody("Get books successful", Some(Books(d))))
      }
    } ~ put {
      entity(as[Book]) { book =>
        onSuccess(repo.update(book)) {
          case data@Some(_) => complete(ResponseBody("A new book was created", data)) // Insert
          case None => complete(ResponseBody("Request to update book successful")) // Update
        }
      }
    }
  }

  // routes with `books/{id}`
  def book: Route = path("books" / IntNumber) { id =>
    get {
      onSuccess(repo.findById(id)) {
        case data@Some(_) => complete(ResponseBody("Get book successful", data))
        case None => complete(StatusCodes.NotFound, ResponseBody("The book does not exist"))
      }
    } ~ delete {
      onSuccess(repo.delete(id)) { d =>
        if (d) complete(ResponseBody("Book deleted"))
        else complete(StatusCodes.NotFound, ResponseBody("The book does not exist"))
      }
    }
  }
}
