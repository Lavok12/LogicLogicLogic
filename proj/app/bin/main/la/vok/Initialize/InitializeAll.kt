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
        Storage.gameController = GameController();
        Storage.gameController.initCoreComponents();
        Storage.gameController.initLoaders();
        Storage.gameController.initScenes();
        Storage.gameController.scriptsPreCompilate()
        Storage.main.frameRate(60f)
        Storage.main.noiseSeed(Functions.rand(0, 255000000).toLong())
        Storage.gameController.initMenu();
    }

    fun updateResolution() {
        Storage.gameController.mainRender.disW = Storage.main.width.toFloat()
        Storage.gameController.mainRender.disH = Storage.main.height.toFloat()
        Storage.gameController.mainRender.disH2 = Storage.gameController.mainRender.disH / 2
        Storage.gameController.mainRender.disW2 = Storage.gameController.mainRender.disW / 2
        Storage.gameController.mainRender.fix = Storage.gameController.mainRender.disW / 2000f

        Storage.lg.checkResolution()
    }
}