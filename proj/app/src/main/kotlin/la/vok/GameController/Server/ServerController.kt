package la.vok.GameController.Server

import la.vok.GameController.GameController;
import la.vok.GameController.Content.Logic.LogicElement
import la.vok.GameController.Content.Map.LogicMap
import la.vok.GameController.TransferModel.*

class ServerController(var gameController: GameController) {
    var logicMap = LogicMap(gameController)
    var players = HashMap<String, PlayerData>()
    var serverTransferModel: ServerTransferModel

    init {
        println("ServerController initialized")
        serverTransferModel = ServerTransferModel(this)
    }
    var LogicElements: ArrayList<LogicElement> = ArrayList()


    fun tick() {
        for (i in LogicElements) {
            i.tick()
        }
        for (i in LogicElements) {
            i.update()
        }
    }

    fun start() {
        println("ServerController started")
        logicMap.addElement(-200f, -400f, "test")
        logicMap.addElement(200f, -400f, "test")
        logicMap.addElement(0f, 400f, "test")
        logicMap.addWire(logicMap.list()[0], logicMap.list()[2])
        logicMap.addWire(logicMap.list()[1], logicMap.list()[2])
    }

    fun connectNewPlayer(id: String, name: String) {
        println("$name connected to Server ($id)")
        players.put(id, PlayerData(id, name))
    }

    fun checkConnections() {

    }
    fun getMap() {
        
    }
}