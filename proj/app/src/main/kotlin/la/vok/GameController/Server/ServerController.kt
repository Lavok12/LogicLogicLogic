package la.vok.GameController.Server

import la.vok.GameController.GameController;
import la.vok.GameController.Content.Logic.LogicElement
import la.vok.GameController.Content.Map.LogicMap
import la.vok.GameController.Content.PlayersContainer
import la.vok.GameController.TransferModel.*
import la.vok.GameController.Content.*
import la.vok.GameController.Server.OnlineWebSocketServer
import la.vok.GameController.Server.PlayerConnect
import la.vok.GameController.Content.Chat.ServerChatController
import la.vok.Storages.*
import processing.data.*

class ServerController(var gameController: GameController, var port: Int = 0) {
    lateinit var onlineWebSocketServer: OnlineWebSocketServer
    var connectsContainer = ConnectsContainer(this)

    var logicMap = LogicMap(gameController)
    var serverTransferModel: ServerTransferModel
    var serverChatController: ServerChatController
    var serverFunctions: ServerFunctions = ServerFunctions(this)
    
    var frame: Long = -1L

    init {
        println("ServerController initialized")
        serverTransferModel = ServerTransferModel(this)
        serverChatController = ServerChatController(this, Settings.serverChatHistory)
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
            serverFunctions.sendToAll("players_data_update", connectsContainer.connectionsToPlayerContainer().toJsonObject())
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

    fun checkConnections() {

    }
    
    fun getMap() {
        
    }

    fun destroy() {

    }
}