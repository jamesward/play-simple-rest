import java.io.File

import controllers.Assets
import play.api.libs.json.Json
import play.api.mvc.{Controller, Action, RequestHeader}
import play.api.GlobalSettings
import play.core.StaticApplication
import play.navigator._
import play.navigator.{PlayNavigator, PlayResourcesController}
import scala.collection.mutable

object Routes extends PlayNavigator {
  
  val index = GET on root to { () => Assets.at(path="/public", "index.html") }
  
  val bars = resources("bars", Bars)

  val webjars = GET on "webjars" / ** to { s: String => Assets.at(path="/META-INF/resources/webjars", s) }
  
}

case class Bar(id: Option[Int], name: String)

object Bars extends PlayResourcesController[Int] {

  var bars = mutable.HashMap.empty[Int, Bar]
  def nextId = if (bars.keys.isEmpty) 0 else bars.keys.max + 1
  
  implicit val barReads = Json.reads[Bar]
  implicit val barWrites = Json.writes[Bar]
  
  def index() = Action {
    Ok(Json.toJson(bars.values))
  }
  
  def `new`() = Routes.redirect(Routes.index().url)()

  def create() = Action(parse.json) { request =>
    Json.fromJson(request.body).map { bar =>
      val createdBar = bar.copy(Some(nextId))
      bars += createdBar.id.get -> createdBar
      Ok(Json.toJson(createdBar))
    }.recoverTotal { error =>
      BadRequest(error.toString)
    }
  }
  
  def show(id: Int) = Action {
    if (bars.contains(id))
      Ok(Json.toJson(bars(id)))
    else
      NotFound
  }
  
  def edit(id: Int) = Routes.redirect(Routes.index().url + "#" + id)()
  
  def update(id: Int) = Action(parse.json) { request =>
    Json.fromJson(request.body).map { bar =>
      if (bar.id.get == id) {
        bars(id) = bar
        Ok(Json.toJson(bar))
      }
      else {
        BadRequest("Something fishy going on with your request")
      }
    }.recoverTotal { error =>
      BadRequest(error.toString)
    }
  }
  
  def delete(id: Int) = Action {
    bars.remove(id)
    Ok
  }
  
}

object Global extends App with GlobalSettings {
  new play.core.server.NettyServer(new StaticApplication(new File(".")), 9000)
  override def onRouteRequest(request: RequestHeader) = Routes.onRouteRequest(request)
  override def onHandlerNotFound(request: RequestHeader) = Routes.onHandlerNotFound(request)
}