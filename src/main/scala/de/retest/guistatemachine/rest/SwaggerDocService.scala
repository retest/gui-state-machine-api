package de.retest.guistatemachine.rest

import com.github.swagger.akka.SwaggerHttpService
import com.github.swagger.akka.model.Info
import io.swagger.models.ExternalDocs

object SwaggerDocService extends SwaggerHttpService {
  override val apiClasses = Set(classOf[GuiStateMachineService])
  override val host = s"${WebServer.Host}:${WebServer.Port}"
  override val info = Info(version = "1.0")
  override val externalDocs = Some(new ExternalDocs().description("Core Docs").url("http://acme.com/docs"))
  //override val securitySchemeDefinitions = Map("basicAuth" -> new BasicAuthDefinition())
  override val unwantedDefinitions = Seq("Function1", "Function1RequestContextFutureRouteResult")
}
