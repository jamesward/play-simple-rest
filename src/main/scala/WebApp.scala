import java.io.File
import play.api.libs.json.Json
import play.api.mvc.{RequestHeader, Action}
import play.api.mvc.Results._
import play.api.GlobalSettings
import play.core.StaticApplication

object Global extends App with GlobalSettings {

  implicit val messageWrites = Json.writes[Message]

  new play.core.server.NettyServer(new StaticApplication(new File(".")), 9000)

  override def onRouteRequest(request: RequestHeader) = Some(Action(Ok(Json.toJson(Message("hello, world")))))
  
  case class Message(value: String)
  
}