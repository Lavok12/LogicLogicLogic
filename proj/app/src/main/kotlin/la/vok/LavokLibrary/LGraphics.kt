package la.vok.LavokLibrary

import la.vok.App
import la.vok.Storages.Storage
import processing.core.*
import processing.opengl.PGraphicsOpenGL
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

class LGraphics {
    lateinit var pg: PGraphics
    var parent: App

    var disH = 0f
    var disW = 0f
    var disH2 = 0f
    var disW2 = 0f

    var M = 0f

    fun updateResolution() {
        if (disW != Storage.gameController.rendering.disW || disH != Storage.gameController.rendering.disH) {
            pg = parent.createGraphics(round(Storage.gameController.rendering.disW).toInt(), round(Storage.gameController.rendering.disH).toInt(), PApplet.P2D)
            (pg as PGraphicsOpenGL).textureSampling(3)
            disW = 2000f
            disH = Storage.gameController.rendering.disH / Storage.gameController.rendering.fix

            disW2 = disW / 2
            disH2 = disH / 2

            M = Storage.gameController.rendering.fix
        }
    }

    init {
        parent = Storage.main
        updateResolution()
    }

    fun setText(txt: String, xPos: Float, yPos: Float, size: Float) {
        var lsize = size
        lsize = max(1f, lsize)
        lsize = min(100000f, lsize)
        pg.textSize(lsize * M)
        pg.text(txt, (disW2 + xPos) * M, (disH2 - yPos) * M)
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

    fun setTextWH(txt: String, xPos: Float, yPos: Float, size: Float, w: Float, h: Float) {
        pg.textSize(size * M)
        pg.text(txt, (disW2 + xPos) * M, (disH2 - yPos), w * M, h * M)
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
}
