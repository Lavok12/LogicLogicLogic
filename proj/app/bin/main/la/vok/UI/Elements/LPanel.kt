package la.vok.UI.Elements

import la.vok.UI.*
import la.vok.Storages.Storage
import la.vok.LoadData.LSprite
import la.vok.UI.Canvas.*
import la.vok.UI.Scenes.*
import java.awt.Color
import processing.data.JSONObject
import la.vok.LavokLibrary.*
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.GameController.GameController

class LPanel(
    gameController: GameController,
    pos: Vec2 = Vec2(0f),
    wh: Vec2 = Vec2(200f, 100f),
    align: Vec2 = Vec2(0f),
    parentCanvas: LCanvas = gameController.getCanvas(),
    var panelColor: Color = Color(100, 100, 100, 255),
    var imageKey: String = "",
    var scale: Vec2 = Vec2(1f),
    offsetByWH: Vec2 = Vec2(0f),
    percentWidth: Float = -1f,
    percentHeight: Float = -1f,
    var borderRadius: Float = 0f,
    tag: String = ""
) : LElement(
    gameController, pos, wh, align, parentCanvas,
    percentWidth, percentHeight, offsetByWH
) {
    private var panelSprite: LSprite? = null

    init {
        updateSprites()
    }

    companion object {
        fun JSONToElement(json: JSONObject, parentCanvas: LCanvas, gameController: GameController): LPanel {
            val pos = json.getVec2("pos", Vec2(0f))
            val wh = json.getVec2("wh", Vec2(200f, 100f))
            val align = json.getVec2("align", Vec2(0f))
            val percentWidth = json.LgetFloat("percentWidth", -1f)
            val percentHeight = json.LgetFloat("percentHeight", -1f)
            val offsetByWH = json.getVec2("offsetByWH", Vec2(0f))
            val tag = json.LgetString("tag", "")
            val imageKey = json.LgetString("imageKey", "")
            val panelColor = Functions.getColorFromJSON(json, "panelColor", Color(100, 100, 100, 255))
            val scale = json.getVec2("scale", Vec2(1f))
            val borderRadius = json.LgetFloat("borderRadius", 0f)

            val ret = LPanel(
                gameController,
                pos = pos,
                wh = wh,
                align = align,
                parentCanvas = parentCanvas,
                panelColor = panelColor,
                imageKey = imageKey,
                scale = scale,
                offsetByWH = offsetByWH,
                percentWidth = percentWidth,
                percentHeight = percentHeight,
                borderRadius = borderRadius,
                tag = tag
            )
            ret.gameController = gameController
            ret.checkChilds(json)
            ret.setEvents(json)
            return ret
        }
    }

    override fun updateSprites() {
        if (imageKey.isNotEmpty()) {
            panelSprite = Storage.gameController.loaders.sprites.getSprite(imageKey)
        }
    }

    override fun renderElement(mainRender: MainRender) {
        RenderElements.renderBlock(
            pos = visualPos,
            wh = visualSize * scale,
            borderRadius = borderRadius,
            mainRender = mainRender,
            image = panelSprite?.img,
            clr = panelColor
        )
    }
}
