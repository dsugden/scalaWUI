package ui

import org.scalajs.dom
import org.scalajs.dom.{XMLHttpRequest, HTMLElement}
import scala.concurrent.duration._
import scala.concurrent.{Future, Await}
import scala.util.{Failure, Success}
import scalatags.JsDom._
import all._
import tags2.section
import rx._
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
import org.scalajs.dom.extensions.Ajax
import scala.Some
import shared._
import autowire._

import fr.iscpif.scaladget.JsRxTags._

@JSExport
object Plot {

  val helloValue = Var(0)

  @JSExport
  def run() {
    val submitButton = button("Click me")(
      cursor := "pointer",
      onclick := { () => Post[Api](_.hello(5)).foreach { i =>
        helloValue() = helloValue() + i
      }
        false
      }
    ).render

    Rx {
      dom.document.body.appendChild(submitButton)
      dom.document.body.appendChild(h1(helloValue).render)
    }
  }
}

object Post extends autowire.Client[Web] {

  override def callRequest(req: Request): Future[String] = {
    val url = req.path.mkString("/")
    dom.extensions.Ajax.post(
      url = "http://localhost:8080/" + url,
      data = upickle.write(req.args)
    ).map {
      _.responseText
    }
  }
}
