package io.tempo.teams.util.validators.notblankwithminmax

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class NotNullAndNotBlankWithMinMaxValidator : ConstraintValidator<NotBlankWithMinMax, String> {

    private var min: Long = 0
    private var max: Long = Long.MAX_VALUE

    override fun initialize(parameters: NotBlankWithMinMax) {
        this.min = parameters.min
        this.max = parameters.max
    }

    override fun isValid(value: String?, constraintValidatorContext: ConstraintValidatorContext) : Boolean {
        return (value != null && value.trim().isNotEmpty() && value.length >= this.min && value.length <= this.max)
    }

}