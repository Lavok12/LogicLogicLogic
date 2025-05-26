package la.vok.UI

import la.vok.Storages.Storage
import la.vok.LavokLibrary.LGraphics
import la.volk.UI.Elements.*
import java.awt.Button
import java.awt.Color
import la.vok.GameController.GameController
import la.vok.GameController.Client.ClientController
import la.vok.GameController.Client.Camera

class Rendering(var gameController: GameController) {
    lateinit var lg: LGraphics
    var LScene: LScene? = null;
    val camera: Camera
    get() {
        return gameController.clientController.mainCamera
    }

    fun setScene(scene: LScene) {
        scene.checkLoaded()
        scene.addTagsToCanvas();
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
    fun RenderLogicElements(clientController: ClientController) {
        for (element in clientController.logicMap.list()) {
            element.render(camera, this)
        }
    }

    fun RenderWires(clientController: ClientController) {
        for (wire in clientController.logicMap.wires) {
            wire.render(camera, this)
        }
    }
}