package la.vok.UI.Elements

import la.vok.UI.MainRender
import la.vok.Storages.Storage
import java.awt.Color
import la.vok.UI.Canvas.*
import la.vok.UI.Scenes.*
import processing.data.JSONObject
import la.vok.LavokLibrary.*
import la.vok.GameController.GameController
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.UI.Canvas.*
import la.vok.UI.Scenes.*

class LText(
    gameController: GameController,
    pos: Vec2 = Vec2(0f),
    wh: Vec2 = Vec2(200f, 100f),
    align: Vec2 = Vec2(0f),
    parentCanvas: LCanvas = gameController.getCanvas(),
    var text: String = "Text",
    var textTranslate: Boolean = true,
    var textAlign: Vec2 = Vec2(0f),
    var fontSize: Float = 30f,
    var textColor: Color = Color(255, 255, 255),
    var textDelta: Vec2 = Vec2(0f),
    var textPosAlign: Vec2 = Vec2(0f),
    var scale: Vec2 = Vec2(0f),
    offsetByWH: Vec2 = Vec2(0f),
    percentWidth: Float = -1f,
    percentHeight: Float = -1f,
    tag: String = ""
) : LElement(
    gameController, pos, wh, align, parentCanvas,
    percentWidth, percentHeight, offsetByWH, tag
) {

    var textVisualPos: Vec2 = Vec2(0f)
    var textSize: Float = 0f

    companion object {
        fun JSONToElement(json: JSONObject, parentCanvas: LCanvas, gameController: GameController): LText {
            val pos = json.getVec2("pos", Vec2(0f))
            val wh = json.getVec2("wh", Vec2(200f, 100f))
            val align = json.getVec2("align", Vec2(0f))
            val text = json.LgetString("text", "Text")
            val textTranslate = json.LgetBoolean("textTranslate", true)
            val textAlign = json.getVec2("textAlign", Vec2(0f))
            val fontSize = json.LgetFloat("fontSize", 30f)
            val textColor = Functions.getColorFromJSON(json, "textColor", Color(255, 255, 255))
            val textDelta = json.getVec2("textDelta", Vec2(0f))
            val textPosAlign = json.getVec2("textPosAlign", Vec2(0f))
            val scale = json.getVec2("scale", Vec2(0f))
            val offsetByWH = json.getVec2("offsetByWH", Vec2(0f))
            val percentWidth = json.LgetFloat("percentWidth", -1f)
            val percentHeight = json.LgetFloat("percentHeight", -1f)
            val tag = json.LgetString("tag", "")

            val ret = LText(
                gameController, pos, wh, align, parentCanvas,
                text, textTranslate, textAlign, fontSize,
                textColor, textDelta, textPosAlign, scale,
                offsetByWH, percentWidth, percentHeight, tag
            )

            ret.hasHitbox = false
            ret.gameController = gameController
            ret.checkChilds(json)
            ret.setEvents(json)
            return ret
        }
    }

    fun standartVisuals() {
        textVisualPos = visualPos + textDelta * parentCanvas.scale  * parentCanvas.localScale + visualSize * textPosAlign / 2f
        textSize = parentCanvas.applyCanvasTextSize(fontSize * parentCanvas.textScale)
    }

    override fun updateGridVisuals(vec2: Vec2) {
        super.updateGridVisuals(vec2)
        standartVisuals()
    }

    override fun updateVisuals() {
        super.updateVisuals()
        standartVisuals()
    }

    override fun renderElement(mainRender: MainRender) {
        val lg = mainRender.lg
        lg.setTextAlign(Math.round(textAlign.x), Math.round(textAlign.y))
        lg.pg.fill(
            textColor.red.toFloat(),
            textColor.green.toFloat(),
            textColor.blue.toFloat(),
            textColor.alpha.toFloat()
        )
        lg.setText(
            if (textTranslate) gameController.loaders.language.getText(text) else text,
            textVisualPos,
            textSize
        )
    }
}

