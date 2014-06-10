package com.gigaspaces.sbp.datageneration

import scala.util.Random
import com.gigaspaces.sbp.SpaceThing

/** User: jason
  * Date: 5/27/14
  * Time: 6:17 PM
  */
trait GeneratesSpaceThings {

  implicit val numPartitions: Int
  implicit val rand: Random

  def generateTestSpaceThing(payload: String = rand.nextString(8),
                             routeId: Int = rand.nextInt(numPartitions),
                             spaceId: java.lang.String = null): SpaceThing = {

    val testThing = new SpaceThing
    testThing.setPayload(payload)
    testThing.setRouteId(routeId)
    testThing.setSpaceId(spaceId) // auto-generated (hence null default var)
    testThing
  }


}
