package la.vok.GameController.TransferModel

import la.vok.GameController.Content.PlayerData
import processing.data.JSONObject

class ServerTransferUpdater(var severTransferModel: ServerTransferModel) : TransfeUpdater {
    var serverController = severTransferModel.serverController
    var gameController = serverController.gameController
    
    init {
        println("       ServerTransferUpdater initialized")
    }

    override fun processingData(transferPackage: TransferPackage) {
        if (transferPackage.recipient != TransferPackage.SERVER) {
            return
        }
        when(transferPackage.header) {
            "connect_new_player" -> {
                serverController.connectNewPlayer(transferPackage.sender, transferPackage.data.getString("name"))
            }
            "update_player_data" -> {
                var id = transferPackage.data.getString("id", "")
                if (serverController.playersContainer.checkData(id)) {
                    var player = serverController.playersContainer.getData(id)!!
                    var dataP = PlayerData.fromJsonObject(transferPackage.data, gameController)
                    player.PX = dataP.PX
                    player.name = dataP.name
                    player.PY = dataP.PY
                    player.updateTime()
                } else {
                    TransferPackage("disconnect", TransferPackage.SERVER, id, JSONObject()).send(severTransferModel)
                }
            }
        }
    }
}