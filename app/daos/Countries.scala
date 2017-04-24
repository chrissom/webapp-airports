package daos

import javax.inject.Singleton

import models.Country
import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
  * Country DAO.
  *
  * @author christophe
  */
@Singleton
class Countries extends ReadCSV[Country]("data/countries.csv") {

  override def readRow(columns: Vector[String]) = {
    Country(
      id = columns(0).toInt,
      code = removeQuotes(columns(1)),
      name = removeQuotes(columns(2))
    )
  }

  def find(q: String) = data.map { d =>
    d.find(bySameCode(q))
      .orElse(d.find(bySameName(q)))
  }

  def findByCode(code: String) = data.map { d =>
    d.find(bySameCode(code))
  }

  def findByName(name: String) = data.map { d =>
    d.find(bySameName(name))
  }

  private def bySameCode(code: String)(country: Country): Boolean = country.code.equalsIgnoreCase(code)
  private def bySameName(name: String)(country: Country): Boolean = country.name.equalsIgnoreCase(name)
}
