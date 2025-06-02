package la.vok.UI;

import java.awt.Color
import la.vok.UI.Rendering
import la.vok.Storages.Storage
import processing.core.PImage
import la.vok.LavokLibrary.Functions
import la.vok.LavokLibrary.LGraphics
import la.volk.UI.Elements.LElement

object RenderElements {
    fun renderBlock(
        posX: Float, 
        posY: Float, 
        width: Float, 
        height: Float, 
        clr: Color, 
        borderRadius: Float = 0f,
        rendering: Rendering = Storage.gameController.rendering,
        image: PImage? = null
        ) {
        var lg: LGraphics = rendering.lg
        if (image == null) {
            lg.pg.fill(clr.red.toFloat(), clr.green.toFloat(), clr.blue.toFloat(), clr.alpha.toFloat())
            if (borderRadius == 0f) {
                lg.setBlock(posX, posY, width, height)
            } else {
                lg.setBlock(posX, posY, width, height, borderRadius)
            }
        } else {
            lg.pg.tint(clr.red.toFloat(), clr.green.toFloat(), clr.blue.toFloat(), clr.alpha.toFloat());
            lg.setImage(image, posX, posY, width, height)
        }
    }
    fun renderElement(element: LElement, rendering: Rendering = Storage.gameController.rendering) {
        element.render(rendering);
    }
}