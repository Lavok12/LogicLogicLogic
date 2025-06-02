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
        Storage.gameController.initScenes();
        Storage.main.frameRate(60f)
        Storage.main.noiseSeed(Functions.rand(0, 255000000).toLong())
        Storage.gameController.initMenu();
    }

    fun updateResolution() {
        Storage.gameController.rendering.disW = Storage.main.width.toFloat()
        Storage.gameController.rendering.disH = Storage.main.height.toFloat()
        Storage.gameController.rendering.disH2 = Storage.gameController.rendering.disH / 2
        Storage.gameController.rendering.disW2 = Storage.gameController.rendering.disW / 2
        Storage.gameController.rendering.fix = Storage.gameController.rendering.disW / 2000f

        Storage.lg.updateResolution()
        PApplet.println("Resolution updated", Storage.gameController.rendering.disW, Storage.gameController.rendering.disH)
    }
}