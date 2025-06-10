package la.vok.GameController.TransferModel

import java.*
import processing.data.JSONObject
import la.vok.GameController.Content.Logic.*
import la.vok.GameController.Content.PlayersContainer
import la.vok.GameController.Client.LoadState
import la.vok.Storages.Storage

class ClientTransferUpdater(var clientTransferModel: ClientTransferModel) : TransfeUpdater {
    var transferBuffer = TransferBuffer(this)
    var clientController = clientTransferModel.clientController
    var gameController = clientController.gameController

    private val handlers = mapOf(
        "loadState_connect_server" to CLIENT_loadState_connect_server(),
        "loadState_getPos_server" to CLIENT_loadState_getPos_server(),
        "loadState_loadMap_server" to CLIENT_loadState_loadMap_server(),
        "disconnect" to CLIENT_disconnect(),
        "players_data_update" to CLIENT_players_data_update(),
        "new_message" to CLIENT_new_message(),
        "ping" to CLIENT_ping(),
        "pong" to CLIENT_pong(),
    )

    init {
        println("       ClientTransferUpdater initialized")
    }

    override fun processingData(transferPackage: TransferPackage) {
        if (transferPackage.recipient != clientController.clientId && transferPackage.recipient != TransferPackage.ALL) {
            return
        }

        val handler = handlers[transferPackage.header]
        if (handler != null) {
            handler.handle(transferPackage.data, this)
        } else {
            println("${transferPackage.header} not found")
        }
    }
}
