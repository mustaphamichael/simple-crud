package app.data.persistence

import java.sql.Timestamp

import app.data.Model
import slick.jdbc.JdbcProfile
import slick.lifted

import scala.concurrent.{ExecutionContext, Future}

/*
 * @created - 04/11/2020
 * @project - simple-crud
 * @author  - Michael Mustapha
 */
trait DB {
  val driver: JdbcProfile

  import driver.api._

  lazy val db: Database = Database.forConfig("database")
}

// PostgreSQL db - main app db
trait PG extends DB {
  override val driver: JdbcProfile = slick.jdbc.PostgresProfile
}

// In-memory db for test purposes only
trait H2 extends DB {
  override val driver: JdbcProfile = slick.jdbc.H2Profile
}

// To provide a single source for table operations
trait TableDefinition extends DB {

  import driver.api._

  abstract class BaseTable[M <: Model](tag: Tag, _tableName: String)
    extends Table[M](tag, _tableName) {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def dateCreated = column[Timestamp]("date_created", O.SqlType("TIMESTAMP DEFAULT NOW()"))
  }

}

// Database operations
trait DBOperationDef extends TableDefinition {

  import driver.api._

  abstract class BaseOperation[M <: Model, T <: BaseTable[M]](implicit ec: ExecutionContext) {

    val table: lifted.TableQuery[T]

    def insert(model: M): Future[M] = db.run {
      table returning table += model
    }

    def all: Future[Seq[M]] = db.run {
      table.result
    }

    def findById(id: Int): Future[Option[M]] = db.run {
      table.filter(_.id === id).result.headOption
    }

    def update(model: M): Future[Option[M]] = db.run {
      (table returning table).insertOrUpdate(model)
    }

    def delete(id: Int): Future[Boolean] = db.run {
      table.filter(_.id === id).delete.map(_ > 0)
    }
  }

}
