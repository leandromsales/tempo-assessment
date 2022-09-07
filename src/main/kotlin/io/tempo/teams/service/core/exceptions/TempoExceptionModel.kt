package io.tempo.teams.service.core.exceptions

import com.fasterxml.jackson.annotation.*
import org.springframework.http.HttpStatus
import java.util.HashMap

class TempoExceptionModel {

    @JsonProperty(value = "error_code")
    var errorCodeAsErrors: Errors? = null
        set(value) {
            field = value
            this.description = value?.description()
        }

    var description: String? = ""

    @JsonIgnore
    var httpStatus = HttpStatus.INTERNAL_SERVER_ERROR

    @JsonProperty(value = "additional_description")
    var additionalDescription: String? = ""

    @JsonProperty(value = "additional_fields")
    var additionalFields: MutableMap<String, Any> = HashMap()

    @JsonGetter(value = "error_code")
    fun getErrorCode(): Int? {
        return errorCodeAsErrors?.value()
    }

    @JsonSetter(value = "error_code")
    fun setErrorCode(errorCode: Int?) {
        errorCodeAsErrors = Errors.valueOf(errorCode!!)
    }

    fun addField(key: String, value: Any) {
        additionalFields[key] = value
    }

    override fun toString(): String {
        return "TempoException{" +
                "errorCode=" + errorCodeAsErrors +
                ", description='" + errorCodeAsErrors?.description() + '\'' +
                ", additionalErrorCode=" + httpStatus +
                ", additionalDescription='" + additionalDescription + '\'' +
                ", additionalFields=" + additionalFields +
                '}'
    }
}