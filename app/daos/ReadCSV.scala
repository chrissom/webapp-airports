package daos

import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

/**
  * Read CSV Data (read only).
  *
  * @param file csv file name
  * @tparam T type
  */
abstract class ReadCSV[T](file: String) {

  // load csv row
  def readRow(columns: Vector[String]): T

  // load csv data
  lazy val data: Future[Vector[T]] = {
    val is = this.getClass.getClassLoader.getResourceAsStream(file)
    Future {
      scala.io.Source.fromInputStream(is)
        .getLines
        .drop(1)                                                      // remove header
        .map(line => readRow(line.split(",").toVector))               // these lines take
        .foldLeft(Vector[T]()) { (acc: Vector[T], current: T) => {    // too much time :(
          acc :+ current                                              // there's better way to do that
        }                                                             //
      }
    }
  }

  // helpers to clean data
  protected def removeQuotes(s: String) = s.replaceAll("""^"(.*)"$""", "$1")
}
