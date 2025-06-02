package la.vok.GameController.TransferModel

import la.vok.GameController.Content.PlayerData
import processing.data.JSONObject
import processing.data.JSONArray

class ServerTransferUpdater(var serverTransferModel: ServerTransferModel) : TransfeUpdater {
    var serverController = serverTransferModel.serverController
    var gameController = serverController.gameController
    
    init {
        println("       ServerTransferUpdater initialized")
    }

    override fun processingData(transferPackage: TransferPackage) {
        if (transferPackage.recipient != TransferPackage.SERVER) {
            return
        }
        var id = transferPackage.data.getString("clientId", "")
        when(transferPackage.header) {
            "loadState_connect_client" -> {
                serverController.connectNewPlayer(transferPackage.sender, transferPackage.data.getString("name"))
            }
            "loadState_getPos_client" -> {
                var player = serverController.playersContainer.getData(id)
                
                player.PX = 500f
                player.PY = 100f
                player.updateTime()
                
                TransferPackage("loadState_getPos_server", TransferPackage.SERVER, id, player.toJsonObject()).send(serverTransferModel)
            }
            "loadState_loadMap_client" -> {
                var json = JSONObject()
                var obj = JSONArray()
                for (i in gameController.serverController.logicMap.map) {
                    obj.append(i.toJsonObject())
                }
                var wires = JSONArray()
                for (i in gameController.serverController.logicMap.wires) {
                    wires.append(i.toJsonObject())
                }
                json.put("elements", obj)
                json.put("wires", wires)

                TransferPackage("loadState_loadMap_server", TransferPackage.SERVER, id, json).send(serverTransferModel)
            }

            "update_player_data" -> {
                id = transferPackage.data.getString("id", "")
                if (serverController.playersContainer.checkData(id)) {
                    var player = serverController.playersContainer.getData(id)!!
                    var dataP = PlayerData.fromJsonObject(transferPackage.data, gameController)
                    player.PX = dataP.PX
                    player.name = dataP.name
                    player.PY = dataP.PY
                    player.isActive = dataP.isActive
                    player.updateTime()
                } else {
                    TransferPackage("disconnect", TransferPackage.SERVER, id, JSONObject()).send(serverTransferModel)
                }
            }
            "add_logicElement" -> {
                if (!serverController.playersContainer.checkData(id)) {
                    return
                }
                var PX = transferPackage.data.getFloat("PX", 0f)
                var PY = transferPackage.data.getFloat("PY", 0f)
                serverController.logicMap.addElement(PX, PY, "")
                var json = JSONObject()
                var obj = JSONArray()
                for (i in gameController.serverController.logicMap.map) {
                    obj.append(i.toJsonObject())
                }
                var wires = JSONArray()
                for (i in gameController.serverController.logicMap.wires) {
                    wires.append(i.toJsonObject())
                }
                json.put("elements", obj)
                json.put("wires", wires)

                TransferPackage("loadState_loadMap_server", TransferPackage.SERVER, TransferPackage.ALL, json).send(serverTransferModel)
            }
        }
    }
}