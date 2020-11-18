package app

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import app.data._
import spray.json._

/*
 * @created - 26/10/2020
 * @project - simple-crud
 * @author  - Michael Mustapha
 */
trait JsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val authorFormat = jsonFormat2(Author)
  implicit val authorsFormat = jsonFormat1(Authors)
  implicit val bookFormat = jsonFormat4(Book)
  implicit val booksFormat = jsonFormat1(Books)

  implicit object baseModelFormat extends RootJsonFormat[Model] {
    override def write(obj: Model): JsValue = obj match {
      case author: Author => author.toJson
      case authors: Authors => authors.toJson
      case book: Book => book.toJson
      case books: Books => books.toJson
      case unknown@_ => serializationError(s"Unable to serialize $unknown")
    }

    // Not required at the moment
    override def read(json: JsValue): Model = ???
  }

  implicit val responseFormat = jsonFormat2(ResponseBody)

}
