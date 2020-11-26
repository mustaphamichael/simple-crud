package app.data

/*
 * @created - 04/11/2020
 * @project - simple-crud
 * @author  - Michael Mustapha
 */
sealed trait Model

case class Author(id: Option[Int], name: String) extends Model

case class Authors(authors: Seq[Author]) extends Model

case class Book(id: Option[Int],
                title: String,
                authorId: Int,
                dateCreated: Option[Long]
               ) extends Model

case class Books(books: Seq[Book]) extends Model

case class ResponseBody(message: String, data: Option[Model] = None)