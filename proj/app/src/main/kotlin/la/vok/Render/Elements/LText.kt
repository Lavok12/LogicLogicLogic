package la.volk.Render.Elements

import la.vok.Render.MainRender
import la.vok.Storages.Storage
import java.awt.Color
import la.vok.Render.LCanvas
import processing.data.JSONObject
import la.vok.LavokLibrary.Functions
import la.vok.GameController.GameController

class LText(
    x: Float = 0f,
    y: Float = 0f,
    width: Float = 200f,
    height: Float = 100f,
    alignX: Float = 0f,
    alignY: Float = 0f,
    parentCanvas: LCanvas = Storage.gameController.mainRender.mainCanvas,
    var text: String = "Text",
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
) : LElement(x, y, width, height, alignX, 
    alignY, parentCanvas, percentWidth, 
    percentHeight, offsetByWidth, 
    offsetByHeight, maxWidth, maxHeight,
    minWidth, minHeight, tag) {

    private var TPX: Float = 0f
    private var TPY: Float = 0f
    private var textSize: Float = 0f
    
    companion object {
        fun JSONToElement(json: JSONObject, parentCanvas: LCanvas, gameController: GameController): LText {
            val x = if (json.hasKey("x")) json.getFloat("x") else 0f
            val y = if (json.hasKey("y")) json.getFloat("y") else 0f
            val width = if (json.hasKey("width")) json.getFloat("width") else 200f
            val height = if (json.hasKey("height")) json.getFloat("height") else 100f
            val alignX = if (json.hasKey("alignX")) json.getFloat("alignX") else 0f
            val alignY = if (json.hasKey("alignY")) json.getFloat("alignY") else 0f
            val text = if (json.hasKey("text")) json.getString("text") else "Text"
            val textAlignX = if (json.hasKey("textAlignX")) json.getInt("textAlignX") else 0
            val textAlignY = if (json.hasKey("textAlignY")) json.getInt("textAlignY") else 0
            val fontSize = if (json.hasKey("fontSize")) json.getFloat("fontSize") else 30f
            val textColor = Functions.getColorFromJSON(json, "textColor", Color(255, 255, 255))
            val textDeltaX = if (json.hasKey("textDeltaX")) json.getFloat("textDeltaX") else 0f
            val textDeltaY = if (json.hasKey("textDeltaY")) json.getFloat("textDeltaY") else 0f
            val textPosAlignX = if (json.hasKey("textPosAlignX")) json.getFloat("textPosAlignX") else 0f
            val textPosAlignY = if (json.hasKey("textPosAlignY")) json.getFloat("textPosAlignY") else 0f
            val offsetByWidth = if (json.hasKey("offsetByWidth")) json.getFloat("offsetByWidth") else 0f
            val offsetByHeight = if (json.hasKey("offsetByHeight")) json.getFloat("offsetByHeight") else 0f
            val percentWidth = if (json.hasKey("percentWidth")) json.getFloat("percentWidth") else -1f
            val percentHeight = if (json.hasKey("percentHeight")) json.getFloat("percentHeight") else -1f
            val maxWidth = if (json.hasKey("maxWidth")) json.getFloat("maxWidth") else 0f
            val maxHeight = if (json.hasKey("maxHeight")) json.getFloat("maxHeight") else 0f
            val minWidth = if (json.hasKey("minWidth")) json.getFloat("minWidth") else 0f
            val minHeight = if (json.hasKey("minHeight")) json.getFloat("minHeight") else 0f
            val tag = if (json.hasKey("tag")) json.getString("tag") else ""

            var ret = LText(
                x, y, width, height, alignX, alignY, parentCanvas,
                text, textAlignX, textAlignY, fontSize,
                textColor, textDeltaX, textDeltaY, textPosAlignX, textPosAlignY,
                offsetByWidth, offsetByHeight, percentWidth, percentHeight,
                maxWidth, maxHeight, minWidth, minHeight, tag
            )
            ret.gameController = gameController;
            ret.checkChilds(json);
            return ret;
        }
    }
    override fun updateVisuals() {
        super.updateVisuals()
        TPX = PX + textDeltaX * parentCanvas.scaleX + SX * textPosAlignX / 2
        TPY = PY + textDeltaY * parentCanvas.scaleY + SY * textPosAlignY / 2
        textSize = parentCanvas.applyCanvasTextSize(fontSize * parentCanvas.textScale)
    }

    override fun render(mainRender: MainRender) {
        updateVisuals()
        val lg = mainRender.lg
        lg.setTextAlign(textAlignX, textAlignY)
        lg.pg.fill(textColor.red.toFloat(), textColor.green.toFloat(), textColor.blue.toFloat(), textColor.alpha.toFloat())
        lg.setText(text, TPX, TPY, textSize)

        super.render(mainRender);
    }
}
