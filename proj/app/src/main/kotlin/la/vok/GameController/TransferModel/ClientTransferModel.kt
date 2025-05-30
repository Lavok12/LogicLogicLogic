package la.vok.GameController.TransferModel

import la.vok.GameController.*
import la.vok.GameController.Client.*
import processing.data.*
import la.vok.GameController.Content.Logic.LogicElement
import la.vok.GameController.Content.Logic.LogicWire

class ClientTransferModel(var clientController: ClientController) {
    var isLocal: Boolean = clientController.gameController.isLocal
    init {
        println("ClientTransferModel initialized")
    }
    val gameController: GameController
    get() {
        return clientController.gameController
    }
    val serverTransferModel: ServerTransferModel
    get() {
        return gameController.serverController.serverTransferModel
    }
    
    // input
    fun client_input_connect(id: String, name: String) {
        if (isLocal) {
            serverTransferModel.server_output_connect(id, name)
        }
    }

    fun client_input_getMap() {
        if (isLocal) {
            serverTransferModel.server_output_getMap()
        }
    }
    // output
    fun client_output_setMap() {
        if (isLocal) {
            var obj = JSONArray();
            for (i in gameController.serverController.logicMap.map) {
                obj.append(i.toJsonObject())
            }
            var wires = JSONArray();
            for (i in gameController.serverController.logicMap.wires) {
                wires.append(i.toJsonObject())
            }

            for (i in 0 until obj.size()) {
                val l: JSONObject = obj.getJSONObject(i)
                gameController.clientController.logicMap.addElement(LogicElement.fromJsonObject(l, gameController, gameController.clientController.logicMap))
            }
            
            for (i in 0 until wires.size()) {
                val l: JSONObject = wires.getJSONObject(i)
                gameController.clientController.logicMap.addWire(LogicWire.fromJsonObject(l, gameController, gameController.clientController.logicMap))
            }
            println("loadMap elements: ${obj.size()}. wires: ${wires.size()}")
        }
    }
}