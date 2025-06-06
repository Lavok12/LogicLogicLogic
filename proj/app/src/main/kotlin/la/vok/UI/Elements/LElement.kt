package la.vok.UI.Elements

import la.vok.UI.*
import la.vok.UI.LCanvas
import la.vok.Storages.*
import la.vok.LoadData.*
import la.vok.GameController.GameController
import org.luaj.vm2.*
import org.luaj.vm2.lib.jse.JsePlatform
import processing.core.PApplet
import processing.data.JSONObject
import processing.data.JSONArray
import la.vok.LavokLibrary.*
import la.vok.InputController.MouseController

open class LElement(
    var gameController: GameController,
    var x: Float = 0f,
    var y: Float = 0f,
    var width: Float = 200f,
    var height: Float = 100f,
    var alignX: Float = 0f,
    var alignY: Float = 0f,
    var parentCanvas: LCanvas = gameController.getCanvas(),
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
    open var elementCanvas: LCanvas = LCanvas(0f, 0f, 0f, 0f, 1f, 1f, 1f, 1f, 1f, -1, gameController)
    open var isActive: Boolean = true

    open var update : Boolean = false
    open var onMouseDown : Boolean = false
    open var onMouseUp : Boolean = false
    open var onMouseMove : Boolean = false
    open var onMouseEnter : Boolean = false
    open var onMouseExit : Boolean = false
    open var onMouseOver : Boolean = false
    open var onMouseHold : Boolean = false
    open var hasHitbox : Boolean = true
    
    open var mouseEnter = false
    open var mouseHold = false
    open var mouseDownLastFrame = false
    open var inside = false
    open var childHandled = false

    open var oldMouseX: Float = 0f
    open var oldMouseY: Float = 0f

    open fun tick(mx: Float, my: Float, gameController: GameController): Boolean {
        if (!isActive) return false
        inside = inElement(mx, my)
        mouseDownLastFrame = mouseHold != (gameController.mouseController.isButtonPressed(MouseController.LEFT))
        mouseHold = gameController.mouseController.isButtonPressed(MouseController.LEFT)

        
        childHandled = elementCanvas.tick(mx, my, gameController)

        handleMouseExit(mx, my, gameController)
        handleMouseEnter(mx, my, gameController)
        handleUpdate(mx, my, gameController)
        handleMouseDown(mx, my, gameController)
        handleMouseUp(mx, my, gameController)
        handleMouseMove(mx, my, gameController)
        handleMouseOver(mx, my, gameController)
        handleMouseHold(mx, my, gameController)

        oldMouseX = mx
        oldMouseY = my

        return if (hasHitbox && inside) true else false
    }

    private fun handleMouseEnter(mx: Float, my: Float, gameController: GameController): Boolean {
        if (inside && !mouseEnter) {
            mouseEnter = true
            if (onMouseEnter) {
                onMouseEnter(mx, my)
            }
            return true
        }
        return false
    }

    private fun handleMouseExit(mx: Float, my: Float, gameController: GameController): Boolean {
        if ((childHandled || !inside) && mouseEnter) {
            mouseEnter = false
            if (onMouseExit) {
                onMouseExit(mx, my)
            }
            return true
        }
        return false
    }
    
    private fun handleUpdate(mx: Float, my: Float, gameController: GameController): Boolean {
        if (update) {
            update(mx, my)
        }
        return true
    }

    private fun handleMouseDown(mx: Float, my: Float, gameController: GameController): Boolean {
        if (mouseDownLastFrame && inside) {
            mouseHold = true
            if (onMouseDown) {
                onMouseDown(mx, my)
            }
            return true
        }
        return false
    }

    private fun handleMouseUp(mx: Float, my: Float, gameController: GameController): Boolean {
        if (mouseHold && gameController.mouseController.isButtonPressed(MouseController.LEFT)) {
            mouseHold = false
            if (onMouseUp) {
                onMouseUp(mx, my)
            }
            return true
        }
        return false
    }

    private fun handleMouseMove(mx: Float, my: Float, gameController: GameController): Boolean {
        if ((oldMouseX != mx || oldMouseY != my) && inside) {
            if (onMouseMove) {
                onMouseMove(mx, my)
            }
            return true
        }
        return false
    }

    private fun handleMouseOver(mx: Float, my: Float, gameController: GameController): Boolean {
        if (inside) {
            if (onMouseOver) {
                onMouseOver(mx, my)
            }
            return true
        }
        return false
    }

    private fun handleMouseHold(mx: Float, my: Float, gameController: GameController): Boolean {
        if (mouseHold) {
            if (onMouseHold) {
                onMouseHold(mx, my)
            }
            return true
        }
        return false
    }
    
    open fun deactivate() {
    }
    open fun update(mx: Float, my: Float) {}
    open fun onMouseDown(mx: Float, my: Float) {
        if (!gameController.gameStarted) {
            gameController.startGame(Settings.isClient, Settings.isServer, Settings.isLocal)
        }
    }
    open fun onMouseUp(mx: Float, my: Float) {}
    open fun onMouseMove(mx: Float, my: Float) {}
    open fun onMouseEnter(mx: Float, my: Float) {}
    open fun onMouseExit(mx: Float, my: Float) {}
    open fun onMouseOver(mx: Float, my: Float) {}
    open fun onMouseHold(mx: Float, my: Float) {}

    open fun inElement(mx: Float, my: Float): Boolean {
        if (!Functions.tap(PX, PY, SX, SY, mx, my)) {
            return false;
        }
        return true;
    }

    open fun setEvents(objs: JSONObject) {
        if (objs.hasKey("onMouseDown")) {
            var localJson = objs.LgetJSONObject("onMouseDown")
            onMouseDown = localJson.LgetBoolean("isActive", false)
        }
        if (objs.hasKey("onMouseUp")) {
            var localJson = objs.LgetJSONObject("onMouseUp")
            onMouseUp = localJson.LgetBoolean("isActive", false)
        }
        if (objs.hasKey("onMouseMove")) {
            var localJson = objs.LgetJSONObject("onMouseMove")
            onMouseMove = localJson.LgetBoolean("isActive", false)
        }
        if (objs.hasKey("onMouseEnter")) {
            var localJson = objs.LgetJSONObject("onMouseEnter")
            onMouseEnter = localJson.LgetBoolean("isActive", false)
        }
        if (objs.hasKey("onMouseExit")) {
            var localJson = objs.LgetJSONObject("onMouseExit")
            onMouseExit = localJson.LgetBoolean("isActive", false)
        }
        if (objs.hasKey("onMouseOver")) {
            var localJson = objs.LgetJSONObject("onMouseOver")
            onMouseOver = localJson.LgetBoolean("isActive", false)
        }
        if (objs.hasKey("onMouseHold")) {
            var localJson = objs.LgetJSONObject("onMouseHold")
            onMouseHold = localJson.LgetBoolean("isActive", false)
        }
        if (objs.hasKey("update")) {
            var localJson = objs.LgetJSONObject("update")
            update = localJson.LgetBoolean("isActive", false)
        }
        if (objs.hasKey("hasHitbox")) {
            hasHitbox = objs.LgetBoolean("hitbox", true)
        }
    } 

    open fun checkChilds(json: JSONObject) {
        if (json.hasKey("childs")) {
            val childs = json.getJSONArray("childs")
            for (i in 0 until childs.size()) {
                val raw = childs.get(i).toString().trim()
                if (raw.startsWith("{")) {
                    val child = JSONObject.parse(raw)
                    gameController.loadUIList.addChilds(child, elementCanvas)
                } else {
                    gameController.loadUIList.addChilds(raw, elementCanvas)
                }
            }
        }
        

        if (json.hasKey("whileChilds")) {
            var wc = json.LgetJSONObject("whileChilds")
            var iterations = if (wc.hasKey("iterations")) wc.LgetInt("iterations") else 1
            var varName = if (wc.hasKey("varName")) wc.LgetString("varName") else "&i"

            iterations -= 1

            for (i in 0..iterations) {
                var iwcs = wc.toString()
                iwcs = LuaEvaluator.replacePlaceholders(iwcs, i, varName) { code ->
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
            val x = json.LgetFloat("x", 0f)
            val y = json.LgetFloat("y", 0f)
            val width = json.LgetFloat("width", 0f)
            val height = json.LgetFloat("height", 0f)
            val alignX = json.LgetFloat("alignX", 0f)
            val alignY = json.LgetFloat("alignY", 0f)
            val percentWidth = json.LgetFloat("percentWidth", 0f)
            val percentHeight = json.LgetFloat("percentHeight", 0f)
            val offsetByWidth = json.LgetFloat("offsetByWidth", 0f)
            val offsetByHeight = json.LgetFloat("offsetByHeight", 0f)
            val maxWidth = json.LgetFloat("maxWidth", Float.MAX_VALUE)
            val maxHeight = json.LgetFloat("maxHeight", Float.MAX_VALUE)
            val minWidth = json.LgetFloat("minWidth", 0f)
            val minHeight = json.LgetFloat("minHeight", 0f)
            val tag = json.LgetString("tag", "")


            var ret = LElement(
                gameController, x, y, width, height, alignX, alignY, parentCanvas,
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
        elementCanvas.scaleX = parentCanvas.scaleX
        elementCanvas.scaleY = parentCanvas.scaleY
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
    open fun renderElement(mainRender: MainRender) {
        
    }
    open fun render(mainRender: MainRender) {
        if (isActive) {
            renderElement(mainRender);
            elementCanvas.renderElements();
        }
    }

    open fun getElementType(): String {
        return this::class.simpleName ?: "LElement"
    }
}
