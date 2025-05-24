package la.vok.GameController;

import la.vok.GameController.Client.ClientController
import la.vok.GameController.Server.ServerController
import la.vok.GameController.Menu.MenuController
import la.vok.GameController.TransferModel.TransferModel
import la.vok.LoadData.LanguageController
import la.vok.LoadData.SpriteLoader
import la.vok.LoadData.UILoader
import la.vok.LoadData.LoadUIList
import la.vok.LoadData.ScriptsLoader
import la.vok.LoadData.ScenesLoader
import la.vok.Storages.Settings
import la.vok.UI.*
import com.jsyn.engine.LoadAnalyzer
import la.vok.Storages.Storage

class GameController(var isClient: Boolean, var isServer: Boolean, var isLocal: Boolean = false) {
    lateinit var clientController: ClientController
    lateinit var menuController: MenuController
    lateinit var serverController: ServerController
    lateinit var transferModel: TransferModel
    lateinit var scenesLoader: ScenesLoader
    lateinit var languageController: LanguageController
    lateinit var spriteLoader: SpriteLoader
    lateinit var UILoader: UILoader
    lateinit var loadUIList: LoadUIList
    lateinit var scriptsLoader: ScriptsLoader
    lateinit var mainRender: MainRender

    var gameStarted: Boolean = false
    
    fun startGame() {
        println("Game started")
        gameStarted = true
        initTransferModel()
        initServer()
        initClient()
        getCanvas().elements.clear()
    }
    
    fun startInit() {
        loadUIList = LoadUIList(this)
        UILoader = UILoader(this)
        scriptsLoader = ScriptsLoader(this)
        languageController = LanguageController(Settings.language, this);
        spriteLoader = SpriteLoader(this);
        mainRender = MainRender(this);
        scenesLoader = ScenesLoader(this);
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

    fun initServer() {
        if (isServer) {
            serverController = ServerController(this)
        }
    }
    
    fun initTransferModel() {
        transferModel = TransferModel(this, isLocal)
    }

    fun initLoaders() {
        languageController.loadData()
        spriteLoader.loadData()
        UILoader.loadData()
        scriptsLoader.loadData()
        scenesLoader.loadData()

        mainRender.setScene("main")
    }
    

    fun getCanvas(): LCanvas {
        return mainRender.LScene!!.canvas
    }

    fun UITick() {
        if (isClient) {
            getCanvas().tick(Storage.moux, Storage.mouy)
        }
    }

    fun gameTick() {
        if (isServer) {
            serverController.tick()
        }
    }
}
