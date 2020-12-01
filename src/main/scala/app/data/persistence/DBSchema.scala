package app.data.persistence

import app.data.{Author, Book}

import scala.concurrent.ExecutionContext

/*
 * @created - 04/11/2020
 * @project - simple-crud
 * @author  - Michael Mustapha
 */
trait DBSchema extends DBOperationDef {

  import driver.api._

  /**
   * Author Table Mapping
   */
  class Authors(tag: Tag) extends BaseTable[Author](tag, "AUTHORS") {
    def name = column[String]("name")

    def * = (id.?, name) <> (Author.tupled, Author.unapply)
  }

  lazy val authors = TableQuery[Authors]

  class AuthorRepo(implicit ec: ExecutionContext) extends BaseOperation[Author, Authors] {
    override val table = TableQuery[Authors]
  }

  /**
   * Book Table Mapping
   */
  class Books(tag: Tag) extends BaseTable[Book](tag, "BOOKS") {
    def title = column[String]("title")

    def authorId = column[Int]("author_id")

    def dateCreated = column[Option[Long]]("date_created", O.Default(Some(System.currentTimeMillis())))

    def * = (id.?, title, authorId, dateCreated) <> (Book.tupled, Book.unapply)

    def authorFk =
      foreignKey("author_fk", authorId, authors)(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)
  }

  lazy val books = TableQuery[Books]

  class BookRepo(implicit ec: ExecutionContext) extends BaseOperation[Book, Books] {
    override val table = TableQuery[Books]
  }

}
