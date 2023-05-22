package fr.kotmine.persistanceapi.utils

import java.lang.reflect.Field

object Transcoder {
    fun encode(uuid: String): String {
        return uuid.replace("-", "")
    }

    fun decode(uuid: String): String {
        return uuid.toLowerCase().replace("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})".toRegex(), "$1-$2-$3-$4-$5")
    }

    fun getHashMapPerm(permissions: Any): HashMap<String, Boolean> {
        val result = HashMap<String, Boolean>()
        try {
            for (field: Field in permissions::class.java.declaredFields) {
                if (field.isAnnotationPresent(Perm::class.java)) {
                    field.isAccessible = true
                    result[field.getAnnotation(Perm::class.java).value] = field.getBoolean(permissions)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    fun setAnnotationValue(permissions: Any, key: String, value: Boolean) {
        try {
            for (field: Field in permissions::class.java.declaredFields) {
                field.isAccessible = true
                if (field.isAnnotationPresent(Perm::class.java) && field.getAnnotation(Perm::class.java).value == key) {
                    field.setBoolean(permissions, value)
                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}