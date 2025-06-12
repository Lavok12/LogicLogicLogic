package la.vok.GameController.TransferModel

import la.vok.GameController.*
import la.vok.GameController.Client.*
import la.vok.GameController.Content.Chat.ChatMessage
import processing.data.*
import la.vok.GameController.Content.Logic.LogicElement
import la.vok.GameController.Content.Logic.LogicWire
import la.vok.GameController.Content.PlayersContainer

class CLIENT_loadState_connect_server : ClientTransferHandler {
    override fun handle(data: JSONObject, updater: ClientTransferUpdater) {
        val controller = updater.clientController
        if (data.getString("return", "") == "ok") {
            controller.loadState = LoadState.GET_POS
            controller.serverConnect.updateLastTime()
        } else {
            println("CONNECTION OTKAZ")
            updater.gameController.destroyClient()
        }
    }
}

class CLIENT_loadState_getPos_server : ClientTransferHandler {
    override fun handle(data: JSONObject, updater: ClientTransferUpdater) {
        updater.clientController.player.PX = data.getFloat("PX")
        updater.clientController.player.PY = data.getFloat("PY")
        updater.clientController.loadState = LoadState.LOAD_MAP
    }
}

class CLIENT_player_set_position : ClientTransferHandler {
    override fun handle(data: JSONObject, updater: ClientTransferUpdater) {
        updater.clientController.player.PX = data.getFloat("PX")
        updater.clientController.player.PY = data.getFloat("PY")
    }
}

class CLIENT_loadState_loadMap_server : ClientTransferHandler {
    override fun handle(data: JSONObject, updater: ClientTransferUpdater) {
        val controller = updater.clientController
        val elements = data.getJSONArray("elements")
        val wires = data.getJSONArray("wires")

        controller.logicMap.clear()

        for (i in 0 until elements.size()) {
            val obj = elements.getJSONObject(i)
            controller.logicMap.addElement(
                LogicElement.fromJsonObject(obj, updater.gameController, controller.logicMap)
            )
        }

        for (i in 0 until wires.size()) {
            val obj = wires.getJSONObject(i)
            controller.logicMap.addWire(
                LogicWire.fromJsonObject(obj, updater.gameController, controller.logicMap)
            )
        }

        controller.player.isVisible = true
        controller.loadState = LoadState.STARTED
    }
}

class CLIENT_disconnect : ClientTransferHandler {
    override fun handle(data: JSONObject, updater: ClientTransferUpdater) {
        println("CLIENT KICKED")
        updater.gameController.destroyClient()
    }
}

class CLIENT_ping : ClientTransferHandler {
    override fun handle(data: JSONObject, updater: ClientTransferUpdater) {
        data.setLong("answer", System.currentTimeMillis())
        updater.clientController.clientFunctions.sendToServer("pong", data)
    }
}

class CLIENT_pong : ClientTransferHandler {
    override fun handle(data: JSONObject, updater: ClientTransferUpdater) {
        updater.clientController.serverConnect.updateLastTime()
        val sentTime = data.getLong("time")
        updater.clientController.serverConnect.ping = (System.currentTimeMillis() - sentTime).toInt()
        println("ping ${updater.clientController.serverConnect.ping}")
    }
}

class CLIENT_new_message : ClientTransferHandler {
    override fun handle(data: JSONObject, updater: ClientTransferUpdater) {
        var json = data.getJSONObject("data")
        updater.clientController.clientChatController.addMessage(
            json.getString("autor", ""),
            json.getString("text", ""),
            json.getInt("r", 255),
            json.getInt("g", 255),
            json.getInt("b", 255)
        )
    }
}


class CLIENT_players_data_update : ClientTransferHandler {
    override fun handle(data: JSONObject, updater: ClientTransferUpdater) {
        val controller = updater.clientController
        val cont = PlayersContainer.fromJsonObject(data, updater.gameController)

        controller.playersContainer.setDeleteFlags()
        for ((id, pdata) in cont.playersData) {
            val localPlayer = controller.playersContainer.playersData[id]
            if (localPlayer != null) {
                var isPlayer: Boolean = true
                if (id.equals(updater.clientController.clientId)) {
                    isPlayer = false
                } else {
                    localPlayer.PX = pdata.PX
                    localPlayer.PY = pdata.PY
                    localPlayer.name = pdata.name
                    localPlayer.DELETE_FLAG = false
                    localPlayer!!.isVisible = isPlayer
                }
            } else {
                var isPlayer: Boolean = true
                if (id.equals(updater.clientController.clientId)) {
                    isPlayer = false
                } else {
                    controller.playersContainer.addData(id, pdata.name)
                    controller.playersContainer.playersData[id]!!.PX = pdata.PX
                    controller.playersContainer.playersData[id]!!.PY = pdata.PY
                    controller.playersContainer.playersData[id]!!.name = pdata.name
                    controller.playersContainer.playersData[id]!!.DELETE_FLAG = false
                    controller.playersContainer.playersData[id]!!.isVisible = isPlayer
                }
            }
        }
        controller.playersContainer.deleteWithFlags()
    }
}
