package la.vok.GameController.Client

import la.vok.UI.MainRender
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
        logicMap.addElement(LogicElement(0f, 0f, "test", gameController))
    }

    fun getTransferModel(): TransferModel {
        return gameController.transferModel
    }

    fun start() {
        getTransferModel().getClientId()
        getTransferModel().getMap();
    }

    fun RenderLogicElements() {
        mainCamera.RenderLogicElements(clientController = this)
        mainCamera.RenderWires(clientController = this)
    }
}
