package la.vok.LavokLibrary

import la.vok.App
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.Storages.Storage
import processing.core.*
import processing.opengl.PGraphicsOpenGL
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

class LGraphics {
    lateinit var pg: PGraphics
    var parent: App

    var windowWidth: Float = 0f
    var windowHeight: Float = 0f

    var disH = 0f
    var disW = 0f
    var disH2 = 0f
    var disW2 = 0f

    var M = 0f

    fun checkResolution() {
        if (windowWidth != Storage.gameController.mainRender.disW || windowHeight != Storage.gameController.mainRender.disH) {
            updateResolution()
        }
    }

    fun updateResolution() {
        windowWidth = Storage.gameController.mainRender.disW
        windowHeight = Storage.gameController.mainRender.disH
        PApplet.println(
            "Resolution updated",
            Storage.gameController.mainRender.disW,
            Storage.gameController.mainRender.disH
        )
        pg = parent.createGraphics(
            round(Storage.gameController.mainRender.disW).toInt(),
            round(Storage.gameController.mainRender.disH).toInt(),
            PApplet.P2D
        )
        (pg as PGraphicsOpenGL).textureSampling(3)
        disW = 2000f
        disH = Storage.gameController.mainRender.disH / Storage.gameController.mainRender.fix

        disW2 = disW / 2
        disH2 = disH / 2

        M = Storage.gameController.mainRender.fix

    }

    init {
        parent = Storage.main
        updateResolution()
    }


    fun setTextAlign(x: Int, y: Int) {
        when {
            x == 0 && y == 0 -> pg.textAlign(PApplet.CENTER, PApplet.CENTER)
            x == -1 && y == 0 -> pg.textAlign(PApplet.LEFT, PApplet.CENTER)
            x == 1 && y == 0 -> pg.textAlign(PApplet.RIGHT, PApplet.CENTER)
            x == 0 && y == -1 -> pg.textAlign(PApplet.CENTER, PApplet.LEFT)
            x == -1 && y == -1 -> pg.textAlign(PApplet.LEFT, PApplet.LEFT)
            x == 1 && y == -1 -> pg.textAlign(PApplet.RIGHT, PApplet.LEFT)
            x == 0 && y == 1 -> pg.textAlign(PApplet.CENTER, PApplet.RIGHT)
            x == -1 && y == 1 -> pg.textAlign(PApplet.LEFT, PApplet.RIGHT)
            x == 1 && y == 1 -> pg.textAlign(PApplet.RIGHT, PApplet.RIGHT)
        }
    }

    fun setText(txt: String, xPos: Float, yPos: Float, size: Float) {
        var lsize = size
        lsize = max(1f, lsize)
        lsize = min(100000f, lsize)
        pg.textSize(lsize * M)
        pg.text(txt, (disW2 + xPos) * M, (disH2 - yPos) * M)
    }
    fun setTextWH(txt: String, xPos: Float, yPos: Float, size: Float, w: Float, h: Float) {
        pg.textSize(size * M)
        pg.text(txt, (disW2 + xPos) * M, (disH2 - yPos), w * M, h * M)
    }

    fun getTextWidth(txt: String, size: Float): Float {
        val lsize = size.coerceIn(1f, 100000f)
        pg.textSize(lsize * M)
        return pg.textWidth(txt) / M * 1.015f
    }

    fun fill(red: Float) {
        pg.noFill()
        pg.fill(red)
    }

    fun fill(red: Float, a: Float) {
        pg.noFill()
        pg.fill(red, a)
    }

    fun fill(red: Float, green: Float, blue: Float) {
        pg.noFill()
        pg.fill(red, green, blue)
    }

    fun fill(red: Float, green: Float, blue: Float, alpha: Float) {
        pg.noFill()
        pg.fill(red, green, blue, alpha)
    }

    fun bg(red: Float) {
        pg.background(red)
    }

    fun bg(red: Float, green: Float, blue: Float) {
        pg.background(red, green, blue)
    }

    fun setTint(n1: Float, n2: Float, n3: Float, n4: Float) {
        pg.tint(n1, n2, n3, n4)
    }

    fun setTint(n1: Float, n2: Float, n3: Float) {
        pg.tint(n1, n2, n3)
    }

    fun setTint(n1: Float, n2: Float) {
        pg.tint(n1, n2)
    }

    fun setTint(n1: Float) {
        pg.tint(n1)
    }

    fun noTint() {
        pg.tint(255f, 255f, 255f, 255f)
    }

    fun rect(xPos: Float, yPos: Float, xSize: Float, ySize: Float) {
        pg.rect(xPos * M, yPos * M, xSize * M, ySize * M)
    }

    fun setBlock(xPos: Float, yPos: Float, xSize: Float, ySize: Float) {
        pg.rect((disW2 + xPos - xSize / 2) * M, (disH2 - yPos - ySize / 2) * M, xSize * M, ySize * M)
    }

