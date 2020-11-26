package app.data.persistence

/*
 * @created - 12/11/2020
 * @project - simple-crud
 * @author  - Michael Mustapha
 */
trait DBInitializer extends Schema with PG {

  import app.ActorService._
  import driver.api._

  // Start DB
  log.info("Starting db..........")
  private val createDb = (authors.schema ++ books.schema).create
  //  createDb.statements.foreach(println) // for logging the SQL statement
  db.run(createDb)
}
