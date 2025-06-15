package la.vok.LavokLibrary.Vectors

import kotlin.math.sqrt

data class Vec2(var x: Float = 0f, var y: Float = 0f) {

    constructor(p: Vec2) : this(p.x, p.y)
    constructor(p: Float) : this(p, p)

    // ===== Свойства-комбинации =====
    var xy: Vec2
        get() = Vec2(x, y)
        set(value) {
            x = value.x
            y = value.y
        }

    var yx: Vec2
        get() = Vec2(y, x)
        set(value) {
            y = value.x
            x = value.y
        }

    var xx: Vec2
        get() = Vec2(x, x)
        set(value) {
            x = value.x
        }

    var yy: Vec2
        get() = Vec2(y, y)
        set(value) {
            y = value.x
        }

    // ===== Длина =====
    fun length(): Float = sqrt(x * x + y * y)

    fun lengthSquared(): Float = x * x + y * y

    // ===== Нормализация =====
    fun normalize(): Vec2 {
        val len = length()
        if (len != 0f) {
            x /= len
            y /= len
        }
        return this
    }

    fun normalized(): Vec2 {
        val len = length()
        return if (len != 0f) Vec2(x / len, y / len) else Vec2(0f, 0f)
    }

    // ===== Скалярное произведение =====
    infix fun dot(other: Vec2): Float = x * other.x + y * other.y

    // ===== Копия =====
    fun copy(): Vec2 = Vec2(x, y)

    // ===== Операторы =====
    operator fun plus(other: Vec2): Vec2 = Vec2(x + other.x, y + other.y)
    operator fun minus(other: Vec2): Vec2 = Vec2(x - other.x, y - other.y)
    operator fun times(other: Vec2): Vec2 = Vec2(x * other.x, y * other.y)
    operator fun div(other: Vec2): Vec2 = Vec2(x / other.x, y / other.y)
    operator fun plus(scalar: Float): Vec2 = Vec2(x + scalar, y + scalar)
    operator fun minus(scalar: Float): Vec2 = Vec2(x - scalar, y - scalar)
    operator fun times(scalar: Float): Vec2 = Vec2(x * scalar, y * scalar)
    operator fun div(scalar: Float): Vec2 = Vec2(x / scalar, y / scalar)

    // ===== Присваивающие операторы с числом =====
    operator fun plusAssign(scalar: Float) {
        x += scalar
        y += scalar
    }
    operator fun minusAssign(scalar: Float) {
        x -= scalar
        y -= scalar
    }

    operator fun unaryMinus(): Vec2 = Vec2(-x, -y)

    operator fun plusAssign(other: Vec2) {
        x += other.x
        y += other.y
    }


    operator fun minusAssign(other: Vec2) {
        x -= other.x
        y -= other.y
    }

    operator fun timesAssign(scalar: Float) {
        x *= scalar
        y *= scalar
    }

    operator fun divAssign(scalar: Float) {
        x /= scalar
        y /= scalar
    }

    override fun equals(other: Any?): Boolean {
        return other is Vec2 && x == other.x && y == other.y
    }

    override fun hashCode(): Int = 31 * x.hashCode() + y.hashCode()

    override fun toString(): String = "Vec2(x=$x, y=$y)"
}