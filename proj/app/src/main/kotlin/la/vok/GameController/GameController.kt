package la.vok.GameController;

import la.vok.GameController.Client.ClientController
import la.vok.GameController.Server.ServerController
import la.vok.GameController.Menu.MenuController
import la.vok.GameController.TransferModel.*
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
import la.vok.InputController.*

class GameController(var isClient: Boolean, var isServer: Boolean, var isLocal: Boolean = false) {
    lateinit var clientController: ClientController
    lateinit var menuController: MenuController
    lateinit var serverController: ServerController
    lateinit var scenesLoader: ScenesLoader
    lateinit var languageController: LanguageController
    lateinit var spriteLoader: SpriteLoader
    lateinit var UILoader: UILoader
    lateinit var loadUIList: LoadUIList
    lateinit var scriptsLoader: ScriptsLoader
    lateinit var rendering: Rendering
    lateinit var mouseController: MouseController

    var disH: Float = 0f
    var disW: Float = 0f
    var disH2: Float = 0f
    var disW2: Float = 0f
    var fix: Float = 0f
    
    var gameStarted: Boolean = false
    
    init {
        println("GameController initialized")
    }

    fun startGame() {
        println("Game started")
        gameStarted = true
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
        rendering = Rendering(this);
        scenesLoader = ScenesLoader(this);

        mouseController = MouseController(this)
    }

    fun rendering() {
        if (isClient) {
            rendering.render()
        }
    }

    fun initClient() {
        if (isClient) {
            clientController = ClientController(this)
            clientController.standart()
        }
    }

    fun initMenuController() {
        menuController = MenuController(this);
    }

    fun initServer() {
        if (isServer) {
            serverController = ServerController(this)
            serverController.start()
        }
    }

    fun initLoaders() {
        languageController.loadData()
        spriteLoader.loadData()
        UILoader.loadData()
        scriptsLoader.loadData()
        scenesLoader.loadData()
        
        rendering.setScene(scenesLoader.newScene("main1", "main"))
    }
    

    fun getCanvas(): LCanvas {
        return rendering.LScene!!.canvas
    }
    fun getScene(): LScene {
        return rendering.LScene!!
    }

    fun UITick() {
        if (isClient) {
            getScene().tick(mouseController.moux, mouseController.mouy, this)
        }
    }

    fun gameTick() {
        if (isServer) {
            serverController.tick()
        }
    }
}
