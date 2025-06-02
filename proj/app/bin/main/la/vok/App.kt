package la.vok

import processing.core.PApplet
import processing.core.PGraphics
import processing.event.MouseEvent
import processing.event.KeyEvent
import processing.opengl.PShader
import la.vok.Storages.Storage
import la.vok.LavokLibrary.LGraphics
import la.vok.LavokLibrary.Functions
import la.vok.Initialize.initializeAll


class App : PApplet() {
    override fun settings() {
        Storage.main = this
        size(1080, 720, P2D)
    }
    
    override fun setup() {
        println("JavaVersion: ${PApplet.javaVersionName}")
        initializeAll.initialize()
    }
    
    override fun draw() {
        if (frameCount % 60 == 0) {
            println("FPS: $frameRate")
        }
        
        updateMouseCoordinates()
        Storage.gameController.rendering()
        Storage.gameController.UITick()
        Storage.gameController.gameTick()
    }

    fun updateMouseCoordinates() {
        var moux = (mouseX - Storage.gameController.rendering.disW2) * (Storage.lg.disW / Storage.gameController.rendering.disW)
        var mouy = (-mouseY + Storage.gameController.rendering.disH2) * (Storage.lg.disH / Storage.gameController.rendering.disH)
        var pmoux = (pmouseX - Storage.gameController.rendering.disW2) * (Storage.lg.disW / Storage.gameController.rendering.disW)
        var pmouy = (-pmouseY + Storage.gameController.rendering.disH2) * (Storage.lg.disH / Storage.gameController.rendering.disH)

        Storage.mouseController.updateCoord(moux, mouy, pmoux, pmouy)
    }

    override fun mousePressed(e: MouseEvent) {
        Storage.mouseController.mousePressed(e)
    }
    override fun mouseReleased(e: MouseEvent) {
        Storage.mouseController.mouseReleased(e)
    }
    override fun mouseDragged(e: MouseEvent) {
        Storage.mouseController.mouseDragged(e)
    }
    override fun mouseClicked(e: MouseEvent) {
        Storage.mouseController.mouseClicked(e)
    }
    override fun mouseMoved(e: MouseEvent) {
        Storage.mouseController.mouseMoved(e)
    }
    override fun mouseWheel(e: MouseEvent) {
        Storage.mouseController.mouseWheel(e)
    }
    override fun keyPressed(event: KeyEvent) {
        Storage.gameController.keyTracker.keyPressed(event)
    }
    override fun keyReleased(event: KeyEvent) {
        Storage.gameController.keyTracker.keyReleased(event)
    }
    override fun keyTyped(event: KeyEvent) {
        
    }
}
