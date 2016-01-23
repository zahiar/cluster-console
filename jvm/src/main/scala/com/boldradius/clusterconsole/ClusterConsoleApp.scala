package com.boldradius.clusterconsole

import akka.actor.{ ActorRef, ActorSystem, Props }
import akka.util.Timeout
import com.boldradius.clusterconsole.core.LogF
import com.boldradius.clusterconsole.http._
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._

trait ClusterConsoleAppBase extends LogF { self: App =>

  def useClusterMetrics: Boolean

  val clusterMetricsStanza =
    """akka.extensions = [ "akka.cluster.metrics.ClusterMetricsExtension" ]"""

  // todo - abstract .clusterconsole role into a variable
  val akkaConf =
    s"""akka.remote.netty.tcp.hostname="127.0.0.1"
      |akka.remote.netty.tcp.port=3001
      |akka.cluster.roles = [clusterconsole]
      |${if (useClusterMetrics) clusterMetricsStanza else ""}
      |""".stripMargin

  println(akkaConf)

  val config = ConfigFactory.parseString(akkaConf).withFallback(ConfigFactory.load())

  val clusterConsoleSystem = ActorSystem("ClusterConsoleSystem", config)

  val router: ActorRef = clusterConsoleSystem.actorOf(Props[RouterActor], "router")

  clusterConsoleSystem.actorOf(
    HttpServiceActor.props("127.0.0.1", 9000, Timeout(30 seconds), router),
    "clusterconsolehttp"
  )

}

/*object ClusterConsoleApp extends App with ClusterConsoleAppBase {
  args.logInfo(s"ClusterConsoleApp starting with args:" + _.toList.toString)
  val useClusterMetrics = false
}*/

object ClusterConsoleWithMetricsApp extends App with ClusterConsoleAppBase {
  args.logInfo(s"ClusterConsoleMetricsApp (with Metrics) starting with args:" + _.toList.toString)
  val useClusterMetrics = true
}
