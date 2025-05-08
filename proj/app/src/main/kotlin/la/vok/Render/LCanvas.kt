package la.vok.Render

class LCanvas(
    var posX: Float = 0f,
    var posY: Float = 0f,
    var width: Float = 100f,
    var height: Float = 100f,
    var scaleX: Float = 1f,
    var scaleY: Float = 1f,
    var textScale: Float = 1f,
) {
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