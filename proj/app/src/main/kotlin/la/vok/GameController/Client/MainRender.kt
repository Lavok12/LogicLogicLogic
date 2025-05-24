package la.vok.UI

import la.vok.Storages.Storage
import la.vok.LavokLibrary.LGraphics
import la.volk.UI.Elements.*
import java.awt.Button
import java.awt.Color
import la.vok.GameController.GameController
import la.vok.GameController.Client.ClientController

class MainRender(var gameController: GameController) {
    lateinit var lg: LGraphics
    var LScene: LScene? = null;

    fun setScene(sceneName: String) {
        val scene = gameController.scenesLoader.getScene(sceneName)
        scene.loadScene()
        LScene = scene
    }
    fun continueScene(sceneName: String) {
        val scene = gameController.scenesLoader.getScene(sceneName)
        scene.checkLoaded()
        LScene = scene
    }
    fun setScene(scene: LScene) {
        scene.loadScene()
        LScene = scene
    }
    fun continueScene(scene: LScene) {
        scene.checkLoaded()
        LScene = scene
    }

    fun startRender() {
        Storage.lg.beginDraw();
    }
    fun endRender() {
        Storage.lg.endDraw();
    }
    fun clearLg() {
        Storage.lg.pg.background(0);
        lg.pg.noStroke();
        Storage.lg = Storage.lg;
    }
    fun clearDisplay() {
        Storage.main.background(0);
    }
    fun updateDistaplay() {
        clearDisplay();
        Storage.main.image(Storage.lg.pg, 0f, 0f, Storage.main.width.toFloat(), Storage.main.height.toFloat());
    }

    fun render() {
        lg = Storage.lg;
        startRender();
        clearLg();
        if (gameController.gameStarted) {
            if (gameController.isClient) {
                gameController.clientController.RenderLogicElements()
            }
        }

        gameController.getCanvas().width = Storage.lg.disW
        gameController.getCanvas().height = Storage.lg.disH
        gameController.getCanvas().renderElements();
        updateDistaplay()
        endRender()
        
    }
}