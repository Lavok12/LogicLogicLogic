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
        Storage.gameController = GameController(Settings.isClient, Settings.isServer, Settings.isLocal);
        Storage.gameController.startInit(); 
        Storage.gameController.initLoaders();
        Storage.main.frameRate(60f)
        Storage.main.noiseSeed(Functions.rand(0, 255000000).toLong())
        Storage.gameController.initMenuController();
    }

    fun updateResolution() {
        Storage.gameController.disW = Storage.main.width.toFloat()
        Storage.gameController.disH = Storage.main.height.toFloat()
        Storage.gameController.disH2 = Storage.gameController.disH / 2
        Storage.gameController.disW2 = Storage.gameController.disW / 2
        Storage.gameController.fix = Storage.gameController.disW / 2000f

        Storage.lg.updateResolution()
        PApplet.println("Resolution updated", Storage.gameController.disW, Storage.gameController.disH)
    }
}