/*
 * Copyright (C) 2018-2019 Lightbend Inc. <https://www.lightbend.com>
 */

package akka.http.ccompat

object MapHelpers {
  @if(scala213)
  def convertMapToScala[K, V](jmap: java.util.Map[K, V]): scala.collection.immutable.Map[K, V] = {
    import scala.collection.JavaConverters._
    Map.empty.concat(jmap.asScala)
  }

  @if(!scala213)
  def convertMapToScala[K, V](jmap: java.util.Map[K, V]): scala.collection.immutable.Map[K, V] = {
    import scala.collection.JavaConverters._
    Map.empty ++ jmap.asScala
  }
}
