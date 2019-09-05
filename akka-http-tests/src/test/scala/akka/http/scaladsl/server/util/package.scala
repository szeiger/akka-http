/*
 * Copyright (C) 2009-2019 Lightbend Inc. <https://www.lightbend.com>
 */

package akka.http.scaladsl.server

@if(!scala213)
package object util {
  type VarArgsFunction1[-T, +U] = (T*) => U
}

@if(scala213)
package util {
  // in 2.13 (T*) => U is not a valid type any more, this works on 2.12+ as a drop in replacement
  trait VarArgsFunction1[-T, +U] {
    def apply(alternatives: T*): U
  }
}
