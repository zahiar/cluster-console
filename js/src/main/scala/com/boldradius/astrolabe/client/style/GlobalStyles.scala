package com.boldradius.astrolabe.client.style

import scala.language.postfixOps
import scalacss.Defaults._

object GlobalStyles extends StyleSheet.Inline {
  import dsl._

  val textColor = "#EAD0D0"

  val modalBackground = "#E8E3E3"

  val navUnselectedTextColor = "#8ED5EA"

  val mainHeaderColor = "#2C3138"

  val leftNavBackgrounColor = "#39484E"

  val mapBackground = "#353131"

  val nodeUpColor = "#2FA02B"
  val nodeUnreachableColor = "#D46415"
  val nodeRemovedColor = "#B91414"

  val metricsColor = "#ed903f"

  val common = mixin(
    backgroundColor(Color(mapBackground))
  )

  val button = style(
    padding(0.5 ex, 2 ex),
    backgroundColor(Color("#eee")),
    border(1 px, solid, black)
  )

  val leftNav = style("leftNav")(
    border(1 px, solid, white),
    backgroundColor(Color("#39484E"))
  )

  val mainHeaders = style("mainHeaders")(
    backgroundColor(Color(mainHeaderColor)),
    borderColor(white),
    borderBottom(1 px, solid)
  )

  val regText = style("regText")(
    color(Color(textColor))
  )

  style(

    unsafeRoot("body")(
      common,
      paddingTop(50.px),

      unsafeChild("h3")(
        color(Color(textColor))
      ),
      unsafeChild("h2")(
        color(Color(textColor))
      ),
      unsafeChild("h4")(
        color(Color(textColor))
      )

    )

  )

  val bootstrapStyles = new BootstrapStyles
}
