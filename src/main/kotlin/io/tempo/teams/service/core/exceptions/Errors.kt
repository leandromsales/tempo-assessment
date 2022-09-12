package io.tempo.teams.service.core.exceptions

import com.fasterxml.jackson.annotation.JsonValue
import org.springframework.http.HttpStatus
import java.lang.IllegalArgumentException

// TODO: add missing error description
// TODO: what is the best way to deal with erro code and responses

enum class Errors(val value: Int, val description: String, val httpStatus: HttpStatus? = null,
                  val exceptionClass: Class<*>? = null) {

    // General erro codes
    GENERAL_NO_ERROR(0, "Success"),
    GENERAL_GENERIC_ERROR(1, "Error from the underlying layer not detected by Tempo Software. " +
            "Check additional description for details.", HttpStatus.INTERNAL_SERVER_ERROR),
    GENERAL_INTERNAL_SERVER_ERROR(2, "Internal server error.", HttpStatus.INTERNAL_SERVER_ERROR),
    GENERAL_STATUS_NOT_EXPECTED(3, "Transaction status not expected.", HttpStatus.CONFLICT),
    GENERAL_NOT_IMPLEMENTED_YET(4, "Not implemented yet.", HttpStatus.CONFLICT),
    GENERAL_MISSING_ARGUMENT(5, "Missing argument.", HttpStatus.BAD_REQUEST),
    GENERAL_VALUE_INVALID(6, "Invalid value for parameter.", HttpStatus.BAD_REQUEST),

    // Team
    TEAM_NOT_FOUND(1000, "Team not found.", HttpStatus.NOT_FOUND),
    TEAM_LINKAGE_ERROR(1004, "Error trying to link entity to the team.", HttpStatus.CONFLICT),
    TEAM_USER_NOT_LINKED(1005, "Entity is not linked to the team.", HttpStatus.BAD_REQUEST),
    TEAM_SAVE_ERROR(1006, "Error on saving team.", HttpStatus.CONFLICT),
    TEAM_DELETE_FAILURE(1007, "Failure deleting team."),
    TEAM_MISSING_TEAM_LEADER(1008, "Missing team leader.", HttpStatus.CONFLICT),

    // User
    USER_ID_ALREADY_IN_USE(2000, "", HttpStatus.CONFLICT),
    USER_NOT_FOUND(2001, "", HttpStatus.NOT_FOUND),
    USER_INVALID_ID(2002, "Invalid user identifier.", HttpStatus.BAD_REQUEST),
    USER_INVALID(2009, "The expected user is not valid", HttpStatus.BAD_REQUEST),
    USER_UPDATE_FAILURE(2007, "Failure updating user."),
    USER_DELETE_FAILURE(2008, "Failure deleting user."),
    USER_PERMISSION_NOT_FOUND(2009, "", HttpStatus.NOT_FOUND),

    // Role
    ROLE_SET_USER(3005, "Failure setting user role to a team."),
    ROLE_ID_ALREADY_IN_USE(3000, "", HttpStatus.CONFLICT),
    ROLE_NOT_FOUND(3001, "", HttpStatus.NOT_FOUND),
    ROLE_INVALID_ID(3002, "Invalid role identifier.", HttpStatus.BAD_REQUEST),
    ROLE_INVALID(3003, "The expected role is not valid", HttpStatus.BAD_REQUEST),
    ROLE_UPDATE_FAILURE(3004, "Failure updating role."),
    ROLE_DELETE_FAILURE(3005, "Failure deleting role."),
    ROLE_SAVE_ERROR(3006, "Error on saving role.", HttpStatus.CONFLICT),

    ;

    fun exception(message: String, cause: Throwable? = null) : TempoException {
        return exception(message, null, cause, null)
    }

    fun exception(message: String, additionalFields: MutableMap<String, Any>) : TempoException {
        return exception(message, additionalFields, null, null)
    }

    fun exception(message: String, httpStatus: HttpStatus) : TempoException {
        return exception(message, null, null, httpStatus)
    }

    fun exception(message: String,
                  additionalFields: MutableMap<String, Any>?,
                  cause: Throwable?) : TempoException {
        return exception(message, additionalFields, cause, null)
    }

    // TODO: review this approach, because it seems to be good since avoid us to create an exception class for each
    //  situation, so use it in all exception throws.
    fun exception(message: String,
                  additionalFields: MutableMap<String, Any>?,
                  cause: Throwable?,
                  httpStatus: HttpStatus?) : TempoException {
        val tempoException = if (this.exceptionClass != null) {
            this.exceptionClass.getDeclaredConstructor(String::class.java, Throwable::class.java)
                .newInstance(message, cause) as TempoException
        } else {
            TempoException(message, this, httpStatus ?: this.httpStatus ?: HttpStatus.INTERNAL_SERVER_ERROR, cause)
        }
        if (additionalFields != null) tempoException.model.additionalFields = additionalFields
        return tempoException
    }

    @Throws(TempoException::class)
    fun throwError(message: String, additionalFields: MutableMap<String, Any>? = null, cause: Throwable? = null) {
        throw this.exception(message, additionalFields, cause)
    }

    @Throws(TempoException::class)
    fun throwError(message: String, cause: Throwable? = null) {
        throw this.exception(message, null, cause)
    }

    @JsonValue
    fun value(): Int {
        return value
    }

    fun description(): String {
        return description
    }

    companion object {

        fun valueOf(value: Int): Errors {
            return resolve(value)
                ?: throw IllegalArgumentException("No matching constant for [$value]")
        }

        fun resolve(value: Int): Errors? {
            for (errorCode in values()) {
                if (errorCode.value == value) {
                    return errorCode
                }
            }
            return null
        }
    }
}