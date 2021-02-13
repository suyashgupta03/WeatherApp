package com.weather.datasources.api.common

import com.orhanobut.logger.Logger
import kotlin.reflect.KProperty

fun <T, P : Any?> generateMappingError(className: String, prop: KProperty<P>): T? {
    Logger.w("Mapping error in '$className' at property '${prop.name}'")
    return null
}
