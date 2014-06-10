package com.gigaspaces.sbp.gstest

/** User: jason
  * Date: 5/27/14
  * Time: 8:42 PM
  */
object SpaceMode extends Enumeration {
  type SpaceMode = Value
  val Embedded, Remote, LocalCache, LocalView = Value
}
