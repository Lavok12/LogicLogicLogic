package la.vok.GameController.Server

import ClientProcess
import ServerProcess
import la.vok.GameController.GameController;
import la.vok.GameController.Content.Map.LogicMap
import la.vok.GameController.TransferModel.*
import la.vok.GameController.Content.Chat.ServerChatController
import la.vok.GameController.Processes.InterruptionManager
import la.vok.GameController.Processes.ProcessesManager
import la.vok.LavokLibrary.MetaStorage
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.Storages.*

class ServerController(var gameController: GameController, var port: Int = 0) : MetaStorage() {
    lateinit var onlineWebSocketServer: OnlineWebSocketServer
    var connectsContainer = ConnectsContainer(this)

    var logicMap = LogicMap(gameController)
    var serverTransferModel: ServerTransferModel
    var serverChatController: ServerChatController
    var serverFunctions: ServerFunctions = ServerFunctions(this)
    
    var frame: Long = -1L

    val interruptions = InterruptionManager<ServerProcess>()
    val processes = ProcessesManager<ServerProcess>()

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
        processes.tick()

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
        logicMap.addElement(Vec2(-200f, -400f), "test")
        logicMap.addElement(Vec2(200f, -400f), "test")
        logicMap.addElement(Vec2(0f, 400f), "test")
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