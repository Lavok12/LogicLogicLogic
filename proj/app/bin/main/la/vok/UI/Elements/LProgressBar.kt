package la.vok.UI.Elements

import la.vok.UI.MainRender
import la.vok.UI.*
import la.vok.UI.Canvas.*
import la.vok.UI.Scenes.*
import la.vok.Storages.Storage
import la.vok.LoadData.LSprite
import java.awt.Color
import la.vok.UI.Canvas.*
import la.vok.UI.Scenes.*
import la.vok.LavokLibrary.*;
import processing.data.JSONObject
import la.vok.GameController.GameController

class LProgressBar(
    gameController: GameController,
    x: Float = 0f,
    y: Float = 0f,
    width: Float = 200f,
    height: Float = 40f,
    alignX: Float = 0f,
    alignY: Float = 0f,
    parentCanvas: LCanvas = Storage.gameController.getCanvas(),
    var progress: Float = 0f,
    var smooth: Boolean = true,
    var steps: Int = 10,
    var fillColor: Color = Color(255, 0, 0),
    var gradientColor: Color? = Color(0, 255, 0),
    var backgroundColor: Color = Color(50, 50, 50),
    var borderRadius: Float = 10f,
    var fillImageKey: String = "",
    var backgroundImageKey: String = "",
    var imagePadding: Float = 2f,
    var paddingX: Float = 4f,
    var paddingY: Float = 4f,
    var scaleX: Float = 1f,
    var scaleY: Float = 1f,
    var postImageKey: String = "",
    offsetByWidth: Float = 0f,
    offsetByHeight: Float = 0f,
    percentWidth: Float = -1f,
    percentHeight: Float = -1f,
    maxWidth: Float = 0f,
    maxHeight: Float = 0f,
    minWidth: Float = 0f,
    minHeight: Float = 0f,
    tag: String = ""
) : LElement(
    gameController, x, y, width, height, alignX, alignY, parentCanvas,
    percentWidth, percentHeight,
    offsetByWidth, offsetByHeight,
    maxWidth, maxHeight,
    minWidth, minHeight,
    tag
) {

    var fillSprite: LSprite? = null
    var backgroundSprite: LSprite? = null
    var postSprite: LSprite? = null

    companion object {
        fun JSONToElement(json: JSONObject, parentCanvas: LCanvas, gameController: GameController): LProgressBar {
            val x = json.LgetFloat("x", 0f)
            val y = json.LgetFloat("y", 0f)
            val width = json.LgetFloat("width", 200f)
            val height = json.LgetFloat("height", 40f)
            val alignX = json.LgetFloat("alignX", 0f)
            val alignY = json.LgetFloat("alignY", 0f)
            val progress = json.LgetFloat("progress", 0f)
            val smooth = json.LgetBoolean("smooth", true)
            val steps = json.LgetInt("steps", 10)
            val fillColor = Functions.getColorFromJSON(json, "fillColor", Color(255, 0, 0))
            val gradientColor = if (json.hasKey("gradientColor")) Functions.getColorFromJSON(json, "gradientColor", Color(0, 255, 0)) else null
            val backgroundColor = Functions.getColorFromJSON(json, "backgroundColor", Color(50, 50, 50))
            val borderRadius = json.LgetFloat("borderRadius", 10f)
            val fillImageKey = json.LgetString("fillImageKey", "")
            val backgroundImageKey = json.LgetString("backgroundImageKey", "")
            val imagePadding = json.LgetFloat("imagePadding", 2f)
            val paddingX = json.LgetFloat("paddingX", 4f)
            val paddingY = json.LgetFloat("paddingY", 4f)
            val scaleX = json.LgetFloat("scaleX", 1f)
            val scaleY = json.LgetFloat("scaleY", 1f)
            val offsetByWidth = json.LgetFloat("offsetByWidth", 0f)
            val offsetByHeight = json.LgetFloat("offsetByHeight", 0f)
            val percentWidth = json.LgetFloat("percentWidth", -1f)
            val percentHeight = json.LgetFloat("percentHeight", -1f)
            val maxWidth = json.LgetFloat("maxWidth", 0f)
            val maxHeight = json.LgetFloat("maxHeight", 0f)
            val minWidth = json.LgetFloat("minWidth", 0f)
            val minHeight = json.LgetFloat("minHeight", 0f)
            val tag = json.LgetString("tag", "")
            val postImageKey = json.LgetString("postImageKey", "")
    
            var ret = LProgressBar(
                gameController, x, y, width, height,
                alignX, alignY, parentCanvas,
                progress, smooth, steps,
                fillColor, gradientColor, backgroundColor,
                borderRadius,
                fillImageKey, backgroundImageKey,
                imagePadding, paddingX, paddingY,
                scaleX, scaleY,
                postImageKey,
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
    

    init {
        updateSprites();
    }

    override fun updateSprites() {
        super.updateSprites()
        if (fillImageKey.isNotEmpty()) {
            fillSprite = Storage.gameController.spriteLoader.getSprite(fillImageKey)
        }
        if (backgroundImageKey.isNotEmpty()) {
            backgroundSprite = Storage.gameController.spriteLoader.getSprite(backgroundImageKey)
        }
        if (postImageKey.isNotEmpty()) {
            postSprite = Storage.gameController.spriteLoader.getSprite(postImageKey)
        }
    }

    override fun renderElement(mainRender: MainRender) {
        val lg = mainRender.lg
        // Внутренние размеры с учётом padding
        val innerX = PX + paddingX
        val innerY = PY
        val innerW = SX - 2 * paddingX
        val innerH = SY - 2 * paddingY

        val currentProgress = progress.coerceIn(0f, 1f)

        // 1. Фон
        RenderElements.renderBlock(
            PX, PY, SX * scaleX, SY * scaleY,
            backgroundColor,
            borderRadius,
            mainRender,
            backgroundSprite?.img
        )

        // 2. Заливка (пошагово или плавно)
        if (steps > 0 && !smooth) {
            val stepWidth = (innerW - (imagePadding - 1) * steps) / steps
            val filledSteps = (currentProgress * steps).toInt()
            
            for (i in 0 until filledSteps) {
                var fcolor = fillColor
                if (gradientColor != null) {
                    fcolor = fillColor * (1 - i.toFloat() / steps) + gradientColor!! * (i.toFloat() / steps)
                }
                if (fillSprite != null) {
                    lg.pg.tint(fcolor.red.toFloat(), fcolor.green.toFloat(), fcolor.blue.toFloat(), fcolor.alpha.toFloat());
                }
                val stepX = innerX + i * (stepWidth + imagePadding)
                RenderElements.renderBlock(
                    stepX - SX / 2 + stepWidth / 2,
                    innerY,
                    stepWidth,
                    innerH,
                    fcolor,
                    borderRadius,
                    mainRender,
                    fillSprite?.img
                )
            }
        } else {
            val filledWidth = innerW * currentProgress
            if (fillSprite != null) {
                lg.pg.tint(fillColor.red.toFloat(), fillColor.green.toFloat(), fillColor.blue.toFloat(), fillColor.alpha.toFloat());
            }
            RenderElements.renderBlock(
                innerX - SX / 2 + filledWidth / 2,
                innerY,
                filledWidth,
                innerH,
                fillColor,
                borderRadius,
                mainRender,
                fillSprite?.img
            )
        }
        // 3. пост-изображение
        if (postSprite != null) {
            RenderElements.renderBlock(
                PX, PY, SX * scaleX, SY * scaleY,
                backgroundColor,
                borderRadius,
                mainRender,
                postSprite?.img
            )
        }
    }
}
