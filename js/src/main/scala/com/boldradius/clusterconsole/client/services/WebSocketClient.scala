package com.boldradius.clusterconsole.client.services

import com.boldradius.clusterconsole.http.ClusterProtocol
import org.scalajs.dom
import org.scalajs.dom.raw._
import upickle.default._
import com.boldradius.clusterconsole.http.Json._

object WebSocketClient extends Console {

  var open: Boolean = false

  lazy val websocket = new WebSocket(getWebsocketUri(dom.document))

  websocket.onopen = { (event: Event) =>
    ClusterService.findDiscoveringClusters()
    ClusterService.findDiscoveredClusters()
    event
  }
  websocket.onerror = { (event: ErrorEvent) => }

  websocket.onmessage = { (event: MessageEvent) =>
    Logger.log.info("WS Msg Raw: " + event.data.toString)
    val msg: ClusterProtocol = read[ClusterProtocol](event.data.toString)
    Logger.log.info("msg: " + msg)
    MainDispatcher.dispatch(msg)
    event
  }

  websocket.onclose = { (event: Event) => }

  def getWebsocketUri(document: Document): String = {
    val wsProtocol = if (dom.document.location.protocol == "https:") "wss" else "ws"

    s"$wsProtocol://${dom.document.location.host}/events"
  }

  def send(msg: ClusterProtocol): Unit = {
    websocket.send(write(msg))
  }

}
