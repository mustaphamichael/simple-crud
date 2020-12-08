package app.swagger

import akka.http.scaladsl.server.{Directives, Route}
import app.AppConfig
import app.routes.{AuthorRoutes, BookRoutes}
import com.github.swagger.akka.SwaggerHttpService
import com.github.swagger.akka.model.{Contact, Info}

/*
 * @created - 01/12/2020
 * @project - simple-crud
 * @author  - Michael Mustapha
 */
private object SwaggerDocService extends SwaggerHttpService with AppConfig {
  override val apiClasses: Set[Class[_]] = Set(classOf[BookRoutes], classOf[AuthorRoutes])
  override val host = s"$interface:$port"
  override val info: Info = Info(
    description = "A simple CRUD app using Akka-Http and PostgreSQL",
    version = "1.0",
    contact = Some(Contact(name = "Michael Mustapha", url = "https://github.com/mustaphamichael", email = "")))
  override val unwantedDefinitions = Seq("Function1", "Function1RequestContextFutureRouteResult")
}

object SwaggerRoutes extends Directives {
  val routes: Route = SwaggerDocService.routes ~ ui

  // The user interface can be rendered using either of the two routes
  // '/swagger' OR '/index.html'
  def ui: Route = path("swagger") {
    getFromResource("swagger/index.html")
  } ~
    getFromResourceDirectory("swagger")
}
