package io.tempo.teams.util.validators.minmax

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ MinMaxValidator::class])
@MustBeDocumented
annotation class MinMax (

    val message: String = "{javax.validation.constraints.MinMax.message}",

    val min: Int = Int.MIN_VALUE,

    val max: Int = Int.MAX_VALUE,

    val groups: Array<KClass<*>> = [],

    val payload: Array<KClass<Payload>> = [],

)