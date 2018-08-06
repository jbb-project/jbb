package org.jbb.restloadtesting

import java.util.UUID

import com.typesafe.config._
import io.gatling.commons.validation.Validation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.util.Random

class ApiSimulation extends Simulation {
  val conf = ConfigFactory.load()
  val baseUrl = conf.getString("baseUrl")
  val httpProtocol = http.baseURL(baseUrl) //sample comment
  val restEndPoint = "/api/v1/members"
  val getMemberScenario = scenario("GET /v1/members")
    .exec(http("GET/v1/members")
      .get(restEndPoint)
      .header("Accept", "application/json")
      .check(status.is(200)))
  val postMemberScenario = scenario("POST /v1/members")
    .exec(http(("POST/v1/members"))
      .post("/api/v1/members")
      .header("Content-Type", "application/json")
      .header("Accept", "application/json")
      .body(StringBody(s => sa(s))).asJSON
      .check(status.is(201))
    )

  def orderRef() = Random.nextInt(Integer.MAX_VALUE)

  def sa(session: Session): Validation[String] = {
    val member = Math.abs(UUID.randomUUID().toString.hashCode)
    s"""{
       |  "displayedName": "LTest$member",
       |  "email": "LTest$member@jbb.com",
       |  "password": "passw",
       |  "username": "LTest$member"
       |}""".stripMargin
  }

  //  setUp(getMemberScenario.inject(constantUsersPerSec(100) during (3 minutes))).throttle(
  //    reachRps(100) in (30 seconds),
  //    holdFor(1 minute)).protocols(httpProtocol)

  setUp(postMemberScenario.inject(rampUsersPerSec(0) to 50 during (30 seconds),
    constantUsersPerSec(50) during (5 minutes)
  ).protocols(httpProtocol))
}
