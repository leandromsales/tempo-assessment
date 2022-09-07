package io.tempo.teams.util.validators.nullorminmax

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ NullOrMinMaxValidator::class])
@MustBeDocumented
annotation class NullOrMinMax (

    val message: String = "{javax.validation.constraints.NullOrMinMax.message}",

    val min: Int = Int.MIN_VALUE,

    val max: Int = Int.MAX_VALUE,

    val groups: Array<KClass<*>> = [],

    val payload: Array<KClass<Payload>> = [],

)