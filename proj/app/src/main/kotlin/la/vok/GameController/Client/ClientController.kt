package la.vok.GameController.Client

import la.vok.UI.Rendering
import la.vok.GameController.GameController;
import la.vok.GameController.Content.Logic.LogicElement
import la.vok.UI.LCanvas
import la.vok.Storages.Storage
import la.vok.GameController.Content.Map.LogicMap
import la.vok.LavokLibrary.Functions
import la.vok.GameController.TransferModel.*

class ClientController(var gameController: GameController) {
    var logicMap = LogicMap(gameController)
    var mainCamera: Camera
    var clientId: String = Functions.getUniqueDeviceId()
    var name: String = "Name1234"
    var clientTransferModel: ClientTransferModel

    init {
        println("ClientController initialized")
        clientTransferModel = ClientTransferModel(this)
        mainCamera = Camera(0f, 0f, 1f)
    }

    fun standart() {
        start()
        logicMap.updateWireSet()
    }


    fun start() {
        clientTransferModel.client_input_connect(clientId, name)
        clientTransferModel.client_input_getMap()
    }

    fun RenderLogicElements() {
        gameController.rendering.RenderLogicElements(clientController = this)
        gameController.rendering.RenderWires(clientController = this)
    }
}
