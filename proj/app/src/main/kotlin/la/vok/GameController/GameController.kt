package la.vok.GameController;

import la.vok.GameController.Client.ClientController
import la.vok.GameController.Server.ServerController
import la.vok.GameController.TransferModel.TransferModel
import la.vok.LoadData.LanguageController
import la.vok.LoadData.SpriteLoader
import la.vok.LoadData.UILoader
import la.vok.LoadData.LoadUIList
import la.vok.Storages.Settings

class GameController(var isClient: Boolean, var isServer: Boolean, var isLocal: Boolean = false) {
    lateinit var clientController: ClientController
    lateinit var serverController: ServerController
    lateinit var transferModel: TransferModel
    var languageController: LanguageController
    var spriteLoader: SpriteLoader
    var UILoader: UILoader
    var loadUIList: LoadUIList

    init {
        initTransferModel();
        if (isServer) initServer()
        if (isClient) initClient()
        
        UILoader = UILoader(this)
        languageController = LanguageController(Settings.language, this);
        spriteLoader = SpriteLoader(this);
        loadUIList = LoadUIList(this)

        
    }

    fun mainRender() {
        if (isClient) {
            clientController.mainRender()
        }
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
