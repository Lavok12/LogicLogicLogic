package la.vok

import processing.core.PApplet
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
        initializeAll.initialize();
    }
    
    override fun draw() {
        updateMouseCoordinates()
    }

    fun updateMouseCoordinates() {
        Storage.moux = (mouseX - Storage.disW2) * (Storage.lg.disW / Storage.disW)
        Storage.mouy = (-mouseY + Storage.disH2) * (Storage.lg.disH / Storage.disH)
        Storage.pmoux = (pmouseX - Storage.disW2) * (Storage.lg.disW / Storage.disW)
        Storage.pmouy = (-pmouseY + Storage.disH2) * (Storage.lg.disH / Storage.disH)
    }
}
