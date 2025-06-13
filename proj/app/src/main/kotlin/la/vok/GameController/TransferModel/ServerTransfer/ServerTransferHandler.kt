package la.vok.GameController.TransferModel

import la.vok.GameController.*
import la.vok.GameController.Client.*
import la.vok.GameController.Content.Chat.ServerChatController
import processing.data.*
import la.vok.GameController.Content.Logic.LogicElement
import la.vok.GameController.Content.Logic.LogicWire
import la.vok.GameController.Content.PlayerData
import la.vok.Storages.Settings

class SERVER_loadState_connect_client : ServerTransferHandler {
    override fun handle(data: JSONObject, sender: String, updater: ServerTransferUpdater) {
        val name = data.getString("name")
        updater.serverController.serverFunctions.connectNewPlayer(sender, name)
    }
}

class SERVER_loadState_getPos_client : ServerTransferHandler {
    override fun handle(data: JSONObject, sender: String, updater: ServerTransferUpdater) {
        val id = data.getString("clientId", "")
        if (updater.serverController.connectsContainer.contains(id)) {
            val player = updater.serverController.connectsContainer.getPlayerData(id)
            updater.serverController.serverFunctions.sendToClient("loadState_getPos_server", id, player.toJsonObject())
        } else {
            updater.serverController.serverFunctions.sendToClient("disconnect", id)
        }
    }
}

class SERVER_loadState_loadMap_client : ServerTransferHandler {
    override fun handle(data: JSONObject, sender: String, updater: ServerTransferUpdater) {
        val map = updater.gameController.serverController.logicMap
        val elements = JSONArray()
        val wires = JSONArray()

        map.map.forEach { elements.append(it.toJsonObject()) }
        map.wires.forEach { wires.append(it.toJsonObject()) }

        val response = JSONObject()
        response.put("elements", elements)
        response.put("wires", wires)

        TransferPackage("loadState_loadMap_server", TransferPackage.SERVER, data.getString("clientId", ""), response)
            .send(updater.serverTransferModel)
    }
}

class SERVER_update_player_data : ServerTransferHandler {
    override fun handle(data: JSONObject, sender: String, updater: ServerTransferUpdater) {
        val id = data.getString("id", "")
        val container = updater.serverController.connectsContainer
        if (container.contains(id)) {
            val player = container.getPlayerData(id)
            val newData = PlayerData.fromJsonObject(data, updater.gameController)
            player.PX = newData.PX
            player.PY = newData.PY
            player.name = newData.name
        } else {
            updater.serverController.serverFunctions.disconnectPlayer(sender)
        }
    }
}

class SERVER_chat_message : ServerTransferHandler {
    override fun handle(data: JSONObject, sender: String, updater: ServerTransferUpdater) {
        var text = data.getString("text", "text")
        val id = data.getString("clientId", "")
        val container = updater.serverController.connectsContainer
        if (!container.contains(id)) return
        var autor = container.getPlayerData(id).name
        updater.serverController.serverChatController.addMessage(autor, text, Settings.standartMessage)
    }
}

class SERVER_add_logicElement : ServerTransferHandler {
    override fun handle(data: JSONObject, sender: String, updater: ServerTransferUpdater) {
        val id = data.getString("clientId", "")
        val container = updater.serverController.connectsContainer
        if (!container.contains(id)) return

        val map = updater.serverController.logicMap
        map.addElement(data.getFloat("PX", 0f), data.getFloat("PY", 0f), data.getString("type", "d"))

        val json = JSONObject()
        val elements = JSONArray()
        val wires = JSONArray()
        map.map.forEach { elements.append(it.toJsonObject()) }
        map.wires.forEach { wires.append(it.toJsonObject()) }

        json.put("elements", elements)
        json.put("wires", wires)

        TransferPackage("loadState_loadMap_server", TransferPackage.SERVER, TransferPackage.ALL, json)
            .send(updater.serverTransferModel)
    }
}

class SERVER_ping : ServerTransferHandler {
    override fun handle(data: JSONObject, sender: String, updater: ServerTransferUpdater) {
        data.setLong("answer", System.currentTimeMillis())
        updater.serverController.serverFunctions.sendToClient("pong", sender, data)
    }
}

class SERVER_pong : ServerTransferHandler {
    override fun handle(data: JSONObject, sender: String, updater: ServerTransferUpdater) {
        val container = updater.serverController.connectsContainer
        if (container.contains(sender)) {
            val conn = container.getConnect(sender)
            conn.updateLastTime()
            conn.ping = (System.currentTimeMillis() - data.getLong("time")).toInt()
        }
    }
}
