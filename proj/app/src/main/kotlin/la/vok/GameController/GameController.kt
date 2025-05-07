package la.vok.GameController;

import la.vok.GameController.Client.ClientController
import la.vok.GameController.Server.ServerController
import la.vok.GameController.TransferModel.TransferModel
import la.vok.LoadData.LanguageController
import la.vok.LoadData.SpriteLoader
import la.vok.Storages.Settings

class GameController(var isClient: Boolean, var isServer: Boolean, var isLocal: Boolean = false) {
    lateinit var clientController: ClientController
    lateinit var serverController: ServerController
    lateinit var transferModel: TransferModel
    var languageController: LanguageController
    var spriteLoader: SpriteLoader
    
    init {
        if (isClient) initClient()
        if (isServer) initServer()
        initTransferModel();

        languageController = LanguageController(Settings.language);
        spriteLoader = SpriteLoader();
    }

    internal fun initClient() {
        clientController = ClientController()
    }
    internal fun initServer() {
        serverController = ServerController()
    }
    internal fun initTransferModel() {
        transferModel = TransferModel(isLocal = isLocal)
    }
}
