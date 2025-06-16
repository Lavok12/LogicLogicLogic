package la.vok.UI.Canvas

import la.vok.GameController.GameController
import la.vok.Storages.Storage
import la.vok.LavokLibrary.*
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.UI.Elements.LElement
import la.vok.UI.*
import la.vok.UI.Elements.LayoutDirection
import la.vok.UI.Scenes.*
import javax.swing.text.Element

class LCanvas (
    var pos: Vec2 = Vec2(0f),
    var wh: Vec2 = Vec2(100f),
    var scale: Vec2 = Vec2(1f),
    var localScale: Vec2 = Vec2(1f),
    var textScale: Float = 1f,
    var layer: Int = 0,
    var hasHitbox: Boolean = true,
    var gameController: GameController
) {
    var elements = ArrayList<LElement>();
    var isActive = false

    fun removeElement(tag: String) {
        var element = getElementByTag(tag)
        if (element != null) {
            removeElement(element)
        }
    }
    fun removeElement(element: LElement) {
        if (elements.contains(element)) {
            element.destroy()
            elements.remove(element)
        }
    }

    fun inside(mx: Float, my: Float): Boolean {
        if (!Functions.tap(pos, wh * scale, mx, my)) {
            return false;
        }
        return true;
    }

    fun getElementByTag(tag: String): LElement? {
        for (element in elements) {
            if (element.tag == tag) {
                return element
            }
        }
        return null
    }
    fun findElementByTag(tag: String): LElement? {
        for (element in elements) {
            if (element.tag == tag) {
                return element
            } else {
                var e = element.elementCanvas.findElementByTag(tag)
                if (e != null) {
                    return e
                }
            }
        }
        return null
    }
    fun getElementByTagWithPath(path: String): LElement? {
        val tags = path.split(".")
        var currentElement: LElement? = null
        for (tag in tags) {
            if (currentElement == null) {
                currentElement = getElementByTag(tag)
            } else {
                currentElement = currentElement.elementCanvas.getElementByTag(tag)
            }
            if (currentElement == null) {
                return null
            }
        }
        return currentElement
    }

    fun getElementById(id: Int): LElement? {
        return elements[id]
    }
    fun getElementByIdWithPath(path: String): LElement? {
        val ids = path.split(".")
        var currentElement: LElement? = null
        for (id in ids) {
            if (currentElement == null) {
                currentElement = getElementById(id.toInt())
            } else {
                currentElement = currentElement.elementCanvas.getElementById(id.toInt())
            }
            if (currentElement == null) {
                return null
            }
        }
        return currentElement
    }

    fun deactivate() {
        for (LElement in elements) {
            LElement.deactivate()
        }
    }

    fun activate() {
        for (LElement in elements) {
            LElement.activate()
        }
    }


    fun tick() {
        for (i in elements.size - 1 downTo 0) {
            elements[i].tick()
        }
    }

    fun renderElements() {
        for (LElement in elements) {
            LElement.updateVisuals()
            LElement.render(gameController.mainRender)
        }
    }

    fun renderElementsGrid(parent: LElement) {

    }

    fun renderElementsLine(parent: LElement) {
        var i: Int = -1
        when (parent.layoutDirection) {
            LayoutDirection.HORIZONTAL -> {
                var dx = 0f
                if (parent.alignCenterX) {
                    dx = (elements.size-1) * parent.spacing.x * scale.x * localScale.x / 2
                }
                for (LElement in if (!parent.reverseX) {elements} else {elements.reversed()}) {
                    i++
                    LElement.updateGridVisuals(
                        Vec2(pos.x + (parent.contentDelta.x + i * parent.spacing.x) * scale.x * localScale.x - dx
                        ,
                        pos.y + (parent.contentDelta.y) * scale.y * localScale.y)
                    )
                    LElement.render(gameController.mainRender)
                }
            }
            LayoutDirection.VERTICAL -> {
                var dy = 0f
                if (parent.alignCenterX) {
                    dy = (elements.size-1) * parent.spacing.y * scale.y * localScale.y / 2
                }
                for (LElement in if (!parent.reverseY) {elements} else {elements.reversed()}) {
                    i++
                    LElement.updateGridVisuals(
                        Vec2(pos.x + (parent.contentDelta.x) * scale.x * localScale.x
                        ,
                        pos.y + (parent.contentDelta.y + i * parent.spacing.y) * scale.y * localScale.y - dy)
                    )
                    LElement.render(gameController.mainRender)
                }
            }
        }
    }

    fun addChild(el: LElement, tag: String = "") : LElement {
        if (tag != "") {
            el.tag = tag
        }
        el.parentCanvas = this
        elements += el
        return  el
    }
    fun addChild(key: String, tag: String = "") : LElement {
        return gameController.loadUIList.addChilds(key, this, tag)
    }

    fun applyCanvasPosX(x: Float, align: Float = 0f): Float {
        return (x * scale.x * localScale.x + pos.x + align * wh.x/2)
    }
    fun applyCanvasPosY(y: Float, align: Float = 0f): Float {
        return (y * scale.y * localScale.y + pos.y + align * wh.y/2)
    }
    fun applyCanvasSizeX(w: Float): Float {
        return w * scale.x * localScale.x
    }
    fun applyCanvasSizeY(h: Float): Float {
        return h * scale.y * localScale.y
    }
    fun applyCanvasPos(x: Vec2, align: Vec2 = Vec2(0f)): Vec2 {
        return (x * scale * localScale + pos + align * wh/2f)
    }
    fun applyCanvasSize(w: Vec2): Vec2 {
        return w * scale * localScale
    }
    fun applyCanvasTextSize(s: Float): Float {
        return s * textScale
    }
    fun canvasSizePercentX(w: Float): Float {
        return wh.x*w/100f
    }
    fun canvasSizePercentY(h: Float): Float {
        return wh.y*h/100f
    }

    fun getAllElements(): ArrayList<ArrayList<LElement>> {
        val ret = ArrayList<ArrayList<LElement>>()

        fun addToLayer(index: Int, element: LElement) {
            while (ret.size <= index) {
                ret.add(ArrayList())
            }
            ret[index].add(element)
        }

        fun traverse(canvas: LCanvas, layer: Int) {
            for (element in canvas.elements) {
                addToLayer(layer, element)
                element.elementCanvas.let { childCanvas ->
                    traverse(childCanvas, layer + 1)
                }
            }
        }

        traverse(this, 0)
        return ret
    }

    fun destroy() {
        for (element in elements) {
            element.destroy()
        }
    }

    fun debugRender(mainRender: MainRender) {
        for (i in elements) {
            i.debugRender(mainRender)
        }
        mainRender.lg.fill(255f,0f)
        mainRender.lg.pg.stroke(0f,255f,0f)
        mainRender.lg.pg.strokeWeight(4f)
        mainRender.lg.setBlock(pos, wh*scale)
        mainRender.lg.pg.noStroke()
        mainRender.lg.fill(0f, 255f, 0f,255f)
        mainRender.lg.setTextAlign(-1, 1)
        mainRender.lg.setText("scale ${scale.x}, ${scale.y}\nlocalScale ${localScale.x}, ${localScale.y}\nwh ${wh.x}, ${wh.y}", pos-Vec2(wh.x, -wh.y)*scale/2f+Vec2(10f, -15f), 25f)
    }
}