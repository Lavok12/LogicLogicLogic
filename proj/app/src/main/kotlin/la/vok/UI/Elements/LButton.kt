package la.vok.UI.Elements

import la.vok.UI.*
import la.vok.Storages.Storage
import la.vok.LoadData.LSprite
import java.awt.Color
import processing.core.PApplet
import processing.data.JSONObject
import la.vok.UI.LCanvas
import la.vok.LavokLibrary.*
import la.vok.GameController.GameController

class LButton(
    gameController: GameController,
    x: Float = 0f, // X position of the button
    y: Float = 0f, // Y position of the button
    width: Float = 200f, // Width of the button
    height: Float = 100f, // Height of the button
    alignX: Float = 0f, // X alignment of the button (-1 = left, 1 = right, 0.0 = center)
    alignY: Float = 0f, // Y alignment of the button (-1 = top, 1 = bottom, 0.0 = center)
    parentCanvas: LCanvas = gameController.getCanvas(), // Parent canvas for the button
    var text: String = "Button", // Text to be displayed on the button
    var textTranslate: Boolean = true, // Language key
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
    var postImageKey: String = "", // Key for the button post image
    var postHoverImageKey: String = "", // Key for the button post hover image
    offsetByWidth: Float = 0f, // Offset for the button position based on width
    offsetByHeight: Float = 0f, // Offset for the button position based on height
    percentWidth: Float = -1f, // Percentage width of the button (-1 = no percentage, 0.0 = 0%, 1.0 = 100%)
    percentHeight: Float = -1f, // Percentage height of the button (-1 = no percentage, 0.0 = 0%, 1.0 = 100%)
    maxWidth: Float = 0f, // Maximum width of the button
    maxHeight: Float = 0f, // Maximum height of the button
    minWidth: Float = 0f, // Minimum width of the button
    minHeight: Float = 0f, // Minimum height of the button
    tag: String = "" // Tag for the button

) : LElement(gameController, x, y, width, height, alignX, alignY, parentCanvas, 
    percentWidth, percentHeight, offsetByWidth, offsetByHeight, 
    maxWidth, maxHeight, minWidth, minHeight, tag
) {
    var buttonSprite: LSprite? = null
    var hoverButtonSprite: LSprite? = null
    var postSprite: LSprite? = null
    var isHover: Boolean = false

    var TPX: Float = 0f
    var TPY: Float = 0f
    var textSize: Float = 0f

    init {
        updateSprites();
    }

    companion object {
        fun JSONToElement(json: JSONObject, parentCanvas: LCanvas, gameController: GameController): LButton {
            val x = json.LgetFloat("x", 0f)
        val y = json.LgetFloat("y", 0f)
        val width = json.LgetFloat("width", 200f)
        val height = json.LgetFloat("height", 100f)
        val alignX = json.LgetFloat("alignX", 0f)
        val alignY = json.LgetFloat("alignY", 0f)
        val percentWidth = json.LgetFloat("percentWidth", -1f)
        val percentHeight = json.LgetFloat("percentHeight", -1f)
        val offsetByWidth = json.LgetFloat("offsetByWidth", 0f)
        val offsetByHeight = json.LgetFloat("offsetByHeight", 0f)
        val maxWidth = json.LgetFloat("maxWidth", 0f)
        val maxHeight = json.LgetFloat("maxHeight", 0f)
        val minWidth = json.LgetFloat("minWidth", 0f)
        val minHeight = json.LgetFloat("minHeight", 0f)
        val tag = json.LgetString("tag", "")

        val text = json.LgetString("text", "Button")
        val textTranslate = json.LgetBoolean("textTranslate", true)

        val textAlignX = json.LgetInt("textAlignX", 0)
        val textAlignY = json.LgetInt("textAlignY", 0)
        val fontSize = json.LgetFloat("fontSize", 30f)
        val buttonRadius = json.LgetFloat("buttonRadius", 10f)
        val imageKey = json.LgetString("imageKey", "")
        val hoverImageKey = json.LgetString("hoverImageKey", "")
        val postImageKey = json.LgetString("postImageKey", "")
        val postHoverImageKey = json.LgetString("postHoverImageKey", "")

        val textDeltaX = json.LgetFloat("textDeltaX", 0f)
        val textDeltaY = json.LgetFloat("textDeltaY", 0f)
        val textPosAlignX = json.LgetFloat("textPosAlignX", 0f)
        val textPosAlignY = json.LgetFloat("textPosAlignY", 0f)
        val scaleX = json.LgetFloat("scaleX", 1f)
        val scaleY = json.LgetFloat("scaleY", 1f)

        val buttonColor = Functions.getColorFromJSON(json, "buttonColor", Color(150, 120, 100))
        val hoverColor = Functions.getColorFromJSON(json, "hoverColor", Color(140, 110, 90, 200))
        val textColor = Functions.getColorFromJSON(json, "textColor", Color(255, 255, 255))
        val hoverTextColor = Functions.getColorFromJSON(json, "hoverTextColor",  Color(255, 255, 255))
        
                
        var ret = LButton(
            gameController, x, y, width, height, alignX, alignY, parentCanvas,
            text, textTranslate, textAlignX, textAlignY, fontSize,
            buttonRadius, buttonColor, hoverColor,
            textColor, hoverTextColor,
            imageKey, hoverImageKey,
            textDeltaX, textDeltaY,
            textPosAlignX, textPosAlignY,
            scaleX, scaleY,
            postImageKey, postHoverImageKey,
            offsetByWidth, offsetByHeight,
            percentWidth, percentHeight,
            maxWidth, maxHeight,
            minWidth, minHeight,
            tag
        )
            ret.gameController = gameController;
            ret.checkChilds(json);
            ret.setEvents(json)
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
        if (postImageKey != "") {
            postSprite = Storage.gameController.spriteLoader.getSprite(postImageKey)
        }
    }

    override fun updateVisuals() {
        super.updateVisuals()

        TPX = PX + textDeltaX * parentCanvas.scaleX + SX * textPosAlignX/2
        TPY = PY + textDeltaY * parentCanvas.scaleY + SY * textPosAlignY/2

        textSize = parentCanvas.applyCanvasTextSize(fontSize * parentCanvas.textScale)
    }
    
    
    override fun renderElement(mainRender: MainRender) {
        updateVisuals()
        val lg = mainRender.lg

        val currentButtonColor = if (isHover) hoverColor ?: buttonColor else buttonColor
        val currentTextColor = if (isHover) hoverTextColor ?: textColor else textColor

        RenderElements.renderBlock(
            posX = PX,
            posY = PY,
            width = SX*scaleX,
            height = SY*scaleY,
            borderRadius = buttonRadius,
            mainRender = Storage.gameController.mainRender,
            image = if (isHover) hoverButtonSprite?.img else buttonSprite?.img,
            clr = currentButtonColor
        )

        lg.setTextAlign(textAlignX, textAlignY)
        lg.pg.fill(currentTextColor.red.toFloat(), currentTextColor.green.toFloat(), currentTextColor.blue.toFloat(), currentTextColor.alpha.toFloat())
        lg.setText(if (textTranslate) {gameController.languageController.getText(text)} else {text}, TPX, TPY, textSize)

        if (postSprite != null) {
            RenderElements.renderBlock(
                posX = PX,
                posY = PY,
                width = SX*scaleX,
                height = SY*scaleY,
                borderRadius = buttonRadius,
                mainRender = Storage.gameController.mainRender,
                image = if (isHover) Storage.gameController.spriteLoader.getSprite(postHoverImageKey)?.img else postSprite?.img,
                clr = currentButtonColor
            )
        }
    }
} 