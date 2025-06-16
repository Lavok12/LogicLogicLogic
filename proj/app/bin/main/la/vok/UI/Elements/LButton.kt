package la.vok.UI.Elements

import la.vok.UI.*
import la.vok.Storages.Storage
import la.vok.LoadData.LSprite
import java.awt.Color
import processing.core.PApplet
import processing.data.JSONObject
import la.vok.UI.Canvas.*
import la.vok.UI.Scenes.*
import la.vok.LavokLibrary.*
import la.vok.GameController.GameController
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.UI.Canvas.*
import la.vok.UI.Scenes.*

class LButton(
    gameController: GameController,
    pos: Vec2 = Vec2(0f), // X position of the button
    wh: Vec2 = Vec2(0f), // Width of the button
    align: Vec2 = Vec2(0f), // X alignment of the button (-1 = left, 1 = right, 0.0 = center)
    parentCanvas: LCanvas = gameController.getCanvas(), // Parent canvas for the button
    var text: String = "Button", // Text to be displayed on the button
    var textAlign: Vec2 = Vec2(0f), // X alignment of the text (-1 = left, 1 = right, 0 = center)
    var fontSize: Float = 30f, // Font size of the text
    var buttonRadius: Float = 10f, // Radius of the button corners
    var buttonColor: Color = Color(150, 120, 100), // Color of the button
    var hoverColor: Color? = Color(140, 110, 90, 200), // Color of the button when hovered
    var textColor: Color = Color(255, 255, 255), // Color of the text
    var hoverTextColor: Color? = null, // Color of the text when hovered
    var imageKey: String = "", // Key for the button image
    var hoverImageKey: String = "", // Key for the button hover image
    var textDelta: Vec2 = Vec2(0f), // X offset for the text position
    var textPosAlign: Vec2 = Vec2(0f), // X alignment of the text position (-1 = left, 1 = right, 0.0 = center)
    var scale: Vec2 = Vec2(1f), // Scale factor for the button width
    var postImageKey: String = "", // Key for the button post image
    var postHoverImageKey: String = "", // Key for the button post hover image
    offsetByWH: Vec2 = Vec2(0f), // Offset for the button position based on width
    percentWidth: Float = -1f, // Percentage width of the button (-1 = no percentage, 0.0 = 0%, 1.0 = 100%)
    percentHeight: Float = -1f, // Percentage height of the button (-1 = no percentage, 0.0 = 0%, 1.0 = 100%)
    tag: String = "" // Tag for the button

) : LElement(
    gameController, pos, wh, align, parentCanvas,
    percentWidth, percentHeight, offsetByWH, tag
) {
    var buttonSprite: LSprite? = null
    var hoverButtonSprite: LSprite? = null
    var postSprite: LSprite? = null
    var isHover: Boolean = false

    var textVisualPos: Vec2 = Vec2(0f)
    var textSize: Float = 0f

    init {
        updateSprites();
    }

    companion object {
        fun JSONToElement(json: JSONObject, parentCanvas: LCanvas, gameController: GameController): LButton {
            val pos = json.getVec2("pos", Vec2(0f))
            val wh = json.getVec2("wh", Vec2(0f))
            val align = json.getVec2("align", Vec2(0f))
            val percentWidth = json.LgetFloat("percentWidth", -1f)
            val percentHeight = json.LgetFloat("percentHeight", -1f)
            val offsetByWH = json.getVec2("offsetByWH", Vec2(0f))
            val tag = json.LgetString("tag", "")

            val text = json.LgetString("text", "Button")

            val textAlign = json.getVec2("textAlign", Vec2(0f))
            val fontSize = json.LgetFloat("fontSize", 30f)
            val buttonRadius = json.LgetFloat("buttonRadius", 10f)
            val imageKey = json.LgetString("imageKey", "")
            val hoverImageKey = json.LgetString("hoverImageKey", "")
            val postImageKey = json.LgetString("postImageKey", "")
            val postHoverImageKey = json.LgetString("postHoverImageKey", "")

            val textDelta = json.getVec2("textDelta", Vec2(0f))
            val textPosAlign = json.getVec2("textPosAlign", Vec2(0f))
            val scale = json.getVec2("scale", Vec2(1f))

            val buttonColor = Functions.getColorFromJSON(json, "buttonColor", Color(150, 120, 100))
            val hoverColor = Functions.getColorFromJSON(json, "hoverColor", Color(140, 110, 90, 200))
            val textColor = Functions.getColorFromJSON(json, "textColor", Color(255, 255, 255))
            val hoverTextColor = Functions.getColorFromJSON(json, "hoverTextColor", Color(255, 255, 255))


            var ret = LButton(
                gameController, pos, wh, align, parentCanvas,
                text, textAlign, fontSize,
                buttonRadius, buttonColor, hoverColor,
                textColor, hoverTextColor,
                imageKey, hoverImageKey,
                textDelta,
                textPosAlign,
                scale,
                postImageKey, postHoverImageKey,
                offsetByWH,
                percentWidth, percentHeight,
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
            buttonSprite = Storage.gameController.loaders.sprites.getSprite(imageKey)
        }
        if (hoverImageKey != "") {
            hoverButtonSprite = Storage.gameController.loaders.sprites.getSprite(hoverImageKey)
        }
        if (postImageKey != "") {
            postSprite = Storage.gameController.loaders.sprites.getSprite(postImageKey)
        }
    }

    fun standartVisuals() {
        textVisualPos = visualPos + textDelta * parentCanvas.scale + visualSize * textPosAlign / 2f
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

        val currentButtonColor = if (isHover) hoverColor ?: buttonColor else buttonColor
        val currentTextColor = if (isHover) hoverTextColor ?: textColor else textColor

        RenderElements.renderBlock(
            pos = visualPos,
            wh = visualSize * scale,
            borderRadius = buttonRadius,
            mainRender = Storage.gameController.mainRender,
            image = if (isHover) hoverButtonSprite?.img else buttonSprite?.img,
            clr = currentButtonColor
        )

        lg.setTextAlign(Math.round(textAlign.x), Math.round(textAlign.y))
        lg.pg.fill(
            currentTextColor.red.toFloat(),
            currentTextColor.green.toFloat(),
            currentTextColor.blue.toFloat(),
            currentTextColor.alpha.toFloat()
        )
        lg.setText(
                text, textVisualPos, textSize
        )

        if (postSprite != null) {
            RenderElements.renderBlock(
                pos = visualPos,
                wh = visualSize * scale,
                borderRadius = buttonRadius,
                mainRender = Storage.gameController.mainRender,
                image = if (isHover) Storage.gameController.loaders.sprites.getSprite(postHoverImageKey)?.img else postSprite?.img,
                clr = currentButtonColor
            )
        }
    }
} 