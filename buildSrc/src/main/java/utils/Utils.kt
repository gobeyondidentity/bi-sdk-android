package utils

import org.gradle.api.Project
import java.io.FileInputStream
import java.util.Properties

val propsMap: MutableMap<String, Properties> = mutableMapOf()

fun Project.props(path: String = "$rootDir/buildProperties/build-publish.properties"): Properties {
    if (!propsMap.containsKey(path)) {
        val fis = FileInputStream(path)
        val prop = Properties()
        prop.load(fis)
        propsMap[path] = prop
    }

    return propsMap[path]!!
}

fun Project.getProp(key: String, path: String = "$rootDir/buildProperties/build-publish.properties"): Any {
    return props(path)[key] ?: ""
}
