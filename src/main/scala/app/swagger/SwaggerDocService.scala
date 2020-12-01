package app.swagger

import akka.http.scaladsl.server.{Directives, Route}
import app.AppConfig
import app.routes.{AuthorRoutes, BookRoutes}
import com.github.swagger.akka.SwaggerHttpService
import com.github.swagger.akka.model.Info

/*
 * @created - 01/12/2020
 * @project - simple-crud
 * @author  - Michael Mustapha
 */
private object SwaggerDocService extends SwaggerHttpService with AppConfig {
  override val apiClasses: Set[Class[_]] = Set(classOf[BookRoutes])
  override val host = s"$interface:$port"
  override val info: Info = Info(version = "1.0")
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
