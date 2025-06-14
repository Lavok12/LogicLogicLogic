package la.vok
import la.vok.LavokLibrary.getVec2
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.putVec
import processing.core.PApplet
import processing.data.JSONObject

fun main() {
    val obj = JSONObject()
    obj.putVec("s", Vec2(1000f, 100f)) // Явный вызов своей функции
    println(obj)                     // Проверка JSON
    println(obj.getVec2("s"))       // Проверка результата

    PApplet.main("la.vok.App")
}