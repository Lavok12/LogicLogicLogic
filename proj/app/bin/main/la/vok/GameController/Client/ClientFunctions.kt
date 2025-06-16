package la.vok.GameController.Client

import la.vok.GameController.TransferModel.TransferPackage
import la.vok.LavokLibrary.putVec
import processing.data.JSONObject

class ClientFunctions(var clientController: ClientController) {

    fun setLogicElement() {
        var json = JSONObject()
        json.putVec("pos", clientController.player.pos)
        json.put("type", "new")
        sendToServer("add_logicElement", json)
    }

    fun connect() {
        var json = JSONObject()
        json.put("name", clientController.name)
        sendToServer("loadState_connect_client", json)
    }

    fun get_pos() {
        sendToServer("loadState_getPos_client")
    }

    fun load_map() {
        sendToServer("loadState_loadMap_client")
    }

    fun destroy() {
        clientController.player.destroy()
        clientController.playersContainer.clear()
    }

    fun sendToServer(header: String, data: JSONObject = JSONObject()) {
        data.put("clientId", clientController.clientId)
        TransferPackage(
            header,
            clientController.clientId,
            TransferPackage.SERVER,
            data
        ).send(clientController.clientTransferModel)
    }
}