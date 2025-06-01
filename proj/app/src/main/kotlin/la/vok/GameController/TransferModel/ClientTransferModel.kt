package la.vok.GameController.TransferModel

import la.vok.GameController.*
import la.vok.GameController.Client.*
import processing.data.*
import la.vok.GameController.Content.Logic.LogicElement
import la.vok.GameController.Content.Logic.LogicWire

class ClientTransferModel(var clientController: ClientController) : TransferModel {
    init {
        println("   ClientTransferModel initialized")
    }
    var isLocal: Boolean = clientController.gameController.isLocal
    var clientTransferUpdater = ClientTransferUpdater(this)

    val gameController: GameController
    get() {
        return clientController.gameController
    }
    val serverTransferModel: ServerTransferModel
    get() {
        return gameController.serverController.serverTransferModel
    }
    
    // input
    override fun sendData(transferPackage: TransferPackage) {
        if (isLocal) {
            serverTransferModel.getData(transferPackage.copy())
        } else {
            clientController.onlineWebSocketClient.send(transferPackage.toString())
        }
    }

    // output
    override fun getData(transferPackage: TransferPackage) {
        clientTransferUpdater.processingData(transferPackage)
    }
}