package io.shaka.http

// TODO Convert into automated test once code dependency has been sorted out
object ManualTestingTool extends App {
  private val ksConfig = PathAndPassword("naive-http/src/test/resources/certs/keystore-testing.jks", "password")
  private val tsConfig = PathAndPassword("../naive-http/server-truststore.jks", "password")

  val server = HttpServer.https(HttpsConfig(ksConfig, tsConfig), 7878).start()
  println("Mutual SSL auth http server started at https://localhost:" + server.port)
}