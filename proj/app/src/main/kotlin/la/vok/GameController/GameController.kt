package la.vok.GameController;

import la.vok.GameController.Client.ClientController
import la.vok.GameController.Server.ServerController
import la.vok.GameController.Menu.MenuController
import la.vok.LoadData.*
import la.vok.Storages.Settings
import la.vok.UI.*
import la.vok.Storages.Storage
import la.vok.InputController.*
import la.vok.GameController.Client.Rendering.*
import la.vok.GameController.Kts.KtsScriptManager
import la.vok.GameController.Kts.PreLoad.ContentPreLoader
import la.vok.UI.Canvas.LCanvasController
import la.vok.UI.Canvas.LCanvas
import la.vok.UI.Scenes.ScenesContainer
import la.vok.UI.Scenes.*
import la.vok.UI.Elements.LTextField

public enum class ClientState {
    UNINITIALIZED,
    INITIALIZED,
    STARTED,
    STOPPED,
    DESTROYED
}

public enum class ServerState {
    UNINITIALIZED,
    INITIALIZED,
    STARTED,
    STOPPED,
    DESTROYED
}

class GameController() {
    lateinit var clientController: ClientController
    lateinit var menuController: MenuController
    lateinit var serverController: ServerController
    lateinit var scenesLoader: ScenesLoader
    lateinit var languageController: LanguageController
    lateinit var spriteLoader: SpriteLoader
    lateinit var UILoader: UILoader
    lateinit var loadUIList: LoadUIList
    lateinit var scriptsLoader: ScriptsLoader
    lateinit var mainRender: MainRender
    lateinit var renderBuffer: RenderBuffer

    lateinit var mouseController: MouseController
    lateinit var keyTracker: KeyTracker
    lateinit var textFieldController: TextFieldController
    
    lateinit var lCanvasController: LCanvasController
    lateinit var scenesContainer: ScenesContainer
    lateinit var ktsScriptManager: KtsScriptManager

    lateinit var contentPreLoader: ContentPreLoader

    var gameStarted: Boolean = false

    var clientState = ClientState.UNINITIALIZED
    var serverState = ServerState.UNINITIALIZED

    var isClient: Boolean = false
    var isServer: Boolean = false
    var isLocal: Boolean = false
    init {
        println("GameController initialized")
    }

    fun startGame(isClient: Boolean, isServer: Boolean, isLocal: Boolean = false) {
        textFieldController.stopEditing()
        this.isClient = isClient
        this.isServer = isServer
        this.isLocal = isLocal
        println("---")
        println("Game started")
        println("---")
        gameStarted = true
        initServer()
        initClient()
    }

    fun startInit() {
        loadUIList = LoadUIList(this)
        UILoader = UILoader(this)
        scriptsLoader = ScriptsLoader(this)
        languageController = LanguageController(Settings.language, this)
        spriteLoader = SpriteLoader(this)
        mainRender = MainRender(this)
        scenesLoader = ScenesLoader(this)
        renderBuffer = RenderBuffer(this, mainRender)
        mouseController = MouseController(this)
        keyTracker = KeyTracker(this)
        textFieldController = TextFieldController(this)
        lCanvasController = LCanvasController(this)
        ktsScriptManager = KtsScriptManager(this)
        contentPreLoader = ContentPreLoader(this)
    }

    fun rendering() {
        mainRender.render()
    }

    fun initClient() {
        if (isClient && clientState == ClientState.UNINITIALIZED) {
            clientState = ClientState.INITIALIZED
            Storage.name = (getCanvas().findElementByTag("nicknameField") as LTextField).inputString
            if (Storage.name == "") {
                Storage.name = "lvk"
            }
            clientController = ClientController(this, Settings.address)
            initGameScenes()
            mainRender.setScene(scenesContainer.getScene("game")!!)
            if (!isLocal) {
                clientController.initOnline()
            } else {
                startClient()
            }
        }
    }

    private fun startClient() {
        if (clientState == ClientState.INITIALIZED) {
            clientController.start()
            clientState = ClientState.STARTED
        }
    }

    fun initMenu() {
        menuController = MenuController(this)
    }

    fun initServer() {
        if (isServer && serverState == ServerState.UNINITIALIZED) {
            serverState = ServerState.INITIALIZED
            serverController = ServerController(this, Settings.port)
            if (!isLocal) { 
                serverController.initOnline()
            } else {
                startServer()
            }
        }
    }

    private fun startServer() {
        if (serverState == ServerState.INITIALIZED) {
            serverController.start()
            serverState = ServerState.STARTED
        }
    }

    fun initLoaders() {
        languageController.loadData()
        spriteLoader.loadData()
        UILoader.loadData()
        scriptsLoader.loadData()
        scenesLoader.loadData()

        contentPreLoader.loadFiles()
    }

    fun initGameScenes() {
        scenesContainer.addScene(scenesLoader.newScene("game", "game"))
    }
    fun initScenes() {
        scenesContainer = ScenesContainer()
        scenesContainer.addScene(LScene("", "", gameController = this))
        scenesContainer.addScene(scenesLoader.newScene("main", "main"))
        scenesContainer.addScene(scenesLoader.newScene("loading", "loading"))
        scenesContainer.addScene(scenesLoader.newScene("disconnect", "disconnect"))
        mainRender.setScene(scenesContainer.getScene("loading")!!)
    }

    fun getCanvas(): LCanvas {
        return mainRender.LScene!!.canvas
    }

    fun getScene(): LScene {
        return mainRender.LScene!!
    }

    fun checkForStart() {
        if (!isLocal) {
            if (serverState == ServerState.INITIALIZED) {
                startServer()
            }
            if (clientState == ClientState.INITIALIZED) {
                if (clientController.onlineWebSocketClient.connected) {
                    startClient()
                }
                if (clientController.onlineWebSocketClient.onError) {
                    clientController.initOnline()
                }
            }
        }
    }

    fun gameTick() {
        checkForStart()
        if (gameStarted) {
            if (isServer && serverState == ServerState.STARTED) {
                serverController.tick()
            }
            if (isClient && clientState == ClientState.STARTED) {
                clientController.tick()
            }
        }
    }

    fun tick() {
        contentPreLoader.tick()
        keyTracker.tick()
    }

    fun destroyClient() {
        if (!isClientActive()) return
    
        destroyClientInternal()
        mainRender.setScene(scenesContainer.getScene("disconnect")!!)
    
        if (serverState != ServerState.STARTED) {
            destroyGame()
        } else if (isLocal) {
            destroyServerInternal()
            destroyGame()
        }
    }
    
    fun destroyServer() {
        if (!isServerActive()) return
    
        destroyServerInternal()
    
        if (clientState != ClientState.STARTED) {
            destroyGame()
        } else if (isLocal) {
            destroyClientInternal()
            destroyGame()
        }
    }
    
    private fun isClientActive() =
        clientState == ClientState.STARTED || clientState == ClientState.INITIALIZED
    
    private fun isServerActive() =
        serverState == ServerState.STARTED || serverState == ServerState.INITIALIZED
    
    private fun destroyClientInternal() {
        clientController.clientFunctions.destroy()
        println("destroyClient")
        clientState = ClientState.DESTROYED
    }
    
    private fun destroyServerInternal() {
        serverController.destroy()
        println("destroyServer")
        serverState = ServerState.DESTROYED
    }
    

    private fun destroy() {
        gameStarted = false
        println("---")
        println("Game Ended")
        println("---")
    }

    fun destroyGame() {
        if (gameStarted) {
            if (isClient) {
                destroyClient()
            }
            if (isServer) {
                destroyServer()
            }
            destroy()
        }
    }
}