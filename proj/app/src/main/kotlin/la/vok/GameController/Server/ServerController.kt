package la.vok.GameController.Server

import la.vok.GameController.GameController;
import la.vok.GameController.Content.Logic.LogicElement
import la.vok.GameController.Content.Map.LogicMap
import la.vok.GameController.Content.PlayersContainer
import la.vok.GameController.TransferModel.*
import la.vok.GameController.Content.*
import la.vok.GameController.Server.OnlineWebSocketServer
import la.vok.GameController.Server.PlayerConnect
import la.vok.Storages.*
import processing.data.*

class ServerController(var gameController: GameController, var port: Int = 0) {
    lateinit var onlineWebSocketServer: OnlineWebSocketServer
    var connectsContainer = ConnectsContainer(this)

    var logicMap = LogicMap(gameController)
    var serverTransferModel: ServerTransferModel

    var frame: Long = -1L

    init {
        println("ServerController initialized")
        serverTransferModel = ServerTransferModel(this)
    }

    fun initOnline() {
        onlineWebSocketServer = OnlineWebSocketServer(this, port)
        onlineWebSocketServer.start()
    }
    
    fun tick() {
        frame++
        serverTransferModel.serverTransferUpdater.transferBuffer.processingAll()
        logicMap.tick()
        logicMap.update()
        if (frame % Settings.updateIntervalFrames == 0L) {
            connectsContainer.removeOldConnections()
            sendToAll("players_data_update", connectsContainer.connectionsToPlayerContainer().toJsonObject())
        }
        if (frame % Settings.pingInterval == 0L) {
            connectsContainer.pingAll()
        }
    }

    fun start() {
        println("->ServerController started")
        logicMap.addElement(-200f, -400f, "test")
        logicMap.addElement(200f, -400f, "test")
        logicMap.addElement(0f, 400f, "test")
        logicMap.addWire(logicMap.list()[0], logicMap.list()[2])
        logicMap.addWire(logicMap.list()[1], logicMap.list()[2])
    }

    fun connectNewPlayer(id: String, name: String) {
        println("$name connected to Server ($id)")
        connectsContainer.addConnect(id)
        connectsContainer.getConnect(id).initPlayer(id, name)
        var json = JSONObject()
        json.put("return", "ok")
        sendToClient("loadState_connect_server", connectsContainer.getConnect(id), json)
    }

    fun ping(id: String) {
        var json = JSONObject()
        json.put("time", System.currentTimeMillis())
        sendToClient("ping", id, json)
    }

    fun checkConnections() {

    }
    
    fun getMap() {
        
    }

    fun destroy() {

    }

    fun sendToClient(header: String, connect: PlayerConnect, data: JSONObject = JSONObject()) {
        TransferPackage(header, TransferPackage.SERVER, connect.clientId, data).send(serverTransferModel)
    }

    fun sendToClient(header: String, id: String, data: JSONObject = JSONObject()) {
        TransferPackage(header, TransferPackage.SERVER, id, data).send(serverTransferModel)
    }

    fun sendToAll(header: String, data: JSONObject = JSONObject()) {
        TransferPackage(header, TransferPackage.SERVER, TransferPackage.ALL, data).send(serverTransferModel)
    }

    fun disconnectPlayer(id: String) {
        if (connectsContainer.contains(id)) {
            connectsContainer.removeConnect(id)
        }
        sendToClient("disconnect", id)
    }
}