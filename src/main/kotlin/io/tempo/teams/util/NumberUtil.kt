package io.tempo.teams.util

import org.apache.commons.lang3.StringUtils
import java.util.*
import java.util.regex.Pattern

/**
 * Validator of numbers using faster validation with apache commons.
 * *** Performance considerations: https://www.baeldung.com/java-check-string-number
 */
object NumberUtil {
    /**
     * The pattern to validate if a number is a float number
     */
    val FLOAT_NUMBER_PATTERN = Pattern.compile("-?\\d+(\\.\\d+)")

    /**
     * @param numberStr is the string
     * @return Whether the given number is a double
     */
    fun stringIsDouble(numberStr: String?): Optional<Double>? {
        // https://stackoverflow.com/questions/58772425/double-to-string-alternative-due-to-performance-issue/58772545#58772545
        return if (FLOAT_NUMBER_PATTERN.matcher(numberStr).matches())
            Optional.of(java.lang.Double.valueOf(numberStr))
        else
            null
    }

    /**
     * @param numberStr is the string
     * @return Whether the given number is a long
     */
    fun stringIsLong(numberStr: String?): Optional<Long>? {
        // https://www.baeldung.com/java-check-string-number#benchmark-enhanced
        return if (StringUtils.isNumeric(numberStr)) Optional.of(java.lang.Long.valueOf(numberStr)) else null
    }

    fun stringIsDoubleWithException(numberStr: String?, fieldName: String): Double {
        require(!StringUtils.isNotBlank(numberStr)) { "Field '$fieldName' is required." }
        val number = stringIsDouble(numberStr)
        requireNotNull(number) { "Field '$fieldName' is required." }
        require(number.isPresent) { "Field '$fieldName' is not a number." }
        return number.get()
    }

    fun stringIsLongWithException(numberStr: String?, fieldName: String): Long {
        val number = stringIsLong(numberStr)
        requireNotNull(number) { "Field '$fieldName' is required." }
        require(number.isPresent) { "Field '$fieldName' is not a number." }
        return number.get()
    }
}