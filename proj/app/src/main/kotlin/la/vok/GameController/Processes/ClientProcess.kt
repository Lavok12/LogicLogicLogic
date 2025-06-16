import la.vok.GameController.Client.ClientController
import la.vok.GameController.GameController
import la.vok.GameController.Processes.Process

open class ClientProcess(var clientController: ClientController, gameController: GameController) : Process(gameController) {
    open fun processingPackages() {
        clientController.clientTransferModel.clientTransferUpdater.transferBuffer.processingAll()
    }
}
