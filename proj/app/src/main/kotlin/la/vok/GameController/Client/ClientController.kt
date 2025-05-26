package la.vok.GameController.Client

import la.vok.UI.Rendering
import la.vok.GameController.GameController;
import la.vok.GameController.TransferModel.TransferModel
import la.vok.GameController.Content.Logic.LogicElement
import la.vok.UI.LCanvas
import la.vok.Storages.Storage
import la.vok.GameController.Content.Map.LogicMap

class ClientController(var gameController: GameController) {
    var logicMap = LogicMap(gameController)
    var mainCamera: Camera
    var clientId: String = ""
    
    init {
        mainCamera = Camera(0f, 0f, 1f)
        println("ClientController initialized")
    }

    fun standart() {
        logicMap.addElement(LogicElement(-200f, -400f, "test", gameController))
        logicMap.addElement(LogicElement(200f, -400f, "test", gameController))
        logicMap.addElement(LogicElement(0f, 400f, "test", gameController))
        logicMap.addWire(logicMap.list()[0], logicMap.list()[2])
        logicMap.addWire(logicMap.list()[1], logicMap.list()[2])
        logicMap.updateWireSet()
    }

    fun getTransferModel(): TransferModel {
        return gameController.transferModel
    }

    fun start() {
        getTransferModel().getClientId()
        getTransferModel().getMap();
    }

    fun RenderLogicElements() {
        gameController.rendering.RenderLogicElements(clientController = this)
        gameController.rendering.RenderWires(clientController = this)
    }
}
