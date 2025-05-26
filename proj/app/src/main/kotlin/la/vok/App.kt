package la.vok

import processing.core.PApplet
import processing.core.PGraphics
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
    }

    fun updateMouseCoordinates() {
        Storage.moux = (mouseX - Storage.gameController.disW2) * (Storage.lg.disW / Storage.gameController.disW)
        Storage.mouy = (-mouseY + Storage.gameController.disH2) * (Storage.lg.disH / Storage.gameController.disH)
        Storage.pmoux = (pmouseX - Storage.gameController.disW2) * (Storage.lg.disW / Storage.gameController.disW)
        Storage.pmouy = (-pmouseY + Storage.gameController.disH2) * (Storage.lg.disH / Storage.gameController.disH)
    }
}
