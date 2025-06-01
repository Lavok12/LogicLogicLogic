package la.vok.GameController.TransferModel

import processing.data.JSONObject
import la.vok.GameController.Content.Logic.*
import la.vok.GameController.Content.PlayersContainer

class ClientTransferUpdater(var clientTransferModel: ClientTransferModel) : TransfeUpdater {
    var clientController = clientTransferModel.clientController
    var gameController = clientController.gameController

    init {
        println("       ClientTransferUpdater initialized")
    }

    override fun processingData(transferPackage: TransferPackage) {
        if (transferPackage.recipient != clientController.clientId) {
            if (transferPackage.recipient != TransferPackage.ALL) {
                return
            }
        }

        when(transferPackage.header) {
            "set_coord" -> {
                var json = transferPackage.data
                clientController.player.PX = json.getFloat("PX")
                clientController.player.PY = json.getFloat("PY")
            }
            "set_map" -> {
                var json = transferPackage.data
                var obj = json.getJSONArray("elements")
                var wires = json.getJSONArray("wires")
                
                for (i in 0 until obj.size()) {
                    val l: JSONObject = obj.getJSONObject(i)
                    clientController.logicMap.addElement(LogicElement.fromJsonObject(l, gameController, gameController.clientController.logicMap))
                }
                
                for (i in 0 until wires.size()) {
                    val l: JSONObject = wires.getJSONObject(i)
                    clientController.logicMap.addWire(LogicWire.fromJsonObject(l, gameController, gameController.clientController.logicMap))
                }
            }
            "disconnect" -> {
                gameController.destroyClient()
            }
            "send_players_data" -> {
                var json = transferPackage.data
                var cont = PlayersContainer.fromJsonObject(json, gameController)

                clientController.playersContainer.setDeleteFlags()
                for (i in cont.playersData.keys) {
                    if (clientController.playersContainer.playersData.containsKey(i)) {
                        clientController.playersContainer.playersData[i]!!.SEND_PX = cont.playersData[i]!!.PX
                        clientController.playersContainer.playersData[i]!!.SEND_PY = cont.playersData[i]!!.PY
                        clientController.playersContainer.playersData[i]!!.name = cont.playersData[i]!!.name
                        clientController.playersContainer.playersData[i]!!.DELETE_FLAG = false
                    } else {
                        clientController.playersContainer.addData(i, cont.playersData[i]!!.name)
                        clientController.playersContainer.playersData[i]!!.PX = cont.playersData[i]!!.PX
                        clientController.playersContainer.playersData[i]!!.PY = cont.playersData[i]!!.PY
                        clientController.playersContainer.playersData[i]!!.name = cont.playersData[i]!!.name
                        clientController.playersContainer.playersData[i]!!.DELETE_FLAG = false
                    }
                }
                clientController.playersContainer.deleteWithFlags()
            }
        }
    }
}