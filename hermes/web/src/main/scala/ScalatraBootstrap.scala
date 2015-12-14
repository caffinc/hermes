import javax.servlet.ServletContext

import com.caffinc.hermes.web.{ResourcesApp, HermesSwagger}
import com.caffinc.hermes.web.servlets.HermesHome
import org.scalatra.LifeCycle

/**
  * Created by SriramKeerthi on 12/13/2015.
  */
class ScalatraBootstrap extends LifeCycle {
  implicit val swagger = new HermesSwagger

  override def init(context: ServletContext) {
    context.mount(new HermesHome, "/*")
    context.mount(new ResourcesApp, "/docs")
  }
}
