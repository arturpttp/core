package net.dev.art.core.libs.config

interface SpecificSimpleStorage<T> {

    val map: MutableMap<String, T?>?

    fun set(key: String, value: T?)
    fun get(key: String): T?
    fun getOrDefault(key: String, default: T?, autoSet: Boolean = true): T?
    fun setIfNotContains(key: String, value: T? = null)
    fun contains(key: String): Boolean
}
