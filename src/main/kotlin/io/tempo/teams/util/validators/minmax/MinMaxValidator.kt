package io.tempo.teams.util.validators.minmax

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class MinMaxValidator : ConstraintValidator<MinMax, Int> {

    private var min: Int = Int.MIN_VALUE
    private var max: Int = Int.MAX_VALUE

    override fun initialize(parameters: MinMax) {
        this.min = parameters.min
        this.max = parameters.max
    }

    override fun isValid(value: Int?, constraintValidatorContext: ConstraintValidatorContext) : Boolean {
        return (value != null && value >= this.min && value <= this.max)
    }

}