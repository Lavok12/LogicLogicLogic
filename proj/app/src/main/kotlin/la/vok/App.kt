package la.vok

import processing.core.PApplet
import la.vok.Storages.Storage
import la.vok.LavokLibrary.LGraphics
import la.vok.LavokLibrary.Functions

class App : PApplet() {
    override fun settings() {
        Storage.main = this
        size(1080, 720, P2D)
    }
    override fun setup() {
        initializeGraphics();
    }
    override fun draw() {
        updateMouseCoordinates()
    }

    fun initializeGraphics() {
        Storage.lg = LGraphics()
        Functions.parent = this;
        Storage.main.noStroke()
        Storage.main.frameRate(60f)
        updateResolution()
        Storage.main.noiseSeed(Functions.rand(0, 255000000).toLong())
    }

    fun updateMouseCoordinates() {
        Storage.moux = (mouseX - Storage.disW2) * (Storage.lg.disW / Storage.disW)
        Storage.mouy = (-mouseY + Storage.disH2) * (Storage.lg.disH / Storage.disH)
        Storage.pmoux = (pmouseX - Storage.disW2) * (Storage.lg.disW / Storage.disW)
        Storage.pmouy = (-pmouseY + Storage.disH2) * (Storage.lg.disH / Storage.disH)
    }

    fun updateResolution() {
        Storage.disW = Storage.main.width.toFloat()
        Storage.disH = Storage.main.height.toFloat()
        Storage.disH2 = Storage.disH / 2
        Storage.disW2 = Storage.disW / 2
        Storage.fix = Storage.disW / 2000f

        Storage.lg.updateResolution()
        PApplet.println("Resolution updated", Storage.disW, Storage.disH)
    }
}
