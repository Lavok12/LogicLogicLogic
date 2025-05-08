package la.volk.Render.Elements

import la.vok.Render.MainRender
import la.vok.Render.RenderElements.RenderElements
import la.vok.Storages.Storage
import la.vok.LoadData.LSprite
import java.awt.Color
import processing.core.PApplet
import la.vok.Render.LCanvas

class LButton(
    x: Float = 0f,
    y: Float = 0f,
    width: Float = 200f,
    height: Float = 100f,
    alignX: Float = 0f,
    alignY: Float = 0f,
    parentCanvas: LCanvas = Storage.gameController.clientController.mainRender.mainCanvas,
    var text: String = "Button",
    var textAlignX: Int = 0,
    var textAlignY: Int = 0,
    var fontSize: Float = 30f,
    var buttonRadius: Float = 10f,
    var buttonColor: Color = Color(150, 120, 100),
    var hoverColor: Color? = Color(140, 110, 90, 200),
    var textColor: Color = Color(255, 255, 255),
    var hoverTextColor: Color? = null,
    var imageKey: String = "",
    var hoverImageKey: String = "",
    var textDeltaX: Float = 0f,
    var textDeltaY: Float = 0f,
    var textPosAlignX: Float = 0f,
    var textPosAlignY: Float = 0f,

    offsetByWidth: Float = 0f,
    offsetByHeight: Float = 0f,
    percentWidth: Float = -1f,
    percentHeight: Float = -1f,

) : LElement(x, y, width, height, alignX, alignY, parentCanvas, percentWidth, percentHeight, offsetByWidth, offsetByHeight) {
    var buttonSprite: LSprite? = null
    var hoverButtonSprite: LSprite? = null
    var isHover: Boolean = false

    var TPX: Float = 0f
    var TPY: Float = 0f
    var textSize: Float = 0f

    init {
        if (imageKey != "") {
            buttonSprite = Storage.gameController.spriteLoader.getSprite(imageKey)
        }
        if (hoverImageKey != "") {
            hoverButtonSprite = Storage.gameController.spriteLoader.getSprite(hoverImageKey)
        }
    }

    override fun updateVisuals() {
        super.updateVisuals()

        TPX = PX + textDeltaX * parentCanvas.scaleX + SX * textPosAlignX/2
        TPY = PY + textDeltaY * parentCanvas.scaleY + SY * textPosAlignY/2

        textSize = parentCanvas.applyCanvasTextSize(fontSize * parentCanvas.textScale)
    }
    
    
    override fun render(mainRender: MainRender) {
        updateVisuals();
        val lg = mainRender.lg

        val currentButtonColor = if (isHover) hoverColor ?: buttonColor else buttonColor
        val currentTextColor = if (isHover) hoverTextColor ?: textColor else textColor

        RenderElements.renderBlock(
            posX = PX,
            posY = PY,
            width = SX,
            height = SY,
            borderRadius = buttonRadius,
            mainRender = Storage.gameController.clientController.mainRender,
            image = if (isHover) hoverButtonSprite?.img else buttonSprite?.img,
            clr = currentButtonColor
        )

        lg.setTextAlign(textAlignX, textAlignY)
        lg.pg.fill(currentTextColor.red.toFloat(), currentTextColor.green.toFloat(), currentTextColor.blue.toFloat(), currentTextColor.alpha.toFloat())
        lg.setText(text, TPX, TPY, textSize)
    }
}