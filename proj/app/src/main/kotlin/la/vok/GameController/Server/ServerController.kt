package la.vok.GameController.Server

import la.vok.GameController.GameController;
import la.vok.GameController.TransferModel.TransferModel
import la.vok.GameController.Content.Logic.LogicElement
import la.vok.GameController.Content.Map.LogicMap

class ServerController(var gameController: GameController) {
    var logicMap = LogicMap(gameController)

    init {
        println("ServerController initialized")
    }
    var LogicElements: ArrayList<LogicElement> = ArrayList()

    fun transferModel(): TransferModel {
        return gameController.transferModel
    }

    fun tick() {
        for (i in LogicElements) {
            i.tick()
        }
        for (i in LogicElements) {
            i.update()
        }
    }
    
    fun getTransferModel(): TransferModel {
        return gameController.transferModel
    }

    fun start() {
        println("ServerController started")
    }
}