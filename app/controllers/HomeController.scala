package controllers

import javax.inject._
import play.api._
import play.api.mvc._

/**
 * App controller.
 */
@Singleton
class AppController @Inject() extends Controller {

  /**
   * Index action.
   */
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

}
