package io.tempo.teams.util.validators.nullorminmax

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class NullOrMinMaxValidator : ConstraintValidator<NullOrMinMax, Int> {

    private var min: Int = Int.MIN_VALUE
    private var max: Int = Int.MAX_VALUE

    override fun initialize(parameters: NullOrMinMax) {
        this.min = parameters.min
        this.max = parameters.max
    }

    override fun isValid(value: Int?, constraintValidatorContext: ConstraintValidatorContext) : Boolean {
        return value == null || (value >= this.min && value <= this.max)
    }

}