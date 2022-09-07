package io.tempo.teams.util.validators.nullornotblank

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ NullOrNotBlankValidator::class])
@MustBeDocumented
annotation class NullOrNotBlank (

    val message: String = "{javax.validation.constraints.NullOrNotBlank.message}",

    val min: Long = Long.MIN_VALUE,

    val max: Long = Long.MAX_VALUE,

    val groups: Array<KClass<*>> = [],

    val payload: Array<KClass<Payload>> = []

)