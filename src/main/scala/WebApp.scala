import java.io.File
import play.api.libs.json.Json
import play.api.mvc.{RequestHeader, Action}
import play.api.mvc.Results._
import play.api.{GlobalSettings, Play, Mode, DefaultApplication}
import play.core.{Router, StaticApplication}

object WebApp extends App {

  implicit val messageWrites = Json.writes[Message]

  val server = new play.core.server.NettyServer(new StaticApplication(new File(".")), 9000)

  def index = Action {
    Ok(Json.toJson(Message("hello, world")))
  }
  
  case class Message(value: String)
  
}

object Global extends GlobalSettings {

  override def onRouteRequest(request: RequestHeader) = {
    // route all requests to the WebApp.index method
    Some(WebApp.index)
  }
}