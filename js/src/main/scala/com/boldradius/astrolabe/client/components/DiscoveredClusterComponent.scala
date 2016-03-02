package com.boldradius.astrolabe.client.components

import com.boldradius.astrolabe.client.components.ClusterFormComponent.{ EditClusterProps, State }
import com.boldradius.astrolabe.client.components.graph.Graph
import Graph.State
import Graph.State
import com.boldradius.astrolabe.client.d3._
import com.boldradius.astrolabe.client.modules.{ Roles, Mode, RxObserver }
import com.boldradius.astrolabe.client.services.ClusterService
import com.boldradius.astrolabe.client.style.GlobalStyles
import com.boldradius.astrolabe.http.{ DiscoveryBegun, ClusterForm, HostPort, DiscoveredCluster }
import japgolly.scalajs.react.extra.OnUnmount
import japgolly.scalajs.react.vdom.all._
import japgolly.scalajs.react._
import com.boldradius.astrolabe.client.services.Logger._
import rx._

import scala.scalajs.js

object DiscoveredClusterComponent {

  @inline private def globalStyles = GlobalStyles

  case class Props(discovered: Rx[Map[String, DiscoveredCluster]], selected: Rx[Option[DiscoveredCluster]], mode: Mode)

  case class State(rolesOpen: Option[String])

  class Backend(t: BackendScope[Props, State]) extends RxObserver(t) {

    def mounted(): Unit = {
      observe(t.props.discovered)
      observe(t.props.selected)

    }

    def selectCluster(e: ReactMouseEvent) = {
      ClusterService.selectCluster(e.currentTarget.firstChild.nodeValue)
      e.preventDefault()
    }

    def roles(system: String) = {
      log.debug("open roles " + system)
      t.modState(_.copy(rolesOpen = Some(system)))
    }

    def closeRolesForm() = {
      log.debug("closeRolesForm")
      t.modState(_.copy(rolesOpen = None))
    }

  }

  val component = ReactComponentB[Props]("DiscoveredClusterComponent")
    .initialState_P(P => {
      State(None)
    }) // initial state
    .backend(new Backend(_))
    .renderPS(($, P, S) => {
      val B = $.backend
      log.debug("************* S.rolesOpen " + S.rolesOpen)

      div(paddingTop := "30px")(
        if (P.discovered().isEmpty) {
          span("")
        } else {
          div(cls := "row", height := "200px")(
            S.rolesOpen.flatMap(role =>
              P.selected().map(cluster =>
                RolesFormComponent(cluster, () => B.closeRolesForm())
              )
            ).getOrElse[ReactElement](span("")),
            div(cls := "col-md-12")(
              div(cls := "row", borderBottom := "1px solid white")(
                div(cls := "col-md-12")(
                  span(fontSize := "20px", color := globalStyles.textColor)("Discovered"))),
              div(cls := "row")(
                P.discovered().values.map(e =>

                  if (isSelected(P, e.system) && P.mode == Roles) {
                    div(cls := "col-md-12", paddingTop := "10px", paddingBottom := "10px", backgroundColor := selectedBackground(P, e.system))(
                      a(href := "", key := e.system, fontSize := "18px")(
                        span(onClick ==> B.selectCluster,
                          color := selectedColor(P, e.system))(e.system)
                      ), span(cls := "pull-right")(button(cls := "btn btn-small", onClick --> B.roles(e.system))("Roles"))
                    )

                  } else {
                    div(cls := "col-md-12", paddingTop := "10px", paddingBottom := "10px", backgroundColor := selectedBackground(P, e.system))(
                      a(href := "", key := e.system, fontSize := "18px")(
                        span(onClick ==> B.selectCluster,
                          color := selectedColor(P, e.system))(e.system)
                      )
                    )

                  }
                )
              )
            )
          )
        }
      )

    }
    ).componentDidMount(_.backend.mounted())
    .configure(OnUnmount.install)
    .build

  def selectedColor(props: Props, system: String) =
    if (isSelected(props, system)) {
      globalStyles.textColor
    } else {
      globalStyles.navUnselectedTextColor
    }

  def selectedBackground(props: Props, system: String) =
    if (isSelected(props, system)) {
      "#6A777B"
    } else {
      ""
    }

  def isSelected(props: Props, system: String): Boolean =
    props.selected().exists(_.system == system)

  def apply(discovered: Rx[Map[String, DiscoveredCluster]],
    selected: Rx[Option[DiscoveredCluster]],
    mode: Mode) = component(Props(discovered, selected, mode))

}
