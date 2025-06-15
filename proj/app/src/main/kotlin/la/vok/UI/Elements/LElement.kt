package la.vok.UI.Elements

import la.vok.UI.*
import la.vok.UI.Canvas.*
import la.vok.UI.Scenes.*
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
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.UI.Canvas.*
import la.vok.UI.Scenes.*

open class LElement(
    var gameController: GameController,
    var pos: Vec2 = Vec2(0f),
    var wh: Vec2 = Vec2(0f),
    var align: Vec2 = Vec2(0f),
    var parentCanvas: LCanvas = gameController.getCanvas(),
    var percentWidth: Float = -1f,
    var percentHeight: Float = -1f,
    var offsetByWH: Vec2 = Vec2(0f),
    var tag: String = "",
) {
    open var visualPos = Vec2(0f)
    open var visualSize = Vec2(0f)

    open var elementCanvas: LCanvas = LCanvas(Vec2(0f), Vec2(0f), Vec2(1f), Vec2(1f), 1f, -1, true, gameController)
    open var isActive: Boolean = true

    open var createEvent: Boolean = false
    open var updateEvent: Boolean = false
    open var activateEvent: Boolean = false
    open var deactivateEvent: Boolean = false
    open var onMouseDown: Boolean = false
    open var onMouseUp: Boolean = false
    open var onMouseMove: Boolean = false
    open var onMouseEnter: Boolean = false
    open var onMouseExit: Boolean = false
    open var onMouseOver: Boolean = false
    open var onMouseHold: Boolean = false
    open var hasHitbox: Boolean = true

    var createScript: String = ""
    var updateScript: String = ""
    var activateScript: String = ""
    var deactivateScript: String = ""
    var onMouseDownScript: String = ""
    var onMouseUpScript: String = ""
    var onMouseMoveScript: String = ""
    var onMouseEnterScript: String = ""
    var onMouseExitScript: String = ""
    var onMouseOverScript: String = ""
    var onMouseHoldScript: String = ""

    var layoutType: LayoutType = LayoutType.NONE // Enum для типа расположения (NONE, LINE, GRID)
    var layoutDirection: LayoutDirection = LayoutDirection.HORIZONTAL // Enum для направления (только для LINE)
    var spacing: Vec2 = Vec2(0f) // Отступ между элементами
    var columns: Int = 1 // Количество колонок для GRID
    var rows: Int = 1 // Количество строк для GRID (может быть 0, если число колонок фиксировано)
    var contentDelta: Vec2 = Vec2(0f)
    var reverseX: Boolean = false
    var reverseY: Boolean = false
    var alignCenterX: Boolean = false
    var alignCenterY: Boolean = false

    open fun destroy() {
        elementCanvas.destroy()
    }

    init {
        handleCreate()
    }

    open fun tick() {
        if (!isActive) return
        handleUpdate()
        otherTick()
        elementCanvas.tick()
    }

    open fun otherTick() {
    }

    open fun handleDeactivate() {
        if (deactivateEvent) {
            deactivateEvent()
        }
    }

    open fun handleActivate() {
        if (activateEvent) {
            activateEvent()
        }
    }
    open fun handleUpdate() {
        if (updateEvent) {
            updateEvent()
        }
    }

    open fun handleCreate() {
        if (createEvent) {
            createEvent()
        }
    }


    open fun handleMouseEnter(mouseController: MouseController) {
        if (onMouseEnter) {
            onMouseEnter(mouseController)
        }
    }

    open fun handleMouseExit(mouseController: MouseController) {
        if (onMouseExit) {
            onMouseExit(mouseController)
        }
    }

    open fun handleMouseDown(mouseController: MouseController) {
        if (onMouseDown) {
            onMouseDown(mouseController)
        }
    }

    open fun handleMouseUp(mouseController: MouseController) {
        if (onMouseUp) {
            onMouseUp(mouseController)
        }
    }

    open fun handleMouseMove(mouseController: MouseController) {
        if (onMouseMove) {
            onMouseMove(mouseController)
        }
    }

    open fun handleMouseOver(mouseController: MouseController) {
        if (onMouseOver) {
            onMouseOver(mouseController)
        }
    }

    open fun handleMouseHold(mouseController: MouseController) {
        if (onMouseHold) {
            onMouseHold(mouseController)
        }
    }

    open fun deactivate() {
        handleDeactivate()
    }

    open fun activate() {
        handleActivate()
    }

    open fun updateEvent() {}
    open fun createEvent() {}
    open fun activateEvent() {}
    open fun deactivateEvent() {}
    open fun onMouseDown(mouseController: MouseController) {
        if (onMouseDownScript != "") {
            gameController.ktsScriptManager.executeScript(onMouseDownScript, "default")
        }
    }
    open fun onMouseUp(mouseController: MouseController) {}
    open fun onMouseMove(mouseController: MouseController) {}
    open fun onMouseEnter(mouseController: MouseController) {}
    open fun onMouseExit(mouseController: MouseController) {}
    open fun onMouseOver(mouseController: MouseController) {}
    open fun onMouseHold(mouseController: MouseController) {}

    open fun inside(mx: Float, my: Float): Boolean {
        if (!isActive) {
            return false
        }
        if (!hasHitbox) {
            return false
        }
        if (!Functions.tap(visualPos, visualSize, mx, my)) {
            return false;
        }
        return true;
    }

    open fun setEvents(objs: JSONObject) {
        if (objs.hasKey("onMouseDown")) {
            val localJson = objs.LgetJSONObject("onMouseDown")
            onMouseDown = localJson.LgetBoolean("isActive", false)
            onMouseDownScript = localJson.LgetString("ktscript", "")
        }
        if (objs.hasKey("onMouseUp")) {
            val localJson = objs.LgetJSONObject("onMouseUp")
            onMouseUp = localJson.LgetBoolean("isActive", false)
            onMouseUpScript = localJson.LgetString("ktscript", "")
        }
        if (objs.hasKey("onMouseMove")) {
            val localJson = objs.LgetJSONObject("onMouseMove")
            onMouseMove = localJson.LgetBoolean("isActive", false)
            onMouseMoveScript = localJson.LgetString("ktscript", "")
        }
        if (objs.hasKey("onMouseEnter")) {
            val localJson = objs.LgetJSONObject("onMouseEnter")
            onMouseEnter = localJson.LgetBoolean("isActive", false)
            onMouseEnterScript = localJson.LgetString("ktscript", "")
        }
        if (objs.hasKey("onMouseExit")) {
            val localJson = objs.LgetJSONObject("onMouseExit")
            onMouseExit = localJson.LgetBoolean("isActive", false)
            onMouseExitScript = localJson.LgetString("ktscript", "")
        }
        if (objs.hasKey("onMouseOver")) {
            val localJson = objs.LgetJSONObject("onMouseOver")
            onMouseOver = localJson.LgetBoolean("isActive", false)
            onMouseOverScript = localJson.LgetString("ktscript", "")
        }
        if (objs.hasKey("onMouseHold")) {
            val localJson = objs.LgetJSONObject("onMouseHold")
            onMouseHold = localJson.LgetBoolean("isActive", false)
            onMouseHoldScript = localJson.LgetString("ktscript", "")
        }
        if (objs.hasKey("updateEvent")) {
            val localJson = objs.LgetJSONObject("updateEvent")
            updateEvent = localJson.LgetBoolean("isActive", false)
            updateScript = localJson.LgetString("ktscript", "")
        }
        if (objs.hasKey("createEvent")) {
            val localJson = objs.LgetJSONObject("createEvent")
            createEvent = localJson.LgetBoolean("isActive", false)
            createScript = localJson.LgetString("ktscript", "")
        }
        setLayoutProperties(objs)
    }

    open fun setLayoutProperties(json: JSONObject) {
        if (json.hasKey("layoutType")) {
            var l = json.LgetString("layoutType", "")
            when (l) {
                "LINE" -> layoutType = LayoutType.LINE
                "GRID" -> layoutType = LayoutType.GRID
                else -> layoutType = LayoutType.NONE
            }
        }
        if (json.hasKey("layoutDirection")) {
            layoutDirection = LayoutDirection.valueOf(json.LgetString("layoutDirection", "").uppercase())
        }
        spacing = json.getVec2("spacing", spacing)
        columns = json.LgetInt("columns", columns)
        rows = json.LgetInt("rows", rows)
        contentDelta = json.getVec2("contentDelta", contentDelta)
        reverseX = json.LgetBoolean("reverseX", false)
        reverseY = json.LgetBoolean("reverseY", false)
        alignCenterX = json.LgetBoolean("alignCenterX", false)
        alignCenterY = json.LgetBoolean("alignCenterY", false)
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
            val pos = json.getVec2("pos", Vec2(0f))
            val wh = json.getVec2("wh", Vec2(0f))
            val align = json.getVec2("align", Vec2(0f))
            val percentWidth = json.LgetFloat("percentWidth", -1f)
            val percentHeight = json.LgetFloat("percentHeight", -1f)
            val offsetByWH = json.getVec2("offsetByWH", Vec2(0f))
            val tag = json.LgetString("tag", "")


            var ret = LElement(
                gameController, pos, wh, align, parentCanvas, percentWidth, percentHeight, offsetByWH, tag
            )
            ret.gameController = gameController
            ret.checkChilds(json)
            return ret
        }
    }

    open fun updateSprites() {}


    open fun updateGridVisuals(newPos: Vec2) {
        visualPos = newPos

        visualSize.x =
            if (percentWidth != -1f) parentCanvas.applyCanvasSizeX(parentCanvas.canvasSizePercentX(percentWidth))
            else parentCanvas.applyCanvasSizeX(wh.x)
        visualSize.y =
            if (percentHeight != -1f) parentCanvas.applyCanvasSizeY(parentCanvas.canvasSizePercentY(percentHeight))
            else parentCanvas.applyCanvasSizeY(wh.y)

    }

    open fun updateVisuals() {
        visualPos = parentCanvas.applyCanvasPos(pos, align)

        visualSize.x =
            if (percentWidth != -1f) parentCanvas.applyCanvasSizeX(parentCanvas.canvasSizePercentX(percentWidth))
            else parentCanvas.applyCanvasSizeX(wh.x)
        visualSize.y =
            if (percentHeight != -1f) parentCanvas.applyCanvasSizeY(parentCanvas.canvasSizePercentY(percentHeight))
            else parentCanvas.applyCanvasSizeY(wh.y)


        visualPos = visualPos + offsetByWH * visualSize

        elementCanvas.pos = visualPos
        elementCanvas.wh = visualSize
    }

    open fun renderElement(mainRender: MainRender) {

    }

    open fun render(mainRender: MainRender) {
        if (isActive) {
            renderElement(mainRender)
            when (layoutType) {
                LayoutType.NONE -> elementCanvas.renderElements()
                LayoutType.LINE -> elementCanvas.renderElementsLine(this)
                LayoutType.GRID -> elementCanvas.renderElementsGrid(this)
            }
        }
    }

    open fun getElementType(): String {
        return this::class.simpleName ?: "LElement"
    }

    fun debugRender(mainRender: MainRender) {
        elementCanvas.debugRender(mainRender)
        mainRender.lg.fill(255f, 0f)
        mainRender.lg.pg.stroke(255f, 0f, 0f)
        mainRender.lg.pg.strokeWeight(4f)
        mainRender.lg.setBlock(visualPos, visualSize)
        mainRender.lg.pg.noStroke()
        mainRender.lg.fill(255f, 0f, 0f, 255f)
        mainRender.lg.setTextAlign(-1, -1)
        mainRender.lg.setText(
            "visualPos ${visualPos.x}, ${visualPos.y}\nvisualSize ${visualSize.x}, ${visualSize.y}\nwh ${wh.x}, ${wh.y}\nParentScale ${parentCanvas.scale}, ${parentCanvas.localScale}\npwh ${percentWidth}, ${percentHeight}",
            visualPos - Vec2(visualSize.x, -visualSize.y) / 2f + Vec2(10f, 185f),
            25f
        )

    }
}
