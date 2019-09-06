/*
 * Copyright (C) 2018-2019 Lightbend Inc. <https://www.lightbend.com>
 */

package akka.http

#if !scala213
  import scala.collection.generic.{ CanBuildFrom, GenericCompanion }
  import scala.collection.{ GenTraversable, mutable }
  import scala.{ collection => c }
#endif

/**
 * INTERNAL API
 */
package object ccompat {

  #if scala213
    type Builder[-A, +To] = scala.collection.mutable.Builder[A, To]
  #else
    private def simpleCBF[A, C](f: => mutable.Builder[A, C]): CanBuildFrom[Any, A, C] = new CanBuildFrom[Any, A, C] {
      def apply(from: Any): mutable.Builder[A, C] = apply()
      def apply(): mutable.Builder[A, C] = f
    }

    implicit def genericCompanionToCBF[A, CC[X] <: GenTraversable[X]](
      fact: GenericCompanion[CC]): CanBuildFrom[Any, A, CC[A]] =
      simpleCBF(fact.newBuilder[A])

    // This really belongs into scala.collection but there's already a package object
    // in scala-library so we can't add to it
    type IterableOnce[+X] = c.TraversableOnce[X]
    val IterableOnce = c.TraversableOnce
  #endif
}

/**
 * INTERNAL API
 */
package ccompat {
  #if scala213
    import akka.http.scaladsl.model.Uri.Query

    trait QuerySeqOptimized extends scala.collection.immutable.LinearSeq[(String, String)] with scala.collection.StrictOptimizedLinearSeqOps[(String, String), scala.collection.immutable.LinearSeq, Query] { self: Query =>
      override protected def fromSpecific(coll: IterableOnce[(String, String)]): Query =
        Query(coll.iterator.to(Seq): _*)

      override protected def newSpecificBuilder: Builder[(String, String), Query] =
        akka.http.scaladsl.model.Uri.Query.newBuilder

      override def empty: Query = akka.http.scaladsl.model.Uri.Query.Empty
    }
  #else
    trait Builder[-Elem, +To] extends mutable.Builder[Elem, To] { self =>
      // This became final in 2.13 so cannot be overridden there anymore
      final override def +=(elem: Elem): this.type = addOne(elem)
      def addOne(elem: Elem): this.type = self.+=(elem)
    }

    trait QuerySeqOptimized extends scala.collection.immutable.LinearSeq[(String, String)] with scala.collection.LinearSeqOptimized[(String, String), akka.http.scaladsl.model.Uri.Query] {
      self: akka.http.scaladsl.model.Uri.Query =>
      override def newBuilder: mutable.Builder[(String, String), akka.http.scaladsl.model.Uri.Query] = akka.http.scaladsl.model.Uri.Query.newBuilder
    }
  #endif
}
