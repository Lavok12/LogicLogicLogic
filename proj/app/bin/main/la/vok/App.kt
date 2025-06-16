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
        if (frameCount % 10 == 0) {
            initializeAll.updateResolution()
        }

        updateMouseCoordinates()
        Storage.gameController.tick()
        Storage.gameController.rendering()
        Storage.gameController.lCanvasController.UITick()
        Storage.gameController.gameTick()
    }

    fun updateMouseCoordinates() {
        Storage.mouseController.updateCoord(mouseX.toFloat(), mouseY.toFloat(), pmouseX.toFloat(), pmouseY.toFloat())
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
