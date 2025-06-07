package la.vok.UI

import la.vok.GameController.GameController
import la.vok.Storages.Storage
import la.vok.UI.Elements.LElement

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
    var gameController: GameController
) {
    var elements = ArrayList<LElement>();
    var isActive = false

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

    fun tick(mx: Float, my: Float, gameController: GameController): Boolean {
        for (i in elements.size - 1 downTo 0) {
            if (elements[i].tick(mx, my, gameController)) {
                return true
            }
        }
        return false
    }
    
    fun renderElements() {
        for (LElement in elements) {
            LElement.render(gameController.mainRender)
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
}