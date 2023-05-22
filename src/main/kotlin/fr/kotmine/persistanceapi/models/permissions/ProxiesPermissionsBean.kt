package fr.kotmine.persistanceapi.models.permissions

import fr.kotmine.persistanceapi.utils.Perm
import fr.kotmine.persistanceapi.utils.Transcoder

data class ProxiesPermissionsBean(
    private val groupsId: Long,
    @Perm("proxies.dispatch")
    var proxiesDispatch: Boolean,
    @Perm("proxies.global")
    var proxiesGlobal: Boolean,
    @Perm("proxies.debug")
    var proxiesDebug: Boolean,
    @Perm("proxies.setoption")
    var proxiesSetOption: Boolean,
    @Perm("proxies.hydro")
    var proxiesHydro: Boolean
) {
    // Reverse the bean to HashMap
    fun getHashMap(): Map<String, Boolean> {
        return Transcoder.getHashMapPerm(this)
    }

    // Set a value into the HashMap
    fun set(key: String, value: Boolean) {
        Transcoder.setAnnotationValue(this, key, value)
    }
}

