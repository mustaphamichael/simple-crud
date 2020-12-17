package app.data

import io.swagger.v3.oas.annotations.media.Schema

/*
 * @created - 04/11/2020
 * @project - simple-crud
 * @author  - Michael Mustapha
 */
sealed trait Model

case class Author(@Schema(required = false, implementation = classOf[Int]) id: Option[Int],
                  name: String)
  extends Model

case class Authors(authors: Seq[Author]) extends Model

case class Book(@Schema(required = false, implementation = classOf[Int]) id: Option[Int],
                title: String,
                authorId: Int,
                @Schema(required = false, implementation = classOf[Long]) dateCreated: Option[Long])
  extends Model

case class Books(books: Seq[Book]) extends Model

case class ResponseBody(message: String, data: Option[Model] = None)