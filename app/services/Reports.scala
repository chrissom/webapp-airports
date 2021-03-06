package services

import javax.inject.{Inject, Singleton}

import daos._
import models._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

/**
  * Reports: airports stats.
  *
  * @author christophe
  */
@Singleton
class Reports @Inject()(airports: Airports, countries: Countries, runways: Runways) {

  type AirportCode = String
  type AirportList = Vector[Airport]

  type TopCountry = (AirportCode, AirportList)
  type TopCountries = Vector[TopCountry]

  private val sortedAirportsData: Future[TopCountries] = {
    airports.data.map(d =>
      d.groupBy(_.countryCode).toVector.sortBy(_._2.size)
    )
  }

  // 10 countries with highest number of airports (with count)
  val top10countries: Future[TopCountries] = sortedAirportsData.map(airports => airports.reverse.take(10))

  // 10 countries with lowest number of airports (with count)
  val bottom10countries: Future[TopCountries] = sortedAirportsData.map(airports => airports.take(10))

}
