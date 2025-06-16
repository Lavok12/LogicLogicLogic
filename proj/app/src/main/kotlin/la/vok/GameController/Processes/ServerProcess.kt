import la.vok.GameController.GameController
import la.vok.GameController.Processes.Process
import la.vok.GameController.Server.ServerController

open class ServerProcess(var serverController: ServerController, gameController: GameController) : Process(gameController) {
    open fun processingPackages() {
        serverController.serverTransferModel.serverTransferUpdater.transferBuffer.processingAll()
    }
}
