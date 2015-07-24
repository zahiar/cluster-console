package clusterconsole.client.components

import clusterconsole.client.modules.RxObserver
import japgolly.scalajs.react.extra.OnUnmount
import rx._
import clusterconsole.client.d3.Layout.{ GraphLinkForce, GraphNodeForce }
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.all._
//import japgolly.scalajs.react.vdom.prefix_<^._
import scala.scalajs.js
import clusterconsole.client.d3._
import js.JSConverters._
import clusterconsole.client.services.Logger._

//object ActionAssignment {
//
//  import clusterconsole.client.style.CustomTags._
//
//
//  val component = ReactComponentB[Unit]("ActionAssignment")
//    .render(P => {
//    svgtag(
//      path(^.key := "acg", d := "M19 3h-4.18C14.4 1.84 13.3 1 12 1c-1.3 0-2.4.84-2.82 2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-7 0c.55 0 1 .45 1 1s-.45 1-1 1-1-.45-1-1 .45-1 1-1zm2 14H7v-2h7v2zm3-4H7v-2h10v2zm0-4H7V7h10v2z")
//    )
//  }).buildU
//
//  def apply() = component()
//}

object ClusterNodeGraphComponent {

  case class Props()

  case class State()
  class Backend(t: BackendScope[Props, State]) extends RxObserver(t) {
    def mounted(): Unit = {
    }

  }

  def component = ReactComponentB[Props]("ClusterNodeGraph")
    .initialStateP(P => {
      State()
    }).backend(new Backend(_))
    .render((P, S, B) => {

      val nodes: List[GraphNodeForce] =
        List(Node("1"), Node("2"), Node("3")).zipWithIndex.map {
          case (node, i) =>
            js.Dynamic.literal(
              "index" -> i,
              "x" -> 0,
              "y" -> 0,
              "px" -> 0,
              "py" -> 0,
              "fixed" -> false,
              "weight" -> 0
            ).asInstanceOf[GraphNodeForce]
        }

      val links: List[GraphLinkForce] =
        List(Link(0, 1), Link(0, 2), Link(1, 2)).zipWithIndex.map {
          case (link, i) =>
            js.Dynamic.literal("source" -> nodes(link.source), "target" -> nodes(link.target)).asInstanceOf[GraphLinkForce]
        }
      div(
        Graph(600, 600, nodes, links)
      )

    }).componentDidMount(_.backend.mounted())
    .configure(OnUnmount.install)
    .build

  def apply() = component(Props())

}
