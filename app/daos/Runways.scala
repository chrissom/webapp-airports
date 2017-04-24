package daos

import javax.inject.Singleton

import models.Runway

import scala.util.Try

/**
  * Runway DAO.
  *
  * @author christophe
  */
@Singleton
class Runways extends ReadCSV[Runway]("data/runways.csv") {

  override def readRow(columns: Vector[String]) = {
    Runway(
      id = columns(0).toInt,
      airportRef = columns(1).toInt,
      airportIdent = removeQuotes(columns(2)),
      leIdent = Try(removeQuotes(columns(8))).getOrElse("")
    )
  }
}
