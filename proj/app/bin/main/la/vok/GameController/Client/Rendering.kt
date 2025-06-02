package la.vok.UI

import la.vok.Storages.Storage
import la.vok.Storages.Settings
import la.vok.LavokLibrary.LGraphics
import la.volk.UI.Elements.*
import java.awt.Button
import java.awt.Color
import la.vok.GameController.GameController
import la.vok.GameController.Client.ClientController
import la.vok.GameController.Client.Camera
import la.vok.GameController.Client.LoadState
import la.vok.GameController.Content.*
import la.vok.GameController.ClientState
import la.vok.UI.LCanvas

class Rendering(var gameController: GameController) {
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

    var canvasRenderList = ArrayList<ArrayList<LCanvas>>()
    init {
        for (i in 0..Settings.canvasRenderLayers) {
            canvasRenderList += ArrayList<LCanvas>()
        }
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
                if (gameController.clientState == ClientState.STARTED && gameController.clientController.loadState == LoadState.STARTED) {
                    gameController.clientController.RenderLogicElements()
                    gameController.clientController.renderPlayer(gameController.clientController.player)
                }
            }
        }

        gameController.getCanvas().width = Storage.lg.disW
        gameController.getCanvas().height = Storage.lg.disH

        canvasRender(5, gameController.getCanvas())

        canvasListRender()
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

    fun renderPlayer(player: PlayerData,  clientController: ClientController) {
        if (player.isActive) {
            player.updateVisual(camera)
            lg.fill(255f)
            lg.setEps(player.VX, player.VY, player.VZ * 40f, player.VZ * 80f)

            canvasRender(2, player.canvas)
        }
    }

    fun canvasRender(layer: Int, canvas: LCanvas) {
        canvasRenderList[layer] += canvas
    }

    fun canvasListRender() {
        for (j in canvasRenderList) {
            for (i in j) {
                i.renderElements()
            }
            j.clear()
        }
    }
}