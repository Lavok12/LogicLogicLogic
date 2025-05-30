package la.vok.GameController.TransferModel

import la.vok.GameController.*
import la.vok.GameController.Server.*
import processing.data.*
import la.vok.GameController.Content.Logic.LogicElement
import la.vok.GameController.Content.Logic.LogicWire

class ServerTransferModel(var serverController: ServerController) {
    var isLocal: Boolean = serverController.gameController.isLocal
    init {
        println("ServerTransferModel initialized")
    }

    val gameController: GameController
    get() {
        return serverController.gameController
    }
    val clientTransferModel: ClientTransferModel
    get() {
        return gameController.clientController.clientTransferModel
    }
    // input

    // output
    fun server_output_connect(id: String, name: String) {
        if (isLocal) {
            gameController.serverController.connectNewPlayer(id, name)
        }
    }
    fun server_output_getMap() {
        if (isLocal) {
            clientTransferModel.client_output_setMap()
        }
    }
}