package app.routes

import java.sql.Timestamp
import java.time.Instant

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import app.JsonSupport
import app.data.persistence.DBSchema
import app.data.{Book, Books, ResponseBody}
import io.swagger.v3.oas.annotations._
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.{Content, Schema}
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import javax.ws.rs.core.MediaType
import javax.ws.rs.{Consumes, DELETE, GET, POST, PUT, Path, Produces}

import scala.concurrent.ExecutionContext

/*
 * @created - 26/10/2020
 * @project - simple-crud
 * @author  - Michael Mustapha
 */

@Path("/books")
class BookRoutes(repo: DBSchema#BookRepo)(implicit ec: ExecutionContext) extends JsonSupport {

  // A polymorphic is not used because of the Swagger dependency
  case class BookResponse(message: String, data: Book)

  case class BookList(message: String, data: Seq[Book])

  val routes: Route =
  // routes with `/books`
    path("books") {
      create ~ getAll ~ edit
    } ~
      // routes with `books/{id}`
      path("books" / IntNumber) { id =>
        getOne(id) ~ deleteOne(id)
      }

  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Create a book", description = "Book created successfully", tags = Array("Book"),
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[Book])))),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "Book created successfully",
        content = Array(new Content(schema = new Schema(implementation = classOf[BookResponse])))))
  )
  def create: Route = post {
    entity(as[Book]) { book =>
      onSuccess(repo.insert(book.copy(dateCreated = Some(Timestamp.from(Instant.now()))))) { d =>
        complete(StatusCodes.Created, ResponseBody("Book created successfully", Some(d)))
      }
    }
  }


  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Get all books", description = "Get all books", tags = Array("Book"),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "Get books successful",
        content = Array(new Content(schema = new Schema(implementation = classOf[BookList])))))
  )
  def getAll: Route = get {
    onSuccess(repo.all) { d =>
      complete(ResponseBody("Get authors successful", Some(Books(d))))
    }
  }


  @PUT
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Edit a book", description = "Edit a book", tags = Array("Book"),
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[Book])))),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "A new book will be created if the id does not exist",
        content = Array(new Content(schema = new Schema(implementation = classOf[Book])),
          new Content(schema = new Schema(implementation = classOf[BookResponse])))))
  )
  def edit: Route = put {
    entity(as[Book]) { book =>
      onSuccess(repo.update(book.copy(dateCreated = Some(Timestamp.from(Instant.now()))))) {
        case data@Some(_) => complete(ResponseBody("A new book was created", data)) // Insert
        case None => complete(ResponseBody("Request to update book successful")) // Update
      }
    }
  }


  @GET
  @Path("/{id}")
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Get a single book", description = "Get a single book", tags = Array("Book"),
    parameters = Array(new Parameter(name = "id", in = ParameterIn.PATH, required = true, description = "Book id")),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "Get book successful",
        content = Array(new Content(schema = new Schema(implementation = classOf[BookResponse])))),
      new ApiResponse(responseCode = "404", description = "Not Found",
        content = Array(new Content(schema = new Schema(implementation = classOf[ResponseBody])))))
  )
  def getOne(@Parameter(hidden = true) id: Int): Route = get {
    onSuccess(repo.findById(id)) {
      case data@Some(_) => complete(ResponseBody("Get book successful", data))
      case None => complete(StatusCodes.NotFound, ResponseBody("The book does not exist"))
    }
  }


  @DELETE
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Path("/{id}")
  @Operation(summary = "Delete a single book", description = "Delete a single book", tags = Array("Book"),
    parameters = Array(new Parameter(name = "id", in = ParameterIn.PATH, required = true, description = "Book id")),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "Book deleted",
        content = Array(new Content(schema = new Schema(implementation = classOf[ResponseBody])))),
      new ApiResponse(responseCode = "404", description = "Not Found"))
  )
  def deleteOne(@Parameter(hidden = true) id: Int): Route = delete {
    onSuccess(repo.delete(id)) { d =>
      if (d) complete(ResponseBody("Book deleted"))
      else complete(StatusCodes.NotFound, ResponseBody("The book does not exist"))
    }
  }
}
