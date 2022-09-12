package io.tempo.teams.service

import io.tempo.teams.service.core.exceptions.TempoException
import io.tempo.teams.service.core.exceptions.Errors
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.http.converter.HttpMessageNotWritableException
import org.springframework.stereotype.Controller
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.regex.Pattern

@ControllerAdvice(annotations = [ RestController::class, Controller::class ])
abstract class AbstractController : ResponseEntityExceptionHandler() {

    private companion object {
        private val LOG = KotlinLogging.logger {}
    }

    @Autowired
    private lateinit var basicController: BasicController

    /**
     * @return The default headers for all Controller Calls
     */
    fun makeDefaultHttpHeaders(additionalHeaders: MutableMap<String, String>? = null) : HttpHeaders {
        val ah = additionalHeaders ?: mapOf()

        val headers = HttpHeaders()

        // Additional headers to the response
        for (key in ah.keys) {
            headers.add(key, ah[key])
        }
        return headers
    }

    override fun handleHttpRequestMethodNotSupported(ex: HttpRequestMethodNotSupportedException,
                                                     headers: HttpHeaders, status: HttpStatus, request: WebRequest)
                                                        : ResponseEntity<Any> {
        val exception = Errors.GENERAL_GENERIC_ERROR.exception(ex.localizedMessage)
        exception.model.httpStatus = status
        exception.addField("original_exception", ex.javaClass.canonicalName)
        return handle(exception, request)!!
    }

    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException,
                                              headers: HttpHeaders, status: HttpStatus, request: WebRequest)
                                                                                                : ResponseEntity<Any> {
        val tempoException = TempoException(Errors.GENERAL_VALUE_INVALID, ex.message)
        return handle(tempoException, request)!!
    }

    override fun handleHttpMessageNotWritable(ex: HttpMessageNotWritableException,
                                              headers: HttpHeaders, status: HttpStatus, request: WebRequest)
                                                                                                : ResponseEntity<Any> {
        val tempoException = TempoException(Errors.GENERAL_VALUE_INVALID, ex.message)
        return handle(tempoException, request)!!
    }

    @ExceptionHandler(value = [TempoException::class, DataIntegrityViolationException::class,
        ConcurrentModificationException::class])
    fun handleAllExceptions(exception: java.lang.Exception, request: WebRequest) : ResponseEntity<Any>? {
        LOG.error("Error handling the request: ${exception.message}")

        if (exception is TempoException) {
            return handle(exception, request)
        }

        return handleUnderlyingExceptions(exception, request)
    }

    private fun handle(tempoException: TempoException, request: WebRequest, body: Any? = null): ResponseEntity<Any>?  {
        return if (body == null)
            basicController.handle(tempoException, tempoException, makeDefaultHttpHeaders(), tempoException.model.httpStatus, request)
        else
            basicController.handle(tempoException, body, makeDefaultHttpHeaders(), tempoException.model.httpStatus, request)
    }

    // Handle spring level exceptions and transforming them to simple exception format
    private fun handleUnderlyingExceptions(ex: java.lang.Exception, request: WebRequest) : ResponseEntity<Any>? {

        LOG.debug { "Underlying error with exception instance ${ex.javaClass.canonicalName}: ${ex.message}" }
        return when (ex) {
            is MissingRequestHeaderException -> {
                val matcher = Pattern.compile("'(.*)'").matcher(ex.message)
                val fieldName = if (matcher.find()) matcher.group(1) else ""
                val exception = Errors.GENERAL_MISSING_ARGUMENT.exception("")
                exception.model.addField("field", fieldName)
                exception.model.addField("type", "header")
                exception.addField("original_exception", ex.javaClass.canonicalName)
                handle(exception, request)
            }

            is HttpMessageNotReadableException -> {
                val tempoException = TempoException(Errors.GENERAL_VALUE_INVALID, ex.message)
                handle(tempoException, request)
            }

            is DataIntegrityViolationException -> {
//                // TODO: finish the implementation of data integrity violation exceptions
//                val CONSTRAINS_I18N_MAP = mapOf("user_id_idx" to "User with this id already exists.")
                val exception = Errors.GENERAL_VALUE_INVALID.exception(ex.message!!)
                exception.addField("original_exception", ex.javaClass.canonicalName)
                handle(exception, request)
            }

            else -> {
                val genericResponse = basicController.handleException(ex, request)
                val exception = Errors.GENERAL_GENERIC_ERROR.exception(ex.localizedMessage)
                exception.model.httpStatus = genericResponse!!.statusCode
                exception.addField("original_exception", ex.javaClass.canonicalName)
                handle(exception, request)

            }
        }
    }

    fun makeResponse(response: Any? = null, additionalHeaders: MutableMap<String, String>? = null) : ResponseEntity<Any> {
        return ResponseEntity.ok().headers(makeDefaultHttpHeaders(additionalHeaders)).body(response)
    }

}