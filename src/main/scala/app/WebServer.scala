package app

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import app.routes.{AuthorRoutes, BookRoutes}
import app.data.persistence.DBInitializer
import app.swagger.SwaggerRoutes
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

import scala.io.StdIn

/*
 * @created - 26/10/2020
 * @project - simple-crud
 * @author  - Michael Mustapha
 */

object WebServer extends App with AppConfig with ErrorHandler with DBInitializer {

  import ActorService._

  lazy val authorRepo: AuthorRepo = new AuthorRepo
  lazy val bookRepo: BookRepo = new BookRepo

  // declare routes
  // Route.seal() is used to enable the custom rejection and exceptions declared in the ErrorHandler mixin
  val routes: Route = cors() {
    Route.seal(
      new AuthorRoutes(authorRepo).routes ~
        new BookRoutes(bookRepo).routes ~
        SwaggerRoutes.routes)
  }

  // bind routes to server
  val bindingFuture = Http().newServerAt(interface, port).bind(routes)

  log.info(s"Server currently running on http://$interface:$port")
  //  StdIn.readLine() // let it run until user presses return
  //  bindingFuture
  //    .flatMap(_.unbind()) // trigger unbinding from the port
  //    .onComplete(_ => {
  //      log.info("Shutting down db..........")
  //      db.close() // close the db connection
  //      system.terminate() // and shutdown when done
  //    })
}
