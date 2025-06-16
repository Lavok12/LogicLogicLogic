package la.vok.LavokLibrary

import processing.data.JSONObject
import processing.data.JSONArray
import kotlin.reflect.KProperty

open class MetaStorage {
    private val meta = mutableMapOf<String, Any>()

    // 1. Делегат доступа к полю через key
    fun <T : Any> tag(key: String, default: T? = null) = object {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
            return getTag(key) ?: default
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            setTag(key, value)
        }
    }

    // 2. Очистка
    fun clearTags() {
        meta.clear()
    }

    // 3. toString для отладки
    override fun toString(): String {
        return "MetaStorage(${meta.mapValues { it.value.toString() }})"
    }

    // 4. Экспорт и импорт (Map)
    fun toMap(): Map<String, Any> = meta.toMap()

    fun fromMap(data: Map<String, Any>) {
        meta.clear()
        meta.putAll(data)
    }

    // ✅ Экспорт в JSON
    fun toJSON(): JSONObject {
        val json = JSONObject()
        for ((key, value) in meta) {
            when (value) {
                is Int -> json.setInt(key, value)
                is Float -> json.setFloat(key, value)
                is Boolean -> json.setBoolean(key, value)
                is String -> json.setString(key, value)
                is List<*> -> {
                    val arr = JSONArray()
                    value.forEach { arr.append(it.toString()) }
                    json.setJSONArray(key, arr)
                }
                is JSONObject -> json.setJSONObject(key, value)
                else -> json.setString(key, value.toString()) // fallback
            }
        }
        return json
    }

    // ✅ Импорт из JSON
    fun fromJSON(json: JSONObject) {
        meta.clear()
        for (key in json.keys()) {
            val value = json.get(key as String)
            when (value) {
                is Int, is Float, is Boolean, is String, is JSONObject, is JSONArray -> meta[key] = value
                else -> meta[key] = value.toString()
            }
        }
    }

    // 6. Проверка типа
    fun <T : Any> isTagOfType(key: String, clazz: Class<T>): Boolean {
        return clazz.isInstance(meta[key])
    }

    // 8. Сокращённые методы
    fun setInt(key: String, value: Int) = setTag(key, value)
    fun getInt(key: String): Int? = getTag(key)

    fun setFloat(key: String, value: Float) = setTag(key, value)
    fun getFloat(key: String): Float? = getTag(key)

    fun setString(key: String, value: String) = setTag(key, value)
    fun getString(key: String): String? = getTag(key)

    // Базовые методы
    fun <T : Any> setTag(key: String, value: T) {
        meta[key] = value
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getTag(key: String): T? {
        return meta[key] as? T
    }

    fun hasTag(key: String): Boolean = key in meta

    fun removeTag(key: String) {
        meta.remove(key)
    }

    fun tagKeys(): Set<String> = meta.keys

    // Read-only доступ ко всем (для внешнего чтения)
    fun getAllTags(): Map<String, Any> = meta.toMap()
}