    fun setBlock(xPos: Float, yPos: Float, xSize: Float, ySize: Float, k: Float) {
        pg.rect((disW2 + xPos - xSize / 2) * M, (disH2 - yPos - ySize / 2) * M, xSize * M, ySize * M, k * M)
    }

    fun setPolygon(angles: Array<FloatArray>, xPos: Float, yPos: Float, mpx: Float, mpy: Float) {
        pg.beginShape()
        for (angle in angles) {
            val x = angle[0]
            val y = angle[1]
            pg.vertex((disW2 + x * mpx + xPos) * M, (disH2 - y * mpy - yPos) * M)
        }
        pg.endShape(PApplet.CLOSE)
    }

    fun setRotateBlock(xPos: Float, yPos: Float, xSize: Float, ySize: Float, Rotate: Float) {
        pg.pushMatrix()
        pg.translate((disW2 + xPos) * M, (disH2 - yPos) * M)
        pg.rotate(Rotate)
        setBlock(-disW2, disH2, xSize, ySize)
        pg.popMatrix()
    }

    fun setEps(xPos: Float, yPos: Float, xSize: Float, ySize: Float) {
        pg.ellipse((disW2 + xPos) * M, (disH2 - yPos) * M, xSize * M, ySize * M)
    }

    fun setRotateEps(xPos: Float, yPos: Float, xSize: Float, ySize: Float, Rotate: Float) {
        pg.pushMatrix()
        pg.translate((disW2 + xPos) * M, (disH2 - yPos) * M)
        pg.rotate(Rotate)
        setEps(-disW2, disH2, xSize, ySize)
        pg.popMatrix()
    }

    fun setLine(xPos: Float, yPos: Float, xPos2: Float, yPos2: Float) {
        pg.line((disW2 + xPos) * M, (disH2 - yPos) * M, (disW2 + xPos2) * M, (disH2 - yPos2) * M)
    }

    fun setLine(xPos: Float, yPos: Float, xPos2: Float, yPos2: Float, w: Float, r: Float, g: Float, b: Float) {
        pg.stroke(r, g, b)
        pg.strokeWeight(w * M)
        pg.line((disW2 + xPos) * M, (disH2 - yPos) * M, (disW2 + xPos2) * M, (disH2 - yPos2) * M)
        pg.noStroke()
    }

    fun setImage(fImage: PImage, xPos: Float, yPos: Float, xSize: Float, ySize: Float) {
        pg.image(fImage, (disW2 + xPos - xSize / 2) * M, (disH2 - yPos - ySize / 2) * M, xSize * M, ySize * M)
    }

    fun setImage(fImage: PImage, xPos: Float, yPos: Float, xSize: Float) {
        val ySize = xSize / fImage.width * fImage.height
        pg.image(fImage, (disW2 + xPos - xSize / 2) * M, (disH2 - yPos - ySize / 2) * M, xSize * M, ySize * M)
    }

    fun setRotateImage(fImage: PImage, xPos: Float, yPos: Float, xSize: Float, ySize: Float, Rotate: Float) {
        pg.pushMatrix()
        pg.translate((disW2 + xPos) * M, (disH2 - yPos) * M)
        pg.rotate(Rotate)
        setImage(fImage, -disW2, disH2, xSize, ySize)
        pg.popMatrix()
    }

    fun beginDraw() {
        pg.beginDraw()
    }

    fun endDraw() {
        pg.endDraw()
    }

    fun getPI(): PImage {
        return pg
    }

    fun setBlock(pos: Vec2, size: Vec2) =
        setBlock(pos.x, pos.y, size.x, size.y)

    fun setBlock(pos: Vec2, size: Vec2, radius: Float) =
        setBlock(pos.x, pos.y, size.x, size.y, radius)
    fun setEps(pos: Vec2, size: Vec2) =
        setEps(pos.x, pos.y, size.x, size.y)

    fun setRotateEps(pos: Vec2, size: Vec2, angle: Float) =
        setRotateEps(pos.x, pos.y, size.x, size.y, angle)
    fun setLine(from: Vec2, to: Vec2) =
        setLine(from.x, from.y, to.x, to.y)

    fun setLine(from: Vec2, to: Vec2, w: Float, r: Float, g: Float, b: Float) =
        setLine(from.x, from.y, to.x, to.y, w, r, g, b)
    fun setImage(image: PImage, pos: Vec2, size: Vec2) =
        setImage(image, pos.x, pos.y, size.x, size.y)

    fun setImage(image: PImage, pos: Vec2, xSize: Float) =
        setImage(image, pos.x, pos.y, xSize)
    fun setRotateImage(image: PImage, pos: Vec2, size: Vec2, angle: Float) =
        setRotateImage(image, pos.x, pos.y, size.x, size.y, angle)
    fun setText(txt: String, pos: Vec2, size: Float) =
        setText(txt, pos.x, pos.y, size)

    fun setTextWH(txt: String, pos: Vec2, size: Float, box: Vec2) =
        setTextWH(txt, pos.x, pos.y, size, box.x, box.y)


}
