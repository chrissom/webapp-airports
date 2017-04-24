package daos

import javax.inject.{Inject, Singleton}

import models.{Airport, Runway}
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

/**
  * Airport DAO.
  *
  * @author christophe
  */
@Singleton
class Airports @Inject()(runways: Runways) extends ReadCSV[Airport]("data/airports.csv") {

  override def readRow(columns: Vector[String]): Airport = {
    Airport(
      id = columns(0).toInt,
      ident = removeQuotes(columns(1)),
      name = removeQuotes(columns(3)),
      countryCode = removeQuotes(columns(8))
    )
  }

  def byCountry(countryCode: String): Future[Vector[(Airport, Vector[Runway])]] = {
    for {
      airports <- data.map(d =>
        d.filterByCountry(countryCode)
      )
      runways <- runways.data.map(d =>
        d.filter(r => airports.containsIdent(r.airportIdent))
          .groupBy(_.airportIdent)
      )
    } yield {
      airports.map { airport =>
        (airport, runways.getOrElse(airport.ident, Vector()))
      }
    }
  }

  /** Extend data (Vector[Airport]) */
  implicit class AirportsExtension(airports: Vector[Airport]) {

    def filterByCountry(code: String) = airports.filter(_.countryCode == code)

    def containsIdent(ident: String) = airports.map(_.ident).contains(ident)
  }
}




