package com.lwm.testing.perf.gatling

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class BootLoadSimulation extends Simulation {

  private val directUrl = "http://localhost:8090"
  private val sb1Url = "http://localhost:8081"
  private val sb2cUrl = "http://localhost:8082"
  private val sb2hUrl = "http://localhost:8083"

  private val endpoint = "/"
  private val contentType = "application/json"
  private val requestCount = 4

  private val simUsers = System.getProperty("SIM_USERS", "1").toInt

  private val directHttpConf = http
    .baseURL(directUrl)
    .acceptHeader("application/json;charset=UTF-8")
  private val sb1HttpConf = http
    .baseURL(sb1Url)
    .acceptHeader("application/json;charset=UTF-8")

  private val queryStatus = repeat(requestCount) {
    exec(http("status-test")
      .get(endpoint)
      .header("Content-Type", contentType)
      .check(status.is(200)))
  }

  private val queryPersons = repeat(requestCount) {
    exec(http("query_persons_direct")
      .get(endpoint + "persons")
      .header("Content-Type", contentType)
      .check(status.is(200)))
  }

  private val queryPersons2 = repeat(requestCount) {
    exec(http("query_persons_sb1")
      .get(endpoint + "persons")
      .header("Content-Type", contentType)
      .check(status.is(200)))
  }
  private val scn = scenario("com.lwm.test.scala.BootLoadSimulation")
    //    .exec(queryStatus)
    .exec(queryPersons)
  private val scn2 = scenario("com.lwm.test.scala.BootLoadSimulation2")
        .exec(queryPersons2)

  setUp(scn.inject(atOnceUsers(simUsers)).protocols(directHttpConf),
    scn2.inject(atOnceUsers(simUsers)).protocols(sb1HttpConf)
  )
}
