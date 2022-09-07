package io.tempo.teams.util.validators.nullornotempty

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ NullOrNotEmptyCollectionValidator::class])
@MustBeDocumented
annotation class NullOrNotEmptyCollection (

    val message: String = "{javax.validation.constraints.NullOrNotEmptyCollection.message}",

    val min: Long = 1,

    val max: Long = Long.MAX_VALUE,

    val groups: Array<KClass<*>> = [],

    val payload: Array<KClass<Payload>> = []

)