package la.vok.GameController.Server

import la.vok.GameController.TransferModel.TransferPackage
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.putVec
import processing.data.JSONObject

class ServerFunctions(var serverController: ServerController) {
    fun disconnectPlayer(id: String) {
        if (serverController.connectsContainer.contains(id)) {
            serverController.connectsContainer.removeConnect(id)
        }
        sendToClient("disconnect", id)
    }

    fun sendToClient(header: String, connect: PlayerConnect, data: JSONObject = JSONObject()) {
        TransferPackage(header, TransferPackage.SERVER, connect.clientId, data).send(serverController.serverTransferModel)
    }

    fun sendToClient(header: String, id: String, data: JSONObject = JSONObject()) {
        TransferPackage(header, TransferPackage.SERVER, id, data).send(serverController.serverTransferModel)
    }

    fun sendToAll(header: String, data: JSONObject = JSONObject()) {
        TransferPackage(header, TransferPackage.SERVER, TransferPackage.ALL, data).send(serverController.serverTransferModel)
    }

    fun connectNewPlayer(id: String, name: String) {
        println("$name connected to Server ($id)")
        serverController.connectsContainer.addConnect(id)
        serverController.connectsContainer.getConnect(id).initPlayer(id, name)
        var json = JSONObject()
        json.put("return", "ok")
        sendToClient("loadState_connect_server", serverController.connectsContainer.getConnect(id), json)
    }

    fun ping(id: String) {
        var json = JSONObject()
        json.put("time", System.currentTimeMillis())
        sendToClient("ping", id, json)
    }

    fun playerSetPosition(id: String, pos: Vec2) {
        var json: JSONObject = JSONObject()
        json.putVec("pos", pos)
        sendToClient("player_set_position", id, json)
    }
}