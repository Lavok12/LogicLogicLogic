package la.volk.Render.Elements

import la.vok.Render.MainRender
import la.vok.Render.RenderElements.RenderElements
import la.vok.Storages.Storage
import la.vok.LoadData.LSprite
import la.vok.Render.LCanvas
import java.awt.Color
import processing.data.JSONObject
import la.vok.LavokLibrary.Functions
import la.vok.GameController.GameController

class LPanel(
    x: Float = 0f, // Положение X
    y: Float = 0f, // Положение Y
    width: Float = 200f, // Ширина панели
    height: Float = 100f, // Высота панели
    alignX: Float = 0f, // Выравнивание по X
    alignY: Float = 0f, // Выравнивание по Y
    parentCanvas: LCanvas = Storage.gameController.clientController.mainRender.mainCanvas, // Канва родителя
    var panelColor: Color = Color(100, 100, 100, 255), // Цвет панели
    var imageKey: String = "", // Ключ изображения панели
    var scaleX: Float = 1f, // Масштаб по ширине
    var scaleY: Float = 1f, // Масштаб по высоте
    offsetByWidth: Float = 0f, // Смещение по ширине
    offsetByHeight: Float = 0f, // Смещение по высоте
    percentWidth: Float = -1f, // Процент ширины (-1 = нет процента, 0.0 = 0%, 1.0 = 100%)
    percentHeight: Float = -1f, // Процент высоты (-1 = нет процента, 0.0 = 0%, 1.0 = 100%)
    maxWidth: Float = 0f, // Максимальная ширина
    maxHeight: Float = 0f, // Максимальная высота
    minWidth: Float = 0f, // Минимальная ширина
    minHeight: Float = 0f, // Минимальная высота
    var borderRadius: Float = 0f, // Радиус скругления углов
    tag: String = "" // Tag for the button

) : LElement(
    x, y, width, height, alignX, alignY, parentCanvas,
    percentWidth, percentHeight, offsetByWidth, offsetByHeight,
    maxWidth, maxHeight, minWidth, minHeight, tag
) {
    private var panelSprite: LSprite? = null

    companion object {
        fun JSONToElement(json: JSONObject, parentCanvas: LCanvas, gameController: GameController): LPanel {
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
            val imageKey = if (json.hasKey("imageKey")) json.getString("imageKey") else ""
            val panelColor = Functions.getColorFromJSON(json, "panelColor", Color(100, 100, 100, 255))
            val scaleX = if (json.hasKey("scaleX")) json.getFloat("scaleX") else 1f
            val scaleY = if (json.hasKey("scaleY")) json.getFloat("scaleY") else 1f
            val borderRadius = if (json.hasKey("borderRadius")) json.getFloat("borderRadius") else 0f
            
            var ret = LPanel(
                x = x,
                y = y,
                width = width,
                height = height,
                alignX = alignX,
                alignY = alignY,
                parentCanvas = parentCanvas,
                panelColor = panelColor,
                imageKey = imageKey,
                scaleX = scaleX,
                scaleY = scaleY,
                offsetByWidth = offsetByWidth,
                offsetByHeight = offsetByHeight,
                percentWidth = percentWidth,
                percentHeight = percentHeight,
                maxWidth = maxWidth,
                maxHeight = maxHeight,
                minWidth = minWidth,
                minHeight = minHeight,
                borderRadius = borderRadius,
                tag = tag
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
        if (imageKey.isNotEmpty()) {
            panelSprite = Storage.gameController.spriteLoader.getSprite(imageKey)
        }
    }

    override fun render(mainRender: MainRender) {
        updateVisuals()

        RenderElements.renderBlock(
            posX = PX,
            posY = PY,
            width = SX*scaleX,
            height = SY*scaleY,
            borderRadius = borderRadius,
            mainRender = mainRender,
            image = panelSprite?.img,
            clr = panelColor
        )

        super.render(mainRender);
    }
}
