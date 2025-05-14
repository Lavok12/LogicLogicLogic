package la.volk.Render.Elements

import la.vok.Render.MainRender
import la.vok.Render.RenderElements.RenderElements
import la.vok.Render.LCanvas
import la.vok.Storages.Storage
import la.vok.LoadData.*
import la.vok.GameController.GameController
import org.luaj.vm2.*
import org.luaj.vm2.lib.jse.JsePlatform
import processing.core.PApplet
import processing.data.JSONObject
import processing.data.JSONArray
import la.vok.LavokLibrary.*

open class LElement(
    var x: Float = 0f,
    var y: Float = 0f,
    var width: Float = 200f,
    var height: Float = 100f,
    var alignX: Float = 0f,
    var alignY: Float = 0f,
    var parentCanvas: LCanvas = Storage.gameController.mainRender.mainCanvas,
    var percentWidth: Float = -1f,
    var percentHeight: Float = -1f,
    var offsetByWidth: Float = 0f,
    var offsetByHeight: Float = 0f,
    var maxWidth: Float = 0f,
    var maxHeight: Float = 0f,
    var minWidth: Float = 0f,
    var minHeight: Float = 0f,
    var tag: String = "",
) {
    open var PX: Float = 0f
    open var PY: Float = 0f
    open var SX: Float = 0f
    open var SY: Float = 0f
    open var elementCanvas: LCanvas = LCanvas(0f, 0f, 0f, 0f, 1f, 1f, 1f)
    open var isVisibl: Boolean = true

    open var gameController: GameController = Storage.gameController;

    open fun replacePlaceholders(text: String, i: Int, varName: String, eval: (String) -> LuaValue = LuaEvaluator::eval): String {
        var result1 = text.replace(varName, i.toString())
        var result = result1.replace("\\", "")
    
        val regex = "<(.*?)>".toRegex()
        result = regex.replace(result) { matchResult ->
            val code = matchResult.groupValues[1]
            println("code $code")
            eval(code).toString()
        }
    
        println("res $result")
        return result
    }
    

    open fun checkChilds(json: JSONObject) {
        if (json.hasKey("childs")) {
            val childs = json.getJSONArray("childs")
            for (i in 0 until childs.size()) {
                when (val child = childs[i]) {
                    is JSONObject -> gameController.loadUIList.addChilds(child, elementCanvas)
                    is String -> gameController.loadUIList.addChilds(child, elementCanvas)
                }
            }
        }

        if (json.hasKey("whileChilds")) {
            var wc = json.getJSONObject("whileChilds")
            var iterations = if (wc.hasKey("iterations")) wc.getInt("iterations") else 1
            var varName = if (wc.hasKey("varName")) wc.getString("varName") else "&i"

            iterations -= 1

            for (i in 0..iterations) {
                var iwcs = wc.toString()
                iwcs = replacePlaceholders(iwcs, i, varName) { code ->
                    val engine = JsePlatform.standardGlobals()  // Загружаем Lua среду
                    val compiledCode = engine.load("return " + code)  // Компилируем Lua-выражение
                    compiledCode.call()  // Выполняем
                }
                var wci = JSONObject.parse(iwcs)
                gameController.loadUIList.addChilds(wci, elementCanvas)
            }
        }
    }

    companion object {
        fun JSONToElement(json: JSONObject, parentCanvas: LCanvas, gameController: GameController): LElement {
            val x = if (json.hasKey("x")) json.getFloat("x") else 0f
            val y = if (json.hasKey("y")) json.getFloat("y") else 0f
            val width = if (json.hasKey("width")) json.getFloat("width") else 0f
            val height = if (json.hasKey("height")) json.getFloat("height") else 0f
            val alignX = if (json.hasKey("alignX")) json.getFloat("alignX") else 0f
            val alignY = if (json.hasKey("alignY")) json.getFloat("alignY") else 0f
            val percentWidth = if (json.hasKey("percentWidth")) json.getFloat("percentWidth") else 0f
            val percentHeight = if (json.hasKey("percentHeight")) json.getFloat("percentHeight") else 0f
            val offsetByWidth = if (json.hasKey("offsetByWidth")) json.getFloat("offsetByWidth") else 0f
            val offsetByHeight = if (json.hasKey("offsetByHeight")) json.getFloat("offsetByHeight") else 0f
            val maxWidth = if (json.hasKey("maxWidth")) json.getFloat("maxWidth") else Float.MAX_VALUE
            val maxHeight = if (json.hasKey("maxHeight")) json.getFloat("maxHeight") else Float.MAX_VALUE
            val minWidth = if (json.hasKey("minWidth")) json.getFloat("minWidth") else 0f
            val minHeight = if (json.hasKey("minHeight")) json.getFloat("minHeight") else 0f
            val tag = if (json.hasKey("tag")) json.getString("tag") else ""

            var ret = LElement(
                x, y, width, height, alignX, alignY, parentCanvas,
                percentWidth, percentHeight,
                offsetByWidth, offsetByHeight,
                maxWidth, maxHeight,
                minWidth, minHeight,
                tag
            )
            ret.gameController = gameController
            ret.checkChilds(json)
            return ret
        }
    }

    open fun updateSprites() {}

    open fun updateVisuals() {
        PX = parentCanvas.applyCanvasPosX(x, alignX)
        PY = parentCanvas.applyCanvasPosY(y, alignY)

        SX = if (percentWidth != -1f)
            parentCanvas.applyCanvasSizeX(parentCanvas.canvasSizePercentX(percentWidth))
        else
            parentCanvas.applyCanvasSizeX(width)

        SY = if (percentHeight != -1f)
            parentCanvas.applyCanvasSizeY(parentCanvas.canvasSizePercentY(percentHeight))
        else
            parentCanvas.applyCanvasSizeY(height)

        if (maxWidth != 0f && SX > maxWidth) {
            SX = maxWidth
        }
        if (maxHeight != 0f && SY > maxHeight) {
            SY = maxHeight
        }
        if (minWidth != 0f && SX < minWidth) {
            SX = minWidth
        }
        if (minHeight != 0f && SY < minHeight) {
            SY = minHeight
        }

        PX += offsetByWidth * SX
        PY += offsetByHeight * SY

        elementCanvas.posX = PX
        elementCanvas.posY = PY
        elementCanvas.width = SX
        elementCanvas.height = SY
    }

    open fun render(mainRender: MainRender) {
        elementCanvas.renderElements();
    }
}
