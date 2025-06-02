package la.vok.GameController.Server

import la.vok.GameController.GameController;
import la.vok.GameController.Content.Logic.LogicElement
import la.vok.GameController.Content.Map.LogicMap
import la.vok.GameController.Content.PlayersContainer
import la.vok.GameController.TransferModel.*
import la.vok.GameController.Content.*
import la.vok.GameController.Server.OnlineWebSocketServer
import la.vok.Storages.Settings
import processing.data.*

class ServerController(var gameController: GameController, var port: Int = 0) {
    var logicMap = LogicMap(gameController)
    var serverTransferModel: ServerTransferModel

    lateinit var onlineWebSocketServer: OnlineWebSocketServer

    var playersContainer: PlayersContainer = PlayersContainer(gameController)
    var LogicElements: ArrayList<LogicElement> = ArrayList()

    var frame: Long = 0L
    

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
        for (i in LogicElements) {
            i.tick()
        }
        for (i in LogicElements) {
            i.update()
        }
        if (frame % Settings.updateIntervalFrames == 0L) {
            playersContainer.removeOldData(Settings.playersKickTime)
           TransferPackage("send_players_data", TransferPackage.SERVER, TransferPackage.ALL, playersContainer.toJsonObject()).send(serverTransferModel)
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
        playersContainer.addData(id, name)
        var json = JSONObject()
        json.put("return", "ok")
        TransferPackage("loadState_connect_server", TransferPackage.SERVER, id, json).send(serverTransferModel)
    }
    
    fun updatePlayer(id: String, JSONdata: JSONObject) {
        if (playersContainer.checkData(id)) {
            playersContainer.updateData(id)
        } else {
            TransferPackage("disconnect", TransferPackage.SERVER, id, JSONObject()).send(serverTransferModel)
        }
    }

    fun checkConnections() {

    }
    
    fun getMap() {
        
    }

    fun destroy() {

    }
}