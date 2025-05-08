package la.vok.Initialize;

import processing.core.PApplet
import la.vok.Storages.Storage
import la.vok.Storages.Settings
import la.vok.LavokLibrary.LGraphics
import la.vok.LavokLibrary.Functions
import la.vok.GameController.GameController

object initializeAll {
    fun initialize() {
        initializeGame()
        initializeGraphics()
    }

    fun initializeGraphics() {
        Storage.lg = LGraphics()
        Storage.main.noStroke()
        updateResolution()
    }

    fun initializeGame() {
        Functions.parent = Storage.main;
        Storage.main.frameRate(60f)
        Storage.main.noiseSeed(Functions.rand(0, 255000000).toLong())
        Storage.gameController = GameController(Settings.isClient, Settings.isServer, Settings.isLocal);
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