package com.boldradius.astrolabe.client.modules

import com.boldradius.astrolabe.client.ClusterConsoleApp.Loc
import japgolly.scalajs.react.extra.router2.RouterCtl
import japgolly.scalajs.react.vdom.all._
import japgolly.scalajs.react.{ ReactComponentB, _ }

object Dashboard {
  // create the React component for Dashboard
  val component = ReactComponentB[RouterCtl[Loc]]("Dashboard")
    .render((P, S) => {
      console.log("Dashboard Initialised.")
      div(cls := "container", paddingTop := "6px")("Hey Katrin!$$$$$$$$ This is the Dashboard Module.")
    }).build
}
