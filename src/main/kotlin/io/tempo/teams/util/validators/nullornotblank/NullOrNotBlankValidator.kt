package io.tempo.teams.util.validators.nullornotblank

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class NullOrNotBlankValidator : ConstraintValidator<NullOrNotBlank, String> {

    private var min: Long = 0
    private var max: Long = Long.MAX_VALUE

    override fun initialize(parameters: NullOrNotBlank) {
        this.min = parameters.min
        this.max = parameters.max
    }

    override fun isValid(value: String?, constraintValidatorContext: ConstraintValidatorContext) : Boolean {
        return ((value == null) || (value.trim().isNotEmpty() && value.length >= this.min && value.length <= this.max))
    }

}