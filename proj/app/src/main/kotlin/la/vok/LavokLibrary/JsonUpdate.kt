package la.vok.LavokLibrary

import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.Vec3
import la.vok.LavokLibrary.Vectors.Vec4
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

fun JSONObject.addReplaceIfAllFloat(obj: JSONObject): JSONObject {
    for (key in obj.keys()) {
        val value = obj.get(key as String)
        when (value) {
            is String -> this.setString(key, value)
            is Int -> this.setInt(key, value)
            is Float -> this.setFloat(key, value)
            is Double -> this.setDouble(key, value)
            is Boolean -> this.setBoolean(key, value)
            is JSONArray -> {
                if (this.hasKey(key) && !this.getJSONArray(key).isVector()) {
                    this.setJSONArray(key, this.getJSONArray(key).add(value))
                } else {
                    this.setJSONArray(key, value)
                }
            }
            is JSONObject -> {
                if (this.hasKey(key)) {
                    this.setJSONObject(key, this.getJSONObject(key).addReplaceIfAllFloat(value))
                } else {
                    this.setJSONObject(key, value)
                }
            }
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

// ===== Сохранение векторов в JSONObject =====

fun JSONObject.putVec(key: String, vec: Vec2): JSONObject {
    val arr = JSONArray()
    arr.append(vec.x)
    arr.append(vec.y)
    this.setJSONArray(key, arr)
    return this
}


fun JSONObject.putVec(key: String, vec: Vec3): JSONObject {
    val arr = JSONArray()
    arr.append(vec.x)
    arr.append(vec.y)
    arr.append(vec.z)
    this.setJSONArray(key, arr)
    return this
}

fun JSONObject.putVec(key: String, vec: Vec4): JSONObject {
    val arr = JSONArray()
    arr.append(vec.x)
    arr.append(vec.y)
    arr.append(vec.z)
    arr.append(vec.w)
    this.setJSONArray(key, arr)
    return this
}


// ===== Загрузка векторов из JSONObject =====

fun JSONObject.getVec2(key: String, default: Vec2 = Vec2(0f, 0f)): Vec2 {
    return try {
        val arr = getJSONArray(key)
        Vec2(arr.getFloat(0), arr.getFloat(1))
    } catch (e: Exception) {
        default
    }
}

fun JSONObject.getVec3(key: String, default: Vec3 = Vec3(0f, 0f, 0f)): Vec3 {
    return try {
        val arr = getJSONArray(key)
        Vec3(arr.getFloat(0), arr.getFloat(1), arr.getFloat(2))
    } catch (e: Exception) {
        default
    }
}

fun JSONObject.getVec4(key: String, default: Vec4 = Vec4(0f, 0f, 0f, 0f)): Vec4 {
    return try {
        val arr = getJSONArray(key)
        Vec4(arr.getFloat(0), arr.getFloat(1), arr.getFloat(2), arr.getFloat(3))
    } catch (e: Exception) {
        default
    }
}

// ===== Добавление векторов в JSONArray как массивов =====

fun JSONArray.putVec(vec: Vec2): JSONArray {
    return this.append(JSONArray().append(vec.x).append(vec.y))
}

fun JSONArray.putVec(vec: Vec3): JSONArray {
    return this.append(JSONArray().append(vec.x).append(vec.y).append(vec.z))
}

fun JSONArray.putVec(vec: Vec4): JSONArray {
    return this.append(JSONArray().append(vec.x).append(vec.y).append(vec.z).append(vec.w))
}

// ===== Загрузка векторов из JSONArray по индексу =====

fun JSONArray.getVec2(index: Int, default: Vec2 = Vec2(0f, 0f)): Vec2 {
    return try {
        val arr = getJSONArray(index)
        Vec2(arr.getFloat(0), arr.getFloat(1))
    } catch (e: Exception) {
        default
    }
}

fun JSONArray.getVec3(index: Int, default: Vec3 = Vec3(0f, 0f, 0f)): Vec3 {
    return try {
        val arr = getJSONArray(index)
        Vec3(arr.getFloat(0), arr.getFloat(1), arr.getFloat(2))
    } catch (e: Exception) {
        default
    }
}

fun JSONArray.getVec4(index: Int, default: Vec4 = Vec4(0f, 0f, 0f, 0f)): Vec4 {
    return try {
        val arr = getJSONArray(index)
        Vec4(arr.getFloat(0), arr.getFloat(1), arr.getFloat(2), arr.getFloat(3))
    } catch (e: Exception) {
        default
    }
}

fun JSONArray.isVector(): Boolean {
    val size = this.size()
    if (size !in 2..4) return false // Только Vec2, Vec3, Vec4

    for (i in 0 until size) {
        val value = this.get(i)
        when (value) {
            is Number -> continue // Уже число — ок
            is String -> {
                // Пытаемся преобразовать строку в число
                try {
                    value.toFloat()
                } catch (e: NumberFormatException) {
                    return false
                }
            }
            else -> return false // Ни число, ни строка — не вектор
        }
    }

    return true
}
