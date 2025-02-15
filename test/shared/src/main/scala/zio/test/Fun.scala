/*
 * Copyright 2019 John A. De Goes and the ZIO Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package zio.test

import zio.ZIO

/**
 * A `Fun[A, B]` is a referentially transparent version of a potentially
 * effectual function from `A` to `B`. Each invocation of the function will be
 * memoized so the function is guaranteed to return the same value for any
 * given input. The function should not involve asynchronous effects.
 */
private[test] final case class Fun[-A, +B] private (private val f: A => B, private val hash: A => Int)
    extends (A => B) {

  final def apply(a: A): B = map.getOrElseUpdate(hash(a), f(a))

  private[this] final val map = ConcurrentHashMap.empty[Int, B]
}

private[test] object Fun {

  /**
   * Constructs a new `Fun` from an effectual function. The function should not
   * involve asynchronous effects.
   */
  final def make[R, A, B](f: A => ZIO[R, Nothing, B]): ZIO[R, Nothing, Fun[A, B]] =
    makeHash(f)(_.hashCode)

  /**
   * Constructs a new `Fun` from an effectual function and a hashing function.
   * This is useful when the domain of the function does not implement
   * `hashCode` in a way that is consistent with equality.
   */
  final def makeHash[R, A, B](f: A => ZIO[R, Nothing, B])(hash: A => Int): ZIO[R, Nothing, Fun[A, B]] =
    ZIO.runtime[R].map(runtime => Fun(a => runtime.unsafeRun(f(a)), hash))

  /**
   * Constructs a new `Fun` from a pure function.
   */
  final def fromFunction[A, B](f: A => B): Fun[A, B] =
    Fun(f, _.hashCode)
}
