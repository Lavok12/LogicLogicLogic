package la.vok.UI.Elements

import la.vok.UI.*
import la.vok.Storages.Storage
import la.vok.LoadData.LSprite
import la.vok.UI.LCanvas
import java.awt.Color
import processing.data.JSONObject
import la.vok.LavokLibrary.*
import la.vok.GameController.GameController

class LPanel(
    gameController: GameController,
    x: Float = 0f, // Положение X
    y: Float = 0f, // Положение Y
    width: Float = 200f, // Ширина панели
    height: Float = 100f, // Высота панели
    alignX: Float = 0f, // Выравнивание по X
    alignY: Float = 0f, // Выравнивание по Y
    parentCanvas: LCanvas = gameController.getCanvas(), // Канва родителя
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
    gameController, x, y, width, height, alignX, alignY, parentCanvas,
    percentWidth, percentHeight, offsetByWidth, offsetByHeight,
    maxWidth, maxHeight, minWidth, minHeight, tag
) {
    private var panelSprite: LSprite? = null

    companion object {
        fun JSONToElement(json: JSONObject, parentCanvas: LCanvas, gameController: GameController): LPanel {
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
            val imageKey = json.LgetString("imageKey", "")
            val panelColor = Functions.getColorFromJSON(json, "panelColor", Color(100, 100, 100, 255))
            val scaleX = json.LgetFloat("scaleX", 1f)
            val scaleY = json.LgetFloat("scaleY", 1f)
            val borderRadius = json.LgetFloat("borderRadius", 0f)

            
            var ret = LPanel(
                gameController,
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
            ret.setEvents(json)
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

    override fun renderElement(mainRender: MainRender) {
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
    }
}
