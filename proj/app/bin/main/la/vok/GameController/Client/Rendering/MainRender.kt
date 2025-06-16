package la.vok.UI

import la.vok.Storages.Storage
import la.vok.Storages.Settings
import la.vok.LavokLibrary.LGraphics
import la.vok.UI.Elements.*
import java.awt.Button
import java.awt.Color
import la.vok.GameController.GameController
import la.vok.GameController.Client.ClientController
import la.vok.GameController.Client.Camera
import la.vok.GameController.Content.*
import la.vok.GameController.ClientState
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.UI.Canvas.*
import la.vok.UI.Scenes.*


class MainRender(var gameController: GameController) {
    var disH: Float = 0f
    var disW: Float = 0f
    var disH2: Float = 0f
    var disW2: Float = 0f
    var fix: Float = 0f

    lateinit var lg: LGraphics
    var LScene: LScene? = null;
    val camera: Camera
    get() {
        return gameController.clientController.mainCamera
    } 

    fun setScene(scene: LScene) {
        if (LScene != null) {
            gameController.lCanvasController.remove(LScene!!.canvas)
        }
        scene.checkLoaded()
        scene.addTagsToCanvas();
        LScene = scene
            gameController.lCanvasController.add(LScene!!.canvas)
    }
    
    fun continueScene(scene: LScene) {
        if (LScene != null) {
            gameController.lCanvasController.remove(LScene!!.canvas)
        }
        scene.checkLoaded()
        LScene = scene
        gameController.lCanvasController.add(LScene!!.canvas)
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
                if (gameController.clientState == ClientState.STARTED && gameController.clientController.connect_success) {
                    gameController.clientController.renderUpdate(gameController.renderBuffer)

                    gameController.renderBuffer.updateVisualAll()
                    gameController.renderBuffer.renderA()
                    gameController.renderBuffer.renderB()
                    gameController.renderBuffer.renderC()
                    gameController.renderBuffer.clearA()
                    gameController.renderBuffer.clearB()
                    gameController.renderBuffer.clearC()
                }
            }
        }
        
        gameController.getCanvas().wh = Vec2(Storage.lg.disW, Storage.lg.disH)

        gameController.lCanvasController.canvasListRender()
        updateDistaplay()
        endRender()

    }
}