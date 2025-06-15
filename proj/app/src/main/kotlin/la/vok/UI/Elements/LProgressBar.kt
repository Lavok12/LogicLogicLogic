package la.vok.UI.Elements

import la.vok.UI.MainRender
import la.vok.UI.*
import la.vok.UI.Canvas.*
import la.vok.UI.Scenes.*
import la.vok.Storages.Storage
import la.vok.LoadData.LSprite
import java.awt.Color
import la.vok.LavokLibrary.*
import la.vok.LavokLibrary.Vectors.Vec2
import processing.data.JSONObject
import la.vok.GameController.GameController

class LProgressBar(
    gameController: GameController,
    pos: Vec2 = Vec2(0f),
    wh: Vec2 = Vec2(200f, 40f),
    align: Vec2 = Vec2(0f),
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
    var padding: Vec2 = Vec2(4f),
    var scale: Vec2 = Vec2(1f),
    var postImageKey: String = "",
    offsetByWH: Vec2 = Vec2(0f),
    percentWidth: Float = -1f,
    percentHeight: Float = -1f,
    tag: String = ""
) : LElement(
    gameController, pos, wh, align, parentCanvas,
    percentWidth, percentHeight, offsetByWH, tag
) {

    var fillSprite: LSprite? = null
    var backgroundSprite: LSprite? = null
    var postSprite: LSprite? = null

    companion object {
        fun JSONToElement(json: JSONObject, parentCanvas: LCanvas, gameController: GameController): LProgressBar {
            val pos = json.getVec2("pos", Vec2(0f))
            val wh = json.getVec2("wh", Vec2(200f, 40f))
            val align = json.getVec2("align", Vec2(0f))
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
            val padding = json.getVec2("padding", Vec2(4f))
            val scale = json.getVec2("scale", Vec2(1f))
            val offsetByWH = json.getVec2("offsetByWH", Vec2(0f))
            val percentWidth = json.LgetFloat("percentWidth", -1f)
            val percentHeight = json.LgetFloat("percentHeight", -1f)
            val maxWH = json.getVec2("maxWH", Vec2(0f))
            val minWH = json.getVec2("minWH", Vec2(0f))
            val tag = json.LgetString("tag", "")
            val postImageKey = json.LgetString("postImageKey", "")

            val ret = LProgressBar(
                gameController, pos, wh, align, parentCanvas,
                progress, smooth, steps,
                fillColor, gradientColor, backgroundColor,
                borderRadius,
                fillImageKey, backgroundImageKey,
                imagePadding, padding, scale,
                postImageKey,
                offsetByWH, percentWidth, percentHeight, tag
            )
            ret.gameController = gameController
            ret.checkChilds(json)
            ret.setEvents(json)
            return ret
        }
    }

    init {
        updateSprites()
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
        val currentProgress = progress.coerceIn(0f, 1f)

        val vs = visualSize
        val ps = visualPos

        val innerW = vs.x - 2 * padding.x
        val innerH = vs.y - 2 * padding.y

        val innerPos = Vec2(
            ps.x - vs.x / 2 + padding.x,
            ps.y - vs.y / 2 + padding.y
        )

        // 1. Фон
        RenderElements.renderBlock(
            pos = ps,
            wh = vs * scale,
            clr = backgroundColor,
            borderRadius = borderRadius,
            mainRender = mainRender,
            image = backgroundSprite?.img
        )

        // 2. Прогресс
        if (steps > 0 && !smooth) {
            val stepWidth = (innerW - (imagePadding - 1) * steps) / steps
            val filledSteps = (currentProgress * steps).toInt()

            for (i in 0 until filledSteps) {
                val t = i.toFloat() / steps
                val fcolor = if (gradientColor != null)
                    fillColor * (1 - t) + gradientColor!! * t
                else fillColor

                if (fillSprite != null) {
                    lg.pg.tint(fcolor.red.toFloat(), fcolor.green.toFloat(), fcolor.blue.toFloat(), fcolor.alpha.toFloat())
                }

                val stepX = innerPos.x + i * (stepWidth + imagePadding)
                RenderElements.renderBlock(
                    pos = Vec2(stepX + stepWidth / 2, innerPos.y + innerH / 2),
                    wh = Vec2(stepWidth, innerH),
                    clr = fcolor,
                    borderRadius = borderRadius,
                    mainRender = mainRender,
                    image = fillSprite?.img
                )
            }
        } else {
            val filledWidth = innerW * currentProgress

            if (fillSprite != null) {
                lg.pg.tint(fillColor.red.toFloat(), fillColor.green.toFloat(), fillColor.blue.toFloat(), fillColor.alpha.toFloat())
            }

            RenderElements.renderBlock(
                pos = Vec2(innerPos.x + filledWidth / 2, innerPos.y + innerH / 2),
                wh = Vec2(filledWidth, innerH),
                clr = fillColor,
                borderRadius = borderRadius,
                mainRender = mainRender,
                image = fillSprite?.img
            )
        }

        // 3. Пост-изображение
        if (postSprite != null) {
            RenderElements.renderBlock(
                pos = ps,
                wh = vs * scale,
                clr = backgroundColor,
                borderRadius = borderRadius,
                mainRender = mainRender,
                image = postSprite?.img
            )
        }
    }
}
