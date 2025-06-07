package la.vok.UI.Elements

import la.vok.UI.MainRender
import la.vok.Storages.Storage
import java.awt.Color
import la.vok.UI.LCanvas
import processing.data.JSONObject
import la.vok.LavokLibrary.*
import la.vok.GameController.GameController
import la.vok.UI.*


class LTextField(
    gameController: GameController,
    x: Float = 0f,
    y: Float = 0f,
    width: Float = 200f,
    height: Float = 100f,
    alignX: Float = 0f,
    alignY: Float = 0f,
    parentCanvas: LCanvas = Storage.gameController.getCanvas(),
    var text: String = "Text",
    var inputString: String = "Text",
    var textTranslate: Boolean = true,
    var textAlignX: Int = 0,
    var textAlignY: Int = 0,
    var fontSize: Float = 30f,
    var textColor: Color = Color(180, 180, 180),
    var inputTextColor: Color = Color(255, 255, 255),
    var textDeltaX: Float = 0f,
    var textDeltaY: Float = 0f,
    var textPosAlignX: Float = 0f,
    var textPosAlignY: Float = 0f,
    var panelColor: Color = Color(100, 100, 100, 255),
    var borderRadius: Float = 0f,
    tag: String = ""
) : LElement(
    gameController, x, y, width, height, alignX, alignY, parentCanvas,
    -1f, -1f, 0f, 0f, 0f, 0f, 0f, 0f, tag
) {
    private var TPX: Float = 0f
    private var TPY: Float = 0f
    private var textSize: Float = 0f

    companion object {
        fun JSONToElement(json: JSONObject, parentCanvas: LCanvas, gameController: GameController): LTextField {
            var ret = LTextField(
                gameController = gameController,
                x = json.LgetFloat("x", 0f),
                y = json.LgetFloat("y", 0f),
                width = json.LgetFloat("width", 200f),
                height = json.LgetFloat("height", 100f),
                alignX = json.LgetFloat("alignX", 0f),
                alignY = json.LgetFloat("alignY", 0f),
                parentCanvas = parentCanvas,
                text = json.LgetString("text", "Text"),
                textTranslate = json.LgetBoolean("textTranslate", true),
                textAlignX = json.LgetInt("textAlignX", 0),
                textAlignY = json.LgetInt("textAlignY", 0),
                fontSize = json.LgetFloat("fontSize", 30f),
                textColor = Functions.getColorFromJSON(json, "textColor", Color(180, 180, 180)),
                inputTextColor = Functions.getColorFromJSON(json, "textColor", Color(255, 255, 255)),
                textDeltaX = json.LgetFloat("textDeltaX", 0f),
                textDeltaY = json.LgetFloat("textDeltaY", 0f),
                textPosAlignX = json.LgetFloat("textPosAlignX", 0f),
                textPosAlignY = json.LgetFloat("textPosAlignY", 0f),
                panelColor = Functions.getColorFromJSON(json, "panelColor", Color(100, 100, 100, 255)),
                borderRadius = json.LgetFloat("borderRadius", 0f),
                tag = json.LgetString("tag", "")
            )
            ret.hasHitbox = false
            ret.gameController = gameController
            ret.checkChilds(json)
            ret.setEvents(json)
            return ret
        }
    }

    override fun updateVisuals() {
        super.updateVisuals()
        TPX = PX + textDeltaX * parentCanvas.scaleX + SX * textPosAlignX / 2
        TPY = PY + textDeltaY * parentCanvas.scaleY + SY * textPosAlignY / 2
        textSize = parentCanvas.applyCanvasTextSize(fontSize * parentCanvas.textScale)
    }

    override fun renderElement(mainRender: MainRender) {
        updateVisuals()
        val lg = mainRender.lg

        // Рендер панели
        RenderElements.renderBlock(
            posX = PX,
            posY = PY,
            width = SX,
            height = SY,
            borderRadius = borderRadius,
            mainRender = mainRender,
            clr = panelColor
        )

        // Рендер текста
        lg.setTextAlign(textAlignX, textAlignY)
        lg.pg.fill(textColor.red.toFloat(), textColor.green.toFloat(), textColor.blue.toFloat(), textColor.alpha.toFloat())
        lg.setText(
            if (inputString.length == 0) {
                if (textTranslate) {gameController.languageController.getText(text)} else {text}
            } else {
                inputString
            }, 
            TPX,
            TPY,
            textSize
        )
    }
}
