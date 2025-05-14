package la.volk.Render.Elements

import la.vok.Render.MainRender
import la.vok.Render.RenderElements.RenderElements
import la.vok.Render.LCanvas
import la.vok.Storages.Storage
import la.vok.LoadData.LSprite
import java.awt.Color

import la.vok.LavokLibrary.*;
import processing.data.JSONObject
import la.vok.GameController.GameController


class LProgressBar(
    x: Float = 0f,
    y: Float = 0f,
    width: Float = 200f,
    height: Float = 40f,
    alignX: Float = 0f,
    alignY: Float = 0f,
    parentCanvas: LCanvas = Storage.gameController.mainRender.mainCanvas,
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
    var paddingX: Float = 4f, // ← новый параметр отступа по X
    var paddingY: Float = 4f, // ← новый параметр отступа по Y
    var scaleX: Float = 1f, // Scale factor for the progress bar width
    var scaleY: Float = 1f, // Scale factor for the progress bar height
    offsetByWidth: Float = 0f,
    offsetByHeight: Float = 0f,
    percentWidth: Float = -1f,
    percentHeight: Float = -1f,
    maxWidth: Float = 0f,
    maxHeight: Float = 0f,
    minWidth: Float = 0f,
    minHeight: Float = 0f,
    tag: String = "" // Tag for the button
) : LElement(
    x, y, width, height, alignX, alignY, parentCanvas,
    percentWidth, percentHeight,
    offsetByWidth, offsetByHeight,
    maxWidth, maxHeight,
    minWidth, minHeight,
    tag
) {

    var fillSprite: LSprite? = null
    var backgroundSprite: LSprite? = null

    companion object {
        fun JSONToElement(json: JSONObject, parentCanvas: LCanvas, gameController: GameController): LProgressBar {
            val x = if (json.hasKey("x")) json.getFloat("x") else 0f
            val y = if (json.hasKey("y")) json.getFloat("y") else 0f
            val width = if (json.hasKey("width")) json.getFloat("width") else 200f
            val height = if (json.hasKey("height")) json.getFloat("height") else 40f
            val alignX = if (json.hasKey("alignX")) json.getFloat("alignX") else 0f
            val alignY = if (json.hasKey("alignY")) json.getFloat("alignY") else 0f
            val progress = if (json.hasKey("progress")) json.getFloat("progress") else 0f
            val smooth = if (json.hasKey("smooth")) json.getBoolean("smooth") else true
            val steps = if (json.hasKey("steps")) json.getInt("steps") else 10
            val fillColor = Functions.getColorFromJSON(json, "fillColor", Color(255, 0, 0))
            val gradientColor = if (json.hasKey("gradientColor")) Functions.getColorFromJSON(json, "gradientColor", Color(0, 255, 0)) else null
            val backgroundColor = Functions.getColorFromJSON(json, "backgroundColor", Color(50, 50, 50))
            val borderRadius = if (json.hasKey("borderRadius")) json.getFloat("borderRadius") else 10f
            val fillImageKey = if (json.hasKey("fillImageKey")) json.getString("fillImageKey") else ""
            val backgroundImageKey = if (json.hasKey("backgroundImageKey")) json.getString("backgroundImageKey") else ""
            val imagePadding = if (json.hasKey("imagePadding")) json.getFloat("imagePadding") else 2f
            val paddingX = if (json.hasKey("paddingX")) json.getFloat("paddingX") else 4f
            val paddingY = if (json.hasKey("paddingY")) json.getFloat("paddingY") else 4f
            val scaleX = if (json.hasKey("scaleX")) json.getFloat("scaleX") else 1f
            val scaleY = if (json.hasKey("scaleY")) json.getFloat("scaleY") else 1f
            val offsetByWidth = if (json.hasKey("offsetByWidth")) json.getFloat("offsetByWidth") else 0f
            val offsetByHeight = if (json.hasKey("offsetByHeight")) json.getFloat("offsetByHeight") else 0f
            val percentWidth = if (json.hasKey("percentWidth")) json.getFloat("percentWidth") else -1f
            val percentHeight = if (json.hasKey("percentHeight")) json.getFloat("percentHeight") else -1f
            val maxWidth = if (json.hasKey("maxWidth")) json.getFloat("maxWidth") else 0f
            val maxHeight = if (json.hasKey("maxHeight")) json.getFloat("maxHeight") else 0f
            val minWidth = if (json.hasKey("minWidth")) json.getFloat("minWidth") else 0f
            val minHeight = if (json.hasKey("minHeight")) json.getFloat("minHeight") else 0f
            val tag = if (json.hasKey("tag")) json.getString("tag") else ""
    
            var ret = LProgressBar(
                x, y, width, height,
                alignX, alignY, parentCanvas,
                progress, smooth, steps,
                fillColor, gradientColor, backgroundColor,
                borderRadius,
                fillImageKey, backgroundImageKey,
                imagePadding, paddingX, paddingY,
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
    }
    
    override fun render(mainRender: MainRender) {
        updateVisuals()
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
        super.render(mainRender);
    }
}
