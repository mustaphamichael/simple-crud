package app.routes

import java.sql.Timestamp
import java.time.Instant

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import app.JsonSupport
import app.data.persistence.DBSchema
import app.data.{Author, Authors, ResponseBody}
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
@Path("/authors")
class AuthorRoutes(repo: DBSchema#AuthorRepo)(implicit ec: ExecutionContext) extends JsonSupport {

  case class AuthorResponse(message: String, data: Author)

  case class AuthorList(message: String, data: Seq[Author])

  val routes: Route =
  // routes with `/authors`
    path("authors") {
      create ~ getAll ~ edit
    } ~
      // routes with `authors/{id}`
      path("authors" / IntNumber) { id =>
        getOne(id) ~ deleteOne(id)
      }

  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Create an author", description = "Create an author", tags = Array("Author"),
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[Author])))),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "Author created successfully",
        content = Array(new Content(schema = new Schema(implementation = classOf[AuthorResponse])))))
  )
  def create: Route = post {
    entity(as[Author]) { author =>
      onSuccess(repo.insert(author.copy(dateCreated = Some(Timestamp.from(Instant.now()))))) { d =>
        complete(StatusCodes.Created, ResponseBody("Author created successfully", Some(d)))
      }
    }
  }


  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Get all authors", description = "Get all authors", tags = Array("Author"),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "Get authors successful",
        content = Array(new Content(schema = new Schema(implementation = classOf[AuthorList])))))
  )
  def getAll: Route = get {
    onSuccess(repo.all) { d =>
      complete(ResponseBody("Get authors successful", Some(Authors(d))))
    }
  }


  @PUT
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Edit an author", description = "Edit an author", tags = Array("Author"),
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[Author])))),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "A new author will be created if the id does not exist",
        content = Array(new Content(schema = new Schema(implementation = classOf[AuthorResponse])))))
  )
  def edit: Route = put {
    entity(as[Author]) { author =>
      onSuccess(repo.update(author.copy(dateCreated = Some(Timestamp.from(Instant.now()))))) {
        case data@Some(_) => complete(ResponseBody("A new author was created", data)) // Insert
        case None => complete(ResponseBody("Request to update author was successful")) // Update
      }
    }
  }


  @GET
  @Path("/{id}")
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Get a single author", description = "Get a single author", tags = Array("Author"),
    parameters = Array(new Parameter(name = "id", in = ParameterIn.PATH, required = true, description = "Book id")),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "Get author successful",
        content = Array(new Content(schema = new Schema(implementation = classOf[AuthorResponse])))),
      new ApiResponse(responseCode = "404", description = "Not Found",
        content = Array(new Content(schema = new Schema(implementation = classOf[ResponseBody])))))
  )
  def getOne(@Parameter(hidden = true) id: Int): Route = get {
    onSuccess(repo.findById(id)) {
      case data@Some(_) => complete(ResponseBody("Get author successful", data))
      case None => complete(StatusCodes.NotFound, ResponseBody("The author does not exist"))
    }
  }


  @DELETE
  @Path("/{id}")
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Delete a single author", description = "Delete a single author", tags = Array("Author"),
    parameters = Array(new Parameter(name = "id", in = ParameterIn.PATH, required = true, description = "Author id")),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "Author deleted",
        content = Array(new Content(schema = new Schema(implementation = classOf[ResponseBody])))),
      new ApiResponse(responseCode = "404", description = "Not Found"))
  )
  def deleteOne(@Parameter(hidden = true) id: Int): Route = delete {
    onSuccess(repo.delete(id)) { d =>
      if (d) complete(ResponseBody("Author deleted"))
      else complete(StatusCodes.NotFound, ResponseBody("The author does not exist"))
    }
  }
}
