package com.boldradius.astrolabe.client.components.graph

import com.boldradius.astrolabe.client.style.GlobalStyles
import com.boldradius.astrolabe.http.RoleDependency
import japgolly.scalajs.react.vdom.all.svg._
import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.{ ReactComponentB, _ }

object ClusterDependencyLegend {

  case class Props(dep: RoleDependency, index: Int, selected: Boolean, selectDep: (RoleDependency, Boolean) => Unit)

  case class State(selected: Boolean)

  class Backend(t: BackendScope[Props, State]) {
    def select = {
      t.modState(_.copy(selected = !t.state.selected))
      t.props.selectDep(t.props.dep, !t.state.selected)
    }
  }

  val component = ReactComponentB[Props]("ClusterDependencyLegend")
    .initialState_P(P => State(P.selected))
    .backend(new Backend(_))
    .renderPS { ($, P, S) =>
      val B = $.backend
      val label = P.dep.tpe.name + ": " + P.dep.tpe.typeName + ". " + P.dep.roles.mkString(",") + "-->" + P.dep.dependsOn.mkString(",")

      val rectwidth = (label.length * 9) + 20

      g({
        import japgolly.scalajs.react.vdom.all._
        onClick --> B.select
      })(rect(width := rectwidth.toString, height := "40", fill := {
        if (S.selected) {
          LegendColors.colors(P.index % 5)
        } else {
          GlobalStyles.leftNavBackgrounColor
        }
      }, x := 0, y := (P.index * 45) + 5, stroke := GlobalStyles.textColor),

        text(x := 10, y := (P.index * 45) + 30, fill := GlobalStyles.textColor, fontSize := "15px", fontFamily := "Courier")(label)
      )
    }.build

  def apply(dep: RoleDependency, index: Int, selected: Boolean, selectDep: (RoleDependency, Boolean) => Unit) =
    component(Props(dep, index, selected, selectDep))
}
