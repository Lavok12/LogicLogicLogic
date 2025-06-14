package la.vok.LavokLibrary.Vectors

import kotlin.math.sqrt

data class Vec3(var x: Float = 0f, var y: Float = 0f, var z: Float = 0f) {

    constructor(p: Vec3) : this(p.x, p.y, p.z)
    constructor(p: Float) : this(p, p, p)
    constructor(p: Float, p2: Vec2) : this(p, p2.x, p2.y)
    constructor(p2: Vec2, p: Float) : this(p2.x, p2.y, p)

    // ===== Комбинированные свойства =====
    var xy: Vec2
        get() = Vec2(x, y)
        set(value) {
            x = value.x
            y = value.y
        }

    var xz: Vec2
        get() = Vec2(x, z)
        set(value) {
            x = value.x
            z = value.y
        }

    var yz: Vec2
        get() = Vec2(y, z)
        set(value) {
            y = value.x
            z = value.y
        }

    var yx: Vec2
        get() = Vec2(y, x)
        set(value) {
            y = value.x
            x = value.y
        }

    var zx: Vec2
        get() = Vec2(z, x)
        set(value) {
            z = value.x
            x = value.y
        }

    var zy: Vec2
        get() = Vec2(z, y)
        set(value) {
            z = value.x
            y = value.y
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

    var zz: Vec2
        get() = Vec2(z, z)
        set(value) {
            z = value.x
        }

    // ===== Комбинированные 3-компонентные свойства =====
    var xyz: Vec3
        get() = Vec3(x, y, z)
        set(value) {
            x = value.x
            y = value.y
            z = value.z
        }

    var xzy: Vec3
        get() = Vec3(x, z, y)
        set(value) {
            x = value.x
            z = value.y
            y = value.z
        }

    var yxz: Vec3
        get() = Vec3(y, x, z)
        set(value) {
            y = value.x
            x = value.y
            z = value.z
        }

    var yzx: Vec3
        get() = Vec3(y, z, x)
        set(value) {
            y = value.x
            z = value.y
            x = value.z
        }

    var zxy: Vec3
        get() = Vec3(z, x, y)
        set(value) {
            z = value.x
            x = value.y
            y = value.z
        }

    var zyx: Vec3
        get() = Vec3(z, y, x)
        set(value) {
            z = value.x
            y = value.y
            x = value.z
        }

    // ===== Повторы одного компонента =====
    var xxx: Vec3
        get() = Vec3(x, x, x)
        set(value) {
            x = value.x
        }

    var yyy: Vec3
        get() = Vec3(y, y, y)
        set(value) {
            y = value.x
        }

    var zzz: Vec3
        get() = Vec3(z, z, z)
        set(value) {
            z = value.x
        }

    // ===== Длина =====
    fun length(): Float = sqrt(x * x + y * y + z * z)

    fun lengthSquared(): Float = x * x + y * y + z * z

    // ===== Нормализация =====
    fun normalize(): Vec3 {
        val len = length()
        if (len != 0f) {
            x /= len
            y /= len
            z /= len
        }
        return this
    }

    fun normalized(): Vec3 {
        val len = length()
        return if (len != 0f) Vec3(x / len, y / len, z / len) else Vec3(0f)
    }

    // ===== Скалярное произведение =====
    infix fun dot(other: Vec3): Float = x * other.x + y * other.y + z * other.z

    // ===== Копия =====
    fun copy(): Vec3 = Vec3(x, y, z)

    // ===== Операторы =====
    operator fun plus(other: Vec3): Vec3 = Vec3(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: Vec3): Vec3 = Vec3(x - other.x, y - other.y, z - other.z)
    operator fun times(other: Vec3): Vec3 = Vec3(x * other.x, y * other.y, z * other.z)
    operator fun div(other: Vec3): Vec3 = Vec3(x / other.x, y / other.y, z / other.z)
    operator fun plus(scalar: Float): Vec3 = Vec3(x + scalar, y + scalar, z + scalar)
    operator fun minus(scalar: Float): Vec3 = Vec3(x - scalar, y - scalar, z - scalar)
    operator fun times(scalar: Float): Vec3 = Vec3(x * scalar, y * scalar, z * scalar)
    operator fun div(scalar: Float): Vec3 = Vec3(x / scalar, y / scalar, z / scalar)

    operator fun unaryMinus(): Vec3 = Vec3(-x, -y, -z)

    operator fun plusAssign(other: Vec3) {
        x += other.x
        y += other.y
        z += other.z
    }

    operator fun minusAssign(other: Vec3) {
        x -= other.x
        y -= other.y
        z -= other.z
    }

    operator fun timesAssign(scalar: Float) {
        x *= scalar
        y *= scalar
        z *= scalar
    }

    operator fun divAssign(scalar: Float) {
        x /= scalar
        y /= scalar
        z /= scalar
    }

    operator fun plusAssign(scalar: Float) {
        x += scalar
        y += scalar
        z += scalar
    }

    operator fun minusAssign(scalar: Float) {
        x -= scalar
        y -= scalar
        z -= scalar
    }


    override fun equals(other: Any?): Boolean {
        return other is Vec3 && x == other.x && y == other.y && z == other.z
    }

    override fun hashCode(): Int = 31 * x.hashCode() + y.hashCode() + z.hashCode()

    override fun toString(): String = "Vec3(x=$x, y=$y, z=$z)"
}
