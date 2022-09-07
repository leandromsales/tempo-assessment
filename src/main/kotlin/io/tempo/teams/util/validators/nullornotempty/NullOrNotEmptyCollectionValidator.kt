package io.tempo.teams.util.validators.nullornotempty

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class NullOrNotEmptyCollectionValidator : ConstraintValidator<NullOrNotEmptyCollection, Collection<*>> {

    private var min: Long = 1
    private var max: Long = Long.MAX_VALUE

    override fun initialize(parameters: NullOrNotEmptyCollection) {
        this.min = parameters.min
        this.max = parameters.max
    }

    override fun isValid(value: Collection<*>?, constraintValidatorContext: ConstraintValidatorContext) : Boolean {
        return ((value == null) || (value.size >= min))
    }

}