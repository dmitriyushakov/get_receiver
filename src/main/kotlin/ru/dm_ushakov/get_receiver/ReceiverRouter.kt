package ru.dm_ushakov.get_receiver

import org.apache.camel.builder.RouteBuilder
import org.apache.camel.model.rest.RestBindingMode
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Configuration
class ReceiverRouter:RouteBuilder() {
    private val messages = mutableListOf<Any>()
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS") as DateTimeFormatter

    private fun getCurrentDatetime():String {
        val dateTime = LocalDateTime.now()
        return dateTimeFormatter.format(dateTime)
    }

    private fun parseGetParams(encoded:String):Map<String,Any> =
        encoded
            .split('&')
            .map { it.split('=') }
            .associate { it[0] to it[1] }

    override fun configure() {
        restConfiguration().apply {
            component = "undertow"
            host = "localhost"
            port = "8080"
            bindingMode = RestBindingMode.json
            contextPath = "/"
            apiContextPath = "/doc"
        }

        rest().apply {
            get("/receive").route().apply {
                setBody().mvel("request.headers.CamelHttpQuery")
                setHeader("datetime", this@ReceiverRouter::getCurrentDatetime)
                setBody().message { parseGetParams(it.body as String) + mapOf("datetime" to it.getHeader("datetime")) }
                process { messages.add( it.`in`.body ) }
            }.endRest()

            get("/messages").route().apply {
                setBody().body { _ -> messages }
            }.endRest()

            get("/clear").route().apply {
                process { messages.clear() }
                setBody().constant("OK")
            }.endRest()
        }
    }
}