package fr.kotmine.persistanceapi.utils

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Perm(val value: String)
