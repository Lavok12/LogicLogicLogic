package la.vok.LavokLibrary

import processing.data.JSONObject
import processing.data.JSONArray

fun JSONObject.add(obj: JSONObject): JSONObject {
    for (key in obj.keys()) {
        val value = obj.get(key as String)
        when (value) {
            is String -> this.setString(key, value)
            is Int -> this.setInt(key, value)
            is Float -> this.setFloat(key, value)
            is Double -> this.setDouble(key, value)
            is Boolean -> this.setBoolean(key, value)
            is JSONArray -> {
                if (this.hasKey(key)) {
                    this.setJSONArray(key, this.getJSONArray(key).add(value))
                } else {
                    this.setJSONArray(key, value)
                }
            }
            is JSONObject -> {
                if (this.hasKey(key)) {
                    this.setJSONObject(key, this.getJSONObject(key).add(value))
                } else {
                    this.setJSONObject(key, value)
                }
            }
        }
    }
    return this
}

fun JSONArray.add(obj: JSONArray): JSONArray {
    for (i in 0 until obj.size()) {
        val value = obj.get(i)
        when (value) {
            is String -> this.append(value)
            is Int -> this.append(value)
            is Float -> this.append(value)
            is Double -> this.append(value)
            is Boolean -> this.append(value)
            is JSONArray -> this.append(value)
            is JSONObject -> this.append(value)
        }
    }
    return this
}


fun JSONObject.LgetInt(key: String, default: Int = 0): Int {
    if (!this.hasKey(key)) return default
    return try {
        when (val value = this.get(key)) {
            is Int -> value
            is Number -> value.toInt()
            is String -> value.toIntOrNull() ?: default
            else -> default
        }
    } catch (e: Exception) {
        default
    }
}

fun JSONObject.LgetFloat(key: String, default: Float = 0f): Float {
    if (!this.hasKey(key)) return default
    return try {
        when (val value = this.get(key)) {
            is Float -> value
            is Number -> value.toFloat()
            is String -> value.toFloatOrNull() ?: default
            else -> default
        }
    } catch (e: Exception) {
        default
    }
}

fun JSONObject.LgetDouble(key: String, default: Double = 0.0): Double {
    if (!this.hasKey(key)) return default
    return try {
        when (val value = this.get(key)) {
            is Double -> value
            is Number -> value.toDouble()
            is String -> value.toDoubleOrNull() ?: default
            else -> default
        }
    } catch (e: Exception) {
        default
    }
}

fun JSONObject.LgetString(key: String, default: String = ""): String {
    if (!this.hasKey(key)) return default
    return try {
        this.get(key).toString()
    } catch (e: Exception) {
        default
    }
}

fun JSONObject.LgetBoolean(key: String, default: Boolean = false): Boolean {
    if (!this.hasKey(key)) return default
    return try {
        when (val value = this.get(key)) {
            is Boolean -> value
            is String -> value.equals("true", ignoreCase = true)
            is Number -> value.toInt() != 0
            else -> default
        }
    } catch (e: Exception) {
        default
    }
}

fun JSONObject.LgetJSONArray(key: String, default: JSONArray = JSONArray()): JSONArray {
    if (!this.hasKey(key)) return default
    return try {
        this.getJSONArray(key)
    } catch (e: Exception) {
        default
    }
}

fun JSONObject.LgetJSONObject(key: String, default: JSONObject = JSONObject()): JSONObject {
    if (!this.hasKey(key)) return default
    return try {
        this.getJSONObject(key)
    } catch (e: Exception) {
        default
    }
}

fun JSONArray.LgetInt(index: Int, default: Int = 0): Int {
    if (index < 0 || index >= this.size()) return default
    return try {
        when (val value = this.get(index)) {
            is Int -> value
            is Number -> value.toInt()
            is String -> value.toIntOrNull() ?: default
            else -> default
        }
    } catch (e: Exception) {
        default
    }
}

fun JSONArray.LgetFloat(index: Int, default: Float = 0f): Float {
    if (index < 0 || index >= this.size()) return default
    return try {
        when (val value = this.get(index)) {
            is Float -> value
            is Number -> value.toFloat()
            is String -> value.toFloatOrNull() ?: default
            else -> default
        }
    } catch (e: Exception) {
        default
    }
}

fun JSONArray.LgetDouble(index: Int, default: Double = 0.0): Double {
    if (index < 0 || index >= this.size()) return default
    return try {
        when (val value = this.get(index)) {
            is Double -> value
            is Number -> value.toDouble()
            is String -> value.toDoubleOrNull() ?: default
            else -> default
        }
    } catch (e: Exception) {
        default
    }
}

fun JSONArray.LgetString(index: Int, default: String = ""): String {
    if (index < 0 || index >= this.size()) return default
    return try {
        this.get(index).toString()
    } catch (e: Exception) {
        default
    }
}

fun JSONArray.LgetBoolean(index: Int, default: Boolean = false): Boolean {
    if (index < 0 || index >= this.size()) return default
    return try {
        when (val value = this.get(index)) {
            is Boolean -> value
            is String -> value.equals("true", ignoreCase = true)
            is Number -> value.toInt() != 0
            else -> default
        }
    } catch (e: Exception) {
        default
    }
}

fun JSONArray.LgetJSONArray(index: Int, default: JSONArray = JSONArray()): JSONArray {
    if (index < 0 || index >= this.size()) return default
    return try {
        this.getJSONArray(index)
    } catch (e: Exception) {
        default
    }
}

fun JSONArray.LgetJSONObject(index: Int, default: JSONObject = JSONObject()): JSONObject {
    if (index < 0 || index >= this.size()) return default
    return try {
        this.getJSONObject(index)
    } catch (e: Exception) {
        default
    }
}
