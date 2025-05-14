package la.vok.GameController;

import la.vok.GameController.Client.ClientController
import la.vok.GameController.Server.ServerController
import la.vok.GameController.Menu.MenuController
import la.vok.GameController.TransferModel.TransferModel
import la.vok.LoadData.LanguageController
import la.vok.LoadData.SpriteLoader
import la.vok.LoadData.UILoader
import la.vok.LoadData.LoadUIList
import la.vok.Storages.Settings
import la.vok.Render.MainRender
import com.jsyn.engine.LoadAnalyzer

class GameController(var isClient: Boolean, var isServer: Boolean, var isLocal: Boolean = false) {
    lateinit var clientController: ClientController
    lateinit var menuController: MenuController
    lateinit var serverController: ServerController
    lateinit var transferModel: TransferModel
    lateinit var languageController: LanguageController
    lateinit var spriteLoader: SpriteLoader
    lateinit var UILoader: UILoader
    lateinit var loadUIList: LoadUIList
    lateinit var mainRender: MainRender

    init {
        
    }
    fun startInit() {
        loadUIList = LoadUIList(this)
        UILoader = UILoader(this)
        languageController = LanguageController(Settings.language, this);
        spriteLoader = SpriteLoader(this);
        mainRender = MainRender(this);
    }

    fun mainRender() {
        if (isClient) {
            mainRender.render()
        }
    }

    fun initClient() {
        if (isClient) {
            clientController = ClientController(this)
        }
    }

    fun initMenuController() {
        menuController = MenuController(this);
    }

    fun initLoaders() {
        languageController.loadData()
        spriteLoader.loadData()
        UILoader.loadData()
    }
    
    fun initServer() {
        if (isServer) {

        }
    }
    
    fun initTransferModel() {
        transferModel = TransferModel(isLocal = isLocal)
    }
}
