package la.volk.Render.Elements

import la.vok.Render.MainRender
import la.vok.Render.RenderElements.RenderElements
import la.vok.Storages.Storage
import la.vok.LoadData.LSprite
import java.awt.Color
import processing.core.PApplet
import processing.data.JSONObject
import la.vok.Render.LCanvas
import la.vok.LavokLibrary.Functions
import la.vok.GameController.GameController

class LButton(
    x: Float = 0f, // X position of the button
    y: Float = 0f, // Y position of the button
    width: Float = 200f, // Width of the button
    height: Float = 100f, // Height of the button
    alignX: Float = 0f, // X alignment of the button (-1 = left, 1 = right, 0.0 = center)
    alignY: Float = 0f, // Y alignment of the button (-1 = top, 1 = bottom, 0.0 = center)
    parentCanvas: LCanvas = Storage.gameController.clientController.mainRender.mainCanvas, // Parent canvas for the button
    var text: String = "Button", // Text to be displayed on the button
    var textAlignX: Int = 0, // X alignment of the text (-1 = left, 1 = right, 0 = center)
    var textAlignY: Int = 0, // Y alignment of the text (-1 = top, 1 = bottom, 0 = center)
    var fontSize: Float = 30f, // Font size of the text
    var buttonRadius: Float = 10f, // Radius of the button corners
    var buttonColor: Color = Color(150, 120, 100), // Color of the button
    var hoverColor: Color? = Color(140, 110, 90, 200), // Color of the button when hovered
    var textColor: Color = Color(255, 255, 255), // Color of the text
    var hoverTextColor: Color? = null, // Color of the text when hovered
    var imageKey: String = "", // Key for the button image
    var hoverImageKey: String = "", // Key for the button hover image
    var textDeltaX: Float = 0f, // X offset for the text position
    var textDeltaY: Float = 0f, // Y offset for the text position
    var textPosAlignX: Float = 0f, // X alignment of the text position (-1 = left, 1 = right, 0.0 = center)
    var textPosAlignY: Float = 0f, // Y alignment of the text position (-1 = top, 1 = bottom, 0.0 = center)
    var scaleX: Float = 1f, // Scale factor for the button width
    var scaleY: Float = 1f, // Scale factor for the button height
    offsetByWidth: Float = 0f, // Offset for the button position based on width
    offsetByHeight: Float = 0f, // Offset for the button position based on height
    percentWidth: Float = -1f, // Percentage width of the button (-1 = no percentage, 0.0 = 0%, 1.0 = 100%)
    percentHeight: Float = -1f, // Percentage height of the button (-1 = no percentage, 0.0 = 0%, 1.0 = 100%)
    maxWidth: Float = 0f, // Maximum width of the button
    maxHeight: Float = 0f, // Maximum height of the button
    minWidth: Float = 0f, // Minimum width of the button
    minHeight: Float = 0f, // Minimum height of the button
    tag: String = "" // Tag for the button

) : LElement(x, y, width, height, alignX, alignY, parentCanvas, 
    percentWidth, percentHeight, offsetByWidth, offsetByHeight, 
    maxWidth, maxHeight, minWidth, minHeight, tag
) {
    var buttonSprite: LSprite? = null
    var hoverButtonSprite: LSprite? = null
    var isHover: Boolean = false

    var TPX: Float = 0f
    var TPY: Float = 0f
    var textSize: Float = 0f

    init {
        updateSprites();
    }

    companion object {
        fun JSONToElement(json: JSONObject, parentCanvas: LCanvas, gameController: GameController): LButton {
            val x = if (json.hasKey("x")) json.getFloat("x") else 0f
            val y = if (json.hasKey("y")) json.getFloat("y") else 0f
            val width = if (json.hasKey("width")) json.getFloat("width") else 200f
            val height = if (json.hasKey("height")) json.getFloat("height") else 100f
            val alignX = if (json.hasKey("alignX")) json.getFloat("alignX") else 0f
            val alignY = if (json.hasKey("alignY")) json.getFloat("alignY") else 0f
            val percentWidth = if (json.hasKey("percentWidth")) json.getFloat("percentWidth") else -1f
            val percentHeight = if (json.hasKey("percentHeight")) json.getFloat("percentHeight") else -1f
            val offsetByWidth = if (json.hasKey("offsetByWidth")) json.getFloat("offsetByWidth") else 0f
            val offsetByHeight = if (json.hasKey("offsetByHeight")) json.getFloat("offsetByHeight") else 0f
            val maxWidth = if (json.hasKey("maxWidth")) json.getFloat("maxWidth") else 0f
            val maxHeight = if (json.hasKey("maxHeight")) json.getFloat("maxHeight") else 0f
            val minWidth = if (json.hasKey("minWidth")) json.getFloat("minWidth") else 0f
            val minHeight = if (json.hasKey("minHeight")) json.getFloat("minHeight") else 0f
            val tag = if (json.hasKey("tag")) json.getString("tag") else ""
    
            val text = if (json.hasKey("text")) json.getString("text") else "Button"
            val textAlignX = if (json.hasKey("textAlignX")) json.getInt("textAlignX") else 0
            val textAlignY = if (json.hasKey("textAlignY")) json.getInt("textAlignY") else 0
            val fontSize = if (json.hasKey("fontSize")) json.getFloat("fontSize") else 30f
            val buttonRadius = if (json.hasKey("buttonRadius")) json.getFloat("buttonRadius") else 10f
            val imageKey = if (json.hasKey("imageKey")) json.getString("imageKey") else ""
            val hoverImageKey = if (json.hasKey("hoverImageKey")) json.getString("hoverImageKey") else ""
            val textDeltaX = if (json.hasKey("textDeltaX")) json.getFloat("textDeltaX") else 0f
            val textDeltaY = if (json.hasKey("textDeltaY")) json.getFloat("textDeltaY") else 0f
            val textPosAlignX = if (json.hasKey("textPosAlignX")) json.getFloat("textPosAlignX") else 0f
            val textPosAlignY = if (json.hasKey("textPosAlignY")) json.getFloat("textPosAlignY") else 0f
            val scaleX = if (json.hasKey("scaleX")) json.getFloat("scaleX") else 1f
            val scaleY = if (json.hasKey("scaleY")) json.getFloat("scaleY") else 1f
    
            val buttonColor = Functions.getColorFromJSON(json, "buttonColor", Color(150, 120, 100))
            val hoverColor = Functions.getColorFromJSON(json, "hoverColor", Color(140, 110, 90, 200))
            val textColor = Functions.getColorFromJSON(json, "textColor", Color(255, 255, 255))
            val hoverTextColor =
                if (json.hasKey("hoverTextColor")) Functions.getColorFromJSON(json, "hoverTextColor", textColor) else null
            
            
            var ret = LButton(
                x, y, width, height, alignX, alignY, parentCanvas,
                text, textAlignX, textAlignY, fontSize,
                buttonRadius, buttonColor, hoverColor,
                textColor, hoverTextColor,
                imageKey, hoverImageKey,
                textDeltaX, textDeltaY,
                textPosAlignX, textPosAlignY,
                scaleX, scaleY,
                offsetByWidth, offsetByHeight,
                percentWidth, percentHeight,
                maxWidth, maxHeight,
                minWidth, minHeight,
                tag
            )
            ret.gameController = gameController;
            ret.checkChilds(json);
            return ret;
        }
    }
    

    override fun updateSprites() {
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
            width = SX*scaleX,
            height = SY*scaleY,
            borderRadius = buttonRadius,
            mainRender = Storage.gameController.clientController.mainRender,
            image = if (isHover) hoverButtonSprite?.img else buttonSprite?.img,
            clr = currentButtonColor
        )

        lg.setTextAlign(textAlignX, textAlignY)
        lg.pg.fill(currentTextColor.red.toFloat(), currentTextColor.green.toFloat(), currentTextColor.blue.toFloat(), currentTextColor.alpha.toFloat())
        lg.setText(text, TPX, TPY, textSize)

        super.render(mainRender);
    }
}