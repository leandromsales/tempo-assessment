package io.tempo.teams.service

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@Controller
class BasicController : ResponseEntityExceptionHandler() {

    fun handle(ex: Exception, body: Any?, headers: HttpHeaders,
               status: HttpStatus, request: WebRequest): ResponseEntity<Any>? {
        return super.handleExceptionInternal(ex, body, headers, status, request)
    }

}
