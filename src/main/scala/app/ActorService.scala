package app

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import org.slf4j.Logger

import scala.concurrent.ExecutionContext

/*
 * @created - 23/11/2020
 * @project - simple-crud
 * @author  - Michael Mustapha
 */

/**
 * Actor System Singleton
 */
object ActorService {
  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "simple-crud-app")
  implicit val ec: ExecutionContext = system.executionContext

  lazy val log: Logger = system.log
}