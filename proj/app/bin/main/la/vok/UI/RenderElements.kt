package la.vok.UI

import java.awt.Color
import la.vok.UI.MainRender
import la.vok.Storages.Storage
import processing.core.PImage
import la.vok.LavokLibrary.Functions
import la.vok.LavokLibrary.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.UI.Elements.LElement

object RenderElements {
    fun renderBlock(
        pos: Vec2,
        wh: Vec2,
        clr: Color, 
        borderRadius: Float = 0f,
        mainRender: MainRender,
        image: PImage? = null
        ) {
        var lg: LGraphics = mainRender.lg
        if (image == null) {
            lg.pg.fill(clr.red.toFloat(), clr.green.toFloat(), clr.blue.toFloat(), clr.alpha.toFloat())
            if (borderRadius == 0f) {
                lg.setBlock(pos, wh)
            } else {
                lg.setBlock(pos, wh, borderRadius)
            }
        } else {
            lg.pg.tint(clr.red.toFloat(), clr.green.toFloat(), clr.blue.toFloat(), clr.alpha.toFloat());
            lg.setImage(image, pos, wh)
        }
    }
    fun renderElement(element: LElement, mainRender: MainRender) {
        element.render(mainRender);
    }
}