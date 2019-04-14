package com.lwm.testing.perf.gatling

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BootLoadSimulation extends Simulation {

  private val directUrl2 = "http://192.168.3.9:8080"
  private val directUrl = "http://192.168.3.9:8090"
  private val sb1Url = "http://192.168.3.9:8081"
  private val sb2cUrl = "http://192.168.3.9:8082"
  private val sb2hUrl = "http://192.168.3.9:8083"
  private val goUrl = "http://localhost:8000"

  private val endpoint = "/"
  private val contentType = "application/json"
  private val requestCount = 4

  private val simUsers = System.getProperty("SIM_USERS", "1").toInt

  private val directHttpConf = http
    .baseURL(directUrl)
    .acceptHeader("application/json;charset=UTF-8")

  private val directHttpConf2 = http
    .baseURL(directUrl2)
    .acceptHeader("application/json;charset=UTF-8")

  private val sb1HttpConf = http
    .baseURL(sb1Url)
    .acceptHeader("application/json;charset=UTF-8")
  private val sb2cHttpConf = http
    .baseURL(sb2cUrl)
    .acceptHeader("application/json;charset=UTF-8")
  private val sb2hHttpConf = http
    .baseURL(sb2hUrl)
    .acceptHeader("application/json;charset=UTF-8")
  private val goHttpConf = http
    .baseURL(goUrl)
    .acceptHeader("application/json;charset=UTF-8")

  private val queryStatus = repeat(requestCount,"n" ) {
    exec(http("status-test")
      .get(endpoint))
//      .header("Content-Type", contentType)
//      .check(status.is(200)))
  }

  private val queryUsers = repeat(requestCount,"n" ) {
    exec(http("query_user_direct")
      .get(endpoint + "user/async?delay="+2000)
    )
  }

  private val queryPersons = repeat(requestCount,"n" ) {
    exec(http("query_persons_direct")
      .get(endpoint + "persons"))
//      .header("Content-Type", contentType)
//      .check(status.is(200)))
  }
  private val scn = scenario("com.lwm.test.scala.BootLoadSimulation")
    //    .exec(queryStatus)
    .exec(queryPersons)

  private val queryPersons2 = repeat(requestCount) {
    exec(http("query_persons_sb1")
      .get(endpoint + "persons")
      .header("Content-Type", contentType)
      .check(status.is(200)))
  }
  private val scn2 = scenario("com.lwm.test.scala.BootLoadSimulation2")
    .exec(queryPersons2)

  private val queryPersons3 = repeat(requestCount) {
    exec(http("query_persons_sbc")
      .get(endpoint + "persons")
      .header("Content-Type", contentType)
      .check(status.is(200)))
  }
  private val scn3 = scenario("com.lwm.test.scala.BootLoadSimulation3")
    .exec(queryPersons3)

  private val queryPersons4 = repeat(requestCount) {
    exec(http("query_persons_sbh")
      .get(endpoint + "persons")
      .header("Content-Type", contentType)
      .check(status.is(200)))
  }
  private val scn4 = scenario("com.lwm.test.scala.BootLoadSimulation4")
    .exec(queryPersons4)

  private val queryPersons5 = repeat(requestCount) {
    exec(http("query_persons_go")
      .get(endpoint + "persons")
      .header("Content-Type", contentType)
      .check(status.is(200)))
  }
  private val scn5 = scenario("com.lwm.test.scala.BootLoadSimulation5")
    .exec(queryPersons5)

  private val scn6 = scenario("com.lwm.test.scala.BootLoadSimulation6")
    .exec(queryUsers)

  setUp(
//    scn.inject(rampUsers(simUsers) over (30 seconds)).protocols(directHttpConf),
//    scn6.inject(rampUsers(simUsers) over (30 seconds)).protocols(directHttpConf2),
//    scn2.inject(rampUsers(simUsers) over (30 seconds)).protocols(sb1HttpConf),
    scn3.inject(rampUsers(simUsers) over (30 seconds)).protocols(sb2cHttpConf),
//    scn4.inject(rampUsers(simUsers) over (30 seconds)).protocols(sb2hHttpConf),
//      scn5.inject(rampUsers(simUsers) over (30 seconds)).protocols(goHttpConf),
  )

}
