package la.volk.Render.Elements

import la.vok.Render.MainRender
import la.vok.Render.RenderElements.RenderElements
import la.vok.Render.LCanvas
import la.vok.Storages.Storage
import la.vok.LoadData.LSprite
import java.awt.Color
import processing.core.PApplet

open class LElement(
    var x: Float = 0f,
    var y: Float = 0f,
    var width: Float = 200f,
    var height: Float = 100f,
    var alignX: Float = 0f,
    var alignY: Float = 0f,
    var parentCanvas: LCanvas = Storage.gameController.clientController.mainRender.mainCanvas,
    var percentWidth: Float = -1f,
    var percentHeight: Float = -1f,
    var offsetByWidth: Float = 0f,
    var offsetByHeight: Float = 0f
) {
    open var PX: Float = 0f
    open var PY: Float = 0f
    open var SX: Float = 0f
    open var SY: Float = 0f
    open var elementCanvas: LCanvas = LCanvas(0f, 0f, 0f, 0f, 1f, 1f, 1f)

    open fun updateVisuals() {
        PX = parentCanvas.applyCanvasPosX(x, alignX)
        PY = parentCanvas.applyCanvasPosY(y, alignY)
    
        SX = if (percentWidth != -1f)
            parentCanvas.applyCanvasSizeX(width + parentCanvas.canvasSizePercentX(percentWidth))
        else
            parentCanvas.applyCanvasSizeX(width)
    
        SY = if (percentHeight != -1f)
            parentCanvas.applyCanvasSizeY(height + parentCanvas.canvasSizePercentY(percentHeight))
        else
            parentCanvas.applyCanvasSizeY(height)

        PX += offsetByWidth * SX
        PY += offsetByHeight * SY

        elementCanvas.posX = PX
        elementCanvas.posY = PY
        elementCanvas.width = SX
        elementCanvas.height = SY
    }
    
    open fun render(mainRender: MainRender) {
    }
}