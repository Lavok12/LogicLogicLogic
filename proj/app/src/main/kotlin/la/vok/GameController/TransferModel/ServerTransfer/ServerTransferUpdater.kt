package la.vok.GameController.TransferModel

import la.vok.GameController.Content.PlayerData
import processing.data.JSONObject
import processing.data.JSONArray

class ServerTransferUpdater(var serverTransferModel: ServerTransferModel) : TransfeUpdater {
    var transferBuffer = TransferBuffer(this)
    var serverController = serverTransferModel.serverController
    var gameController = serverController.gameController

    private val handlers = mapOf(
        "loadState_connect_client" to SERVER_loadState_connect_client(),
        "loadState_getPos_client" to SERVER_loadState_getPos_client(),
        "loadState_loadMap_client" to SERVER_loadState_loadMap_client(),
        "update_player_data" to SERVER_update_player_data(),
        "add_logicElement" to SERVER_add_logicElement(),
        "chat_message" to SERVER_chat_message(),
        "ping" to SERVER_ping(),
        "pong" to SERVER_pong()
    )

    init {
        println("       ServerTransferUpdater initialized")
    }

    override fun processingData(transferPackage: TransferPackage) {
        if (transferPackage.recipient != TransferPackage.SERVER) return

        /*if (transferPackage.header != "loadState_connect_client" && serverController.connectsContainer.contains(transferPackage.sender)) {
            serverController.disconnectPlayer(transferPackage.sender)
        }*/
        val handler = handlers[transferPackage.header]
        if (handler != null) {
            handler.handle(transferPackage.data, transferPackage.sender, this)
        } else {
            println("${transferPackage.header} not found")
        }
    }
}
