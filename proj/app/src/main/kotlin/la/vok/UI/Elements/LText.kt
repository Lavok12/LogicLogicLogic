package la.vok.UI.Elements

import la.vok.UI.MainRender
import la.vok.Storages.Storage
import java.awt.Color
import la.vok.UI.Canvas.*
import la.vok.UI.Scenes.*
import processing.data.JSONObject
import la.vok.LavokLibrary.*
import la.vok.GameController.GameController
import la.vok.UI.Canvas.*
import la.vok.UI.Scenes.*

class LText(
    gameController: GameController,
    x: Float = 0f,
    y: Float = 0f,
    width: Float = 200f,
    height: Float = 100f,
    alignX: Float = 0f,
    alignY: Float = 0f,
    parentCanvas: LCanvas = Storage.gameController.getCanvas(),
    var text: String = "Text",
    var textTranslate: Boolean = true,
    var textAlignX: Int = 0,
    var textAlignY: Int = 0,
    var fontSize: Float = 30f,
    var textColor: Color = Color(255, 255, 255),
    var textDeltaX: Float = 0f,
    var textDeltaY: Float = 0f,
    var textPosAlignX: Float = 0f,
    var textPosAlignY: Float = 0f,
    offsetByWidth: Float = 0f,
    offsetByHeight: Float = 0f,
    percentWidth: Float = -1f,
    percentHeight: Float = -1f,
    maxWidth: Float = 0f,
    maxHeight: Float = 0f,
    minWidth: Float = 0f,
    minHeight: Float = 0f,
    tag: String = "" // Tag for the button
) : LElement(gameController, x, y, width, height, alignX, 
    alignY, parentCanvas, percentWidth, 
    percentHeight, offsetByWidth, 
    offsetByHeight, maxWidth, maxHeight,
    minWidth, minHeight, tag) {

    private var TPX: Float = 0f
    private var TPY: Float = 0f
    private var textSize: Float = 0f
    
    companion object {
        fun JSONToElement(json: JSONObject, parentCanvas: LCanvas, gameController: GameController): LText {
            val x = json.LgetFloat("x", 0f)
            val y = json.LgetFloat("y", 0f)
            val width = json.LgetFloat("width", 200f)
            val height = json.LgetFloat("height", 100f)
            val alignX = json.LgetFloat("alignX", 0f)
            val alignY = json.LgetFloat("alignY", 0f)
            val text = json.LgetString("text", "Text")
            val textTranslate = json.LgetBoolean("textTranslate", true)

            val textAlignX = json.LgetInt("textAlignX", 0)
            val textAlignY = json.LgetInt("textAlignY", 0)
            val fontSize = json.LgetFloat("fontSize", 30f)
            val textColor = Functions.getColorFromJSON(json, "textColor", Color(255, 255, 255))
            val textDeltaX = json.LgetFloat("textDeltaX", 0f)
            val textDeltaY = json.LgetFloat("textDeltaY", 0f)
            val textPosAlignX = json.LgetFloat("textPosAlignX", 0f)
            val textPosAlignY = json.LgetFloat("textPosAlignY", 0f)
            val offsetByWidth = json.LgetFloat("offsetByWidth", 0f)
            val offsetByHeight = json.LgetFloat("offsetByHeight", 0f)
            val percentWidth = json.LgetFloat("percentWidth", -1f)
            val percentHeight = json.LgetFloat("percentHeight", -1f)
            val maxWidth = json.LgetFloat("maxWidth", 0f)
            val maxHeight = json.LgetFloat("maxHeight", 0f)
            val minWidth = json.LgetFloat("minWidth", 0f)
            val minHeight = json.LgetFloat("minHeight", 0f)
            val tag = json.LgetString("tag", "")
            

            var ret = LText(
                gameController, x, y, width, height, alignX, alignY, parentCanvas,
                text, textTranslate, textAlignX, textAlignY, fontSize,
                textColor, textDeltaX, textDeltaY, textPosAlignX, textPosAlignY,
                offsetByWidth, offsetByHeight, percentWidth, percentHeight,
                maxWidth, maxHeight, minWidth, minHeight, tag
            )
            ret.hasHitbox = false
            ret.gameController = gameController;
            ret.checkChilds(json);
            ret.setEvents(json)
            return ret;
        }
    }
    fun standartVisuals() {
        TPX = PX + textDeltaX * parentCanvas.scale.x + SX * textPosAlignX / 2
        TPY = PY + textDeltaY * parentCanvas.scale.y + SY * textPosAlignY / 2
        textSize = parentCanvas.applyCanvasTextSize(fontSize * parentCanvas.textScale)
    }

    override fun updateGridVisuals(nx: Float, ny: Float) {
        super.updateGridVisuals(nx, ny)
        standartVisuals()
    }
    override fun updateVisuals() {
        super.updateVisuals()
        standartVisuals()
    }

    override fun renderElement(mainRender: MainRender) {
        val lg = mainRender.lg
        lg.setTextAlign(textAlignX, textAlignY)
        lg.pg.fill(textColor.red.toFloat(), textColor.green.toFloat(), textColor.blue.toFloat(), textColor.alpha.toFloat())
        lg.setText(if (textTranslate) {gameController.languageController.getText(text)} else {text}, TPX, TPY, textSize)
    }
}
