package la.vok.UI

import la.vok.GameController.GameController
import la.vok.Storages.Storage
import la.volk.UI.Elements.LElement

class LCanvas (
    var posX: Float = 0f,
    var posY: Float = 0f,
    var width: Float = 100f,
    var height: Float = 100f,
    var scaleX: Float = 1f,
    var scaleY: Float = 1f,
    var textScale: Float = 1f,
    var gameController: GameController = Storage.gameController
    
) {
    var elements = ArrayList<LElement>();

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

    fun tick(mx: Float, my: Float, mouseButton: Int): Boolean {
        for (i in elements.size - 1 downTo 0) {
            if (elements[i].tick(mx, my, mouseButton)) {
                return true
            }
        }
        return false
    }
    
    fun renderElements() {
        for (LElement in elements) {
            LElement.render(gameController.rendering)
        }
    }

    fun addChild(el: LElement, tag: String = "") {
        if (tag != "") {
            el.tag = tag;
        }
        el.parentCanvas = this;
        elements += el;
    }
    fun addChild(key: String, tag: String = "") {
        gameController.loadUIList.addChilds(key, this, tag)
    }

    fun applyCanvasPosX(x: Float, align: Float = 0f): Float {
        return (x + posX + align * width/2) / scaleX
    }
    fun applyCanvasPosY(y: Float, align: Float = 0f): Float {
        return (y + posY + align * height/2) / scaleY
    }
    fun applyCanvasSizeX(w: Float): Float {
        return w / scaleX
    }
    fun applyCanvasSizeY(h: Float): Float {
        return h / scaleY
    }
    fun applyCanvasTextSize(s: Float): Float {
        return s / textScale
    }
    fun canvasSizePercentX(w: Float): Float {
        return width*w
    }
    fun canvasSizePercentY(h: Float): Float {
        return height*h
    }
}