package app.data

import java.sql.Timestamp
import java.time.Instant

import io.swagger.v3.oas.annotations.media.Schema

/*
 * @created - 04/11/2020
 * @project - simple-crud
 * @author  - Michael Mustapha
 */
sealed trait Model {
  @Schema(required = false, implementation = classOf[Int])
  def id: Option[Int] = Some(0)

  @Schema(required = false, implementation = classOf[Timestamp])
  def dateCreated: Option[Timestamp] = Some(Timestamp.from(Instant.now()))
}

case class Author(override val id: Option[Int],
                  name: String,
                  override val dateCreated: Option[Timestamp]
                 ) extends Model

case class Authors(authors: Seq[Author]) extends Model

case class Book(override val id: Option[Int],
                title: String,
                authorId: Int,
                override val dateCreated: Option[Timestamp]
               ) extends Model

case class Books(books: Seq[Book]) extends Model

case class ResponseBody(message: String, data: Option[Model] = None)