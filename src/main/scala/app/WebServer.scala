package app

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import app.routes.{AuthorRoutes, BookRoutes}
import app.data.persistence.DBInitializer

import scala.concurrent.ExecutionContext
import scala.io.StdIn

/*
 * @created - 26/10/2020
 * @project - simple-crud
 * @author  - Michael Mustapha
 */

object WebServer extends App with ErrorHandler with DBInitializer {
  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "simple-crud-app")
  implicit val ec: ExecutionContext = system.executionContext

  lazy val authorRepo: AuthorRepo = new AuthorRepo
  lazy val bookRepo: BookRepo = new BookRepo

  // declare routes
  // Route.seal() is used to enable the custom rejection and exceptions declared in the ErrorHandler mixin
  val routes: Route = Route.seal(
    new AuthorRoutes(authorRepo).routes ~
      new BookRoutes(bookRepo).routes)

  // bind routes to server
  val bindingFuture = Http().newServerAt("localhost", 3000).bind(routes)

  println("Server currently running on http://localhost:3000")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => {
      println("Shutting down db..........")
      db.close() // close the db connection
      system.terminate() // and shutdown when done
    })
}
