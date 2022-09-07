package io.tempo.teams.util.validators.notblankwithminmax

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ NotNullAndNotBlankWithMinMaxValidator::class])
@MustBeDocumented
annotation class NotBlankWithMinMax (

    val message: String = "{javax.validation.constraints.NotNullAndNotBlankWithMinMax.message}",

    val min: Long = Long.MIN_VALUE,

    val max: Long = Long.MAX_VALUE,

    val groups: Array<KClass<*>> = [],

    val payload: Array<KClass<Payload>> = []

)