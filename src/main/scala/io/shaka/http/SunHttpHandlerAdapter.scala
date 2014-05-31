package io.shaka.http

import io.shaka.http.Http.HttpHandler
import io.shaka.http.Method._
import io.shaka.http.Headers._
import scala.Some
import scala.io.Source
import com.sun.net.httpserver.{HttpExchange => SunHttpExchange, HttpHandler => SunHttpHandler}


class SunHttpHandlerAdapter(handler: HttpHandler) extends SunHttpHandler {
  override def handle(exchange: SunHttpExchange): Unit = {
    respond(exchange, handler(request(exchange)))
    exchange.close()
  }

  private def request(exchange: SunHttpExchange) = Request(
    method(exchange.getRequestMethod),
    exchange.getRequestURI.toString,
    toHeaders(exchange.getRequestHeaders),
    Some(Entity(Source.fromInputStream(exchange.getRequestBody).map(_.toByte).toArray))
  )

  private def respond(exchange: SunHttpExchange, response: Response) {
    response.headers.foreach {
      header =>
        exchange.getResponseHeaders.add(header._1.name, header._2)
    }
    exchange.sendResponseHeaders(response.status.code, response.entity.map(_.content.length).getOrElse(0).toLong)
    response.entity.foreach {
      entity =>
        val os = exchange.getResponseBody
        os.write(entity.content)
        os.close()
    }
  }
}
