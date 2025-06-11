package la.vok.UI.Canvas

import la.vok.GameController.GameController
import la.vok.Storages.Storage
import la.vok.LavokLibrary.*
import la.vok.UI.Elements.LElement
import la.vok.UI.*
import la.vok.UI.Elements.LayoutDirection
import la.vok.UI.Scenes.*
import javax.swing.text.Element

class LCanvas (
    var posX: Float = 0f,
    var posY: Float = 0f,
    var width: Float = 100f,
    var height: Float = 100f,
    var scaleX: Float = 1f,
    var scaleY: Float = 1f,
    var localScaleX: Float = 1f,
    var localScaleY: Float = 1f,
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
        if (!Functions.tap(posX, posY, width, height, mx, my)) {
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
        gameController.lCanvasController.remove(this)
        for (LElement in elements) {
            LElement.deactivate()
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
                    dx = (elements.size-1) * parent.spacingX * scaleX * localScaleX / 2
                }
                for (LElement in if (!parent.reverseX) {elements} else {elements.reversed()}) {
                    i++
                    LElement.updateGridVisuals(
                        posX + (parent.contentDeltaX + i * parent.spacingX) * scaleX * localScaleX - dx
                        ,
                        posY + (parent.contentDeltaY) * scaleX * localScaleX
                    )
                    LElement.render(gameController.mainRender)
                }
            }
            LayoutDirection.VERTICAL -> {
                var dy = 0f
                if (parent.alignCenterX) {
                    dy = (elements.size-1) * parent.spacingY * scaleY * localScaleY / 2
                }
                for (LElement in if (!parent.reverseY) {elements} else {elements.reversed()}) {
                    i++
                    LElement.updateGridVisuals(
                        posX + (parent.contentDeltaX) * scaleX * localScaleX
                        ,
                        posY + (parent.contentDeltaY + i * parent.spacingY) * scaleX * localScaleX - dy
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
        return (x * scaleX * localScaleX + posX + align * width/2)
    }
    fun applyCanvasPosY(y: Float, align: Float = 0f): Float {
        return (y * scaleY * localScaleY + posY + align * height/2)
    }
    fun applyCanvasSizeX(w: Float): Float {
        return w * scaleX * localScaleX
    }
    fun applyCanvasSizeY(h: Float): Float {
        return h * scaleY * localScaleY
    }
    fun applyCanvasTextSize(s: Float): Float {
        return s * textScale
    }
    fun canvasSizePercentX(w: Float): Float {
        return width*w/100f
    }
    fun canvasSizePercentY(h: Float): Float {
        return height*h/100f
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

}