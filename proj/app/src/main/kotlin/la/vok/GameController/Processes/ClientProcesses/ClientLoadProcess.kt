package la.vok.GameController.Processes.ClientProcesses

import ClientProcess
import la.vok.GameController.Client.ClientController
import la.vok.GameController.GameController

class ClientLoadProcess(clientController: ClientController, gameController: GameController) : ClientProcess(clientController, gameController){
    fun connect() {
        println("LoadState.CONNECT")
        clientController.clientFunctions.connect()
    }
    fun getPos() {
        println("LoadState.GET_POS")
        clientController.clientFunctions.get_pos()
    }
    fun loadMap() {
        println("LoadState.LOAD_MAP")
        clientController.clientFunctions.load_map()
    }

    fun connect_success() {
        getPos()
    }
    fun getPos_success() {
        loadMap()
    }
    fun loadMap_success() {
        end()
    }

    override fun start() {
        connect()
    }

    override fun tick() {
        clientController.clientTransferModel.clientTransferUpdater.transferBuffer.processingAll()
    }

    override fun end() {
        clientController.player.isVisible = true
        clientController.connect_success = true
        gameController.mainRender.setScene(gameController.scenesContainer.getScene("game")!!)
        super.end()
    }
}