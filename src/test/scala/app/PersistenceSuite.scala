package app

import akka.http.scaladsl.testkit.ScalatestRouteTest
import app.data.{Author, Book}
import app.data.persistence.{H2, DBSchema}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{Seconds, Span}
import org.scalatest.wordspec.AnyWordSpec

/*
 * @created - 08/11/2020
 * @project - simple-crud
 * @author  - Michael Mustapha
 */
class PersistenceSuite
  extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with DBSchema
    with H2
    with ScalatestRouteTest {

  // For database test

  //  implicit val ec: ExecutionContext = system.dispatcher
  implicit val defaultPatience: PatienceConfig =
    PatienceConfig(timeout = Span(5, Seconds))

  val authorRepo = new AuthorRepo()
  val bookRepo = new BookRepo()

  "The generic persistence" should {
    "handle data retrieval" in {
      whenReady(authorRepo.all)(_.size shouldEqual 2)
      whenReady(bookRepo.all)(_.nonEmpty)
    }

    "get a single data by id" in {
      whenReady(authorRepo.findById(1))(_.get == Author(Some(1), "Martin Ordesky"))
      whenReady(bookRepo.findById(1))(_.get.authorId == 1)
    }

    // Note the returned value for an `Upsert` operation
    // None - [Updated];  Some - [Inserted]
    "update a single data" in {
      val newName = "Martin O."
      val author = whenReady(authorRepo.findById(1))(a => a).get.copy(name = newName)
      whenReady(authorRepo.update(author))(_ shouldBe None)
    }

    "delete a single data by id" in {
      whenReady(bookRepo.delete(1)) { result =>
        result shouldBe true
      }
      whenReady(bookRepo.findById(1)) { user =>
        user shouldBe None
      }
    }
  }
}
