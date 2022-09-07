package io.tempo.teams.service.core.exceptions

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import java.lang.RuntimeException
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpStatus

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(value = ["stackTrace", "message", "localizedMessage", "cause" ])
open class TempoException : RuntimeException {

    @JsonProperty(value = "TempoException")
    open var model = TempoExceptionModel()

    constructor()

    constructor(errorCode: Errors) : super(errorCode.description()) {
        model.errorCodeAsErrors = errorCode
    }

    constructor(errorCode: Errors, additionalDescription: String?) : super(errorCode.description()) {
        model.errorCodeAsErrors = errorCode
        model.additionalDescription = additionalDescription!!
    }

    constructor(errorCode: Errors, httpStatus: HttpStatus) : super(errorCode.description()) {
        model.errorCodeAsErrors = errorCode
        model.httpStatus = httpStatus
    }

    constructor(additionalDescription: String, errorCode: Errors, cause: Throwable? = null)
            : super(errorCode.description(), cause) {
        model.errorCodeAsErrors = errorCode
        model.additionalDescription = additionalDescription
    }

    constructor(additionalDescription: String, errorCode: Errors, httpStatus: HttpStatus, cause: Throwable? = null)
            : super(errorCode.description(), cause) {
        model.errorCodeAsErrors = errorCode
        model.httpStatus = httpStatus
        model.additionalDescription = additionalDescription
    }

    constructor(errorCode: Errors, httpStatus: HttpStatus?, additionalDescription: String?)
            : super(errorCode.description()) {
        model.errorCodeAsErrors = errorCode
        model.httpStatus = httpStatus!!
        model.additionalDescription = additionalDescription!!
    }

    fun addField(key: String?, value: Any?) {
        model.addField(key!!, value!!)
    }

    override fun toString(): String {
        return "TempoException{" +
                "errorCode=" + model.getErrorCode() +
                ", description='" + model.description + '\'' +
                ", additionalErrorCode=" + model.httpStatus.value() +
                ", additionalDescription='" + model.additionalDescription + '\'' +
                '}'
    }
}