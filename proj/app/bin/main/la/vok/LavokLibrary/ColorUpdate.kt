package la.vok.LavokLibrary

import java.awt.Color
import kotlin.math.*

operator fun Color.plus(other: Color): Color = Color(
    (this.red + other.red).coerceIn(0, 255),
    (this.green + other.green).coerceIn(0, 255),
    (this.blue + other.blue).coerceIn(0, 255),
    (this.alpha + other.alpha).coerceIn(0, 255)
)

operator fun Color.minus(other: Color): Color = Color(
    (this.red - other.red).coerceIn(0, 255),
    (this.green - other.green).coerceIn(0, 255),
    (this.blue - other.blue).coerceIn(0, 255),
    (this.alpha - other.alpha).coerceIn(0, 255)
)


operator fun Color.times(other: Color): Color = Color(
    ((this.red / 255.0) * (other.red / 255.0) * 255).roundToInt().coerceIn(0, 255),
    ((this.green / 255.0) * (other.green / 255.0) * 255).roundToInt().coerceIn(0, 255),
    ((this.blue / 255.0) * (other.blue / 255.0) * 255).roundToInt().coerceIn(0, 255),
    ((this.alpha / 255.0) * (other.alpha / 255.0) * 255).roundToInt().coerceIn(0, 255)
)

operator fun Color.div(other: Color): Color = Color(
    if (other.red != 0) (this.red * 255 / other.red).coerceIn(0, 255) else 255,
    if (other.green != 0) (this.green * 255 / other.green).coerceIn(0, 255) else 255,
    if (other.blue != 0) (this.blue * 255 / other.blue).coerceIn(0, 255) else 255,
    if (other.alpha != 0) (this.alpha * 255 / other.alpha).coerceIn(0, 255) else 255
)


operator fun Color.plus(value: Int): Color = Color(
    (this.red + value).coerceIn(0, 255),
    (this.green + value).coerceIn(0, 255),
    (this.blue + value).coerceIn(0, 255),
    (this.alpha + value).coerceIn(0, 255)
)

operator fun Color.minus(value: Int): Color = Color(
    (this.red - value).coerceIn(0, 255),
    (this.green - value).coerceIn(0, 255),
    (this.blue - value).coerceIn(0, 255),
    (this.alpha - value).coerceIn(0, 255)
)

operator fun Color.plus(value: Float): Color = Color(
    (this.red + value).coerceIn(0f, 255f),
    (this.green + value).coerceIn(0f, 255f),
    (this.blue + value).coerceIn(0f, 255f),
    (this.alpha + value).coerceIn(0f, 255f)
)

operator fun Color.minus(value: Float): Color = Color(
    (this.red - value).coerceIn(0f, 255f),
    (this.green - value).coerceIn(0f, 255f),
    (this.blue - value).coerceIn(0f, 255f),
    (this.alpha - value).coerceIn(0f, 255f)
)

operator fun Color.times(value: Float): Color = Color(
    (this.red * value).roundToInt().coerceIn(0, 255),
    (this.green * value).roundToInt().coerceIn(0, 255),
    (this.blue * value).roundToInt().coerceIn(0, 255),
    (this.alpha * value).roundToInt().coerceIn(0, 255)
)

operator fun Color.div(value: Float): Color = Color(
    (this.red / value).roundToInt().coerceIn(0, 255),
    (this.green / value).roundToInt().coerceIn(0, 255),
    (this.blue / value).roundToInt().coerceIn(0, 255),
    (this.alpha / value).roundToInt().coerceIn(0, 255)
)

fun Color.copy(): Color = Color(red, green, blue, alpha)

fun Color.toHex(withAlpha: Boolean = true): String {
    return if (withAlpha) {
        String.format("#%02X%02X%02X%02X", red, green, blue, alpha)
    } else {
        String.format("#%02X%02X%02X", red, green, blue)
    }
}

fun Color.toRgbaString(): String {
    return "rgba($red, $green, $blue, ${alpha / 255.0})"
}
