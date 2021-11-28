package net.dev.art.core.libs.interfaces

interface Replaces {

    val replaces: MutableMap<String, String> get() = mutableMapOf()

    fun addReplacer(key: String, value: String): Replaces {
        replaces[key] = value
        return this
    }

    fun copy(replace: Replaces): Replaces {
        replace.replaces.let { replaces.putAll(it) }
        return this
    }

    fun getReplaced(string: String): String = string.apply {
        replaces.forEach { this.replace(it.key, it.value) }
    }

    fun getReplaced(vararg strings: String): Array<String> = strings.map {
        getReplaced(it)
    }.toTypedArray()

}