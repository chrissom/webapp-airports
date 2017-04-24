package controllers

import javax.inject._

import daos.{Airports, Countries}
import models._
import play.api._
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services._

import scala.concurrent.Future

/**
 * App controller.
 */
@Singleton
class AppController @Inject()(countries: Countries, airports: Airports) extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def query(query: Option[String] = None) = Action.async {
    val dataEmpty = Vector.empty[(Airport, Vector[Runway])]

    val data = query match {
      case None => Future.successful(dataEmpty)
      case Some(q) =>
        countries.find(q).flatMap {
          case None => Future.successful(dataEmpty)
          case Some(country) => airports.byCountry(country.code)
        }
    }

    data.map(d => Ok(views.html.query(query, d)))
  }

  def report = Action {
    Ok(views.html.report())
  }

}
