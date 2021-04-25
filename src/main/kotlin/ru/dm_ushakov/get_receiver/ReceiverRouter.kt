package ru.dm_ushakov.get_receiver

import org.apache.camel.builder.RouteBuilder
import org.apache.camel.model.rest.RestBindingMode
import org.springframework.context.annotation.Configuration

@Configuration
class ReceiverRouter:RouteBuilder() {
    private val messages = mutableListOf<Any>()

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
                setBody().mvel("request.headers")
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