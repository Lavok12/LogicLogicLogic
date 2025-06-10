package la.vok.GameController.TransferModel

import la.vok.GameController.*
import la.vok.GameController.Server.*
import processing.data.*
import la.vok.GameController.Content.Logic.LogicElement
import la.vok.GameController.Content.Logic.LogicWire

class ServerTransferModel(var serverController: ServerController): TransferModel {
    init {
        println("   ServerTransferModel initialized")
    }

    var isLocal: Boolean = serverController.gameController.isLocal
    var serverTransferUpdater = ServerTransferUpdater(this)

    val gameController: GameController
    get() {
        return serverController.gameController
    }
    val clientTransferModel: ClientTransferModel
    get() {
        return gameController.clientController.clientTransferModel
    }
    // input
    override fun sendData(transferPackage: TransferPackage) {
        if (isLocal) {
            clientTransferModel.getData(transferPackage.copy())
        } else {
            serverController.onlineWebSocketServer.broadcast(transferPackage.toString())
        }
    }
    // output
    override fun getData(transferPackage: TransferPackage) {
        serverTransferUpdater.processingData(transferPackage)
    }
}