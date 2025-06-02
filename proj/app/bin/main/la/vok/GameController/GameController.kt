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
import la.vok.GameController.Content.*

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
    lateinit var keyTracker: KeyTracker
    
    lateinit var scenesContainer: ScenesContainer
    var gameStarted: Boolean = false

    var clientState = ClientState.UNINITIALIZED
    var serverState = ServerState.UNINITIALIZED

    init {
        println("GameController initialized")
    }

    fun startGame() {
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
        rendering = Rendering(this)
        scenesLoader = ScenesLoader(this)

        mouseController = MouseController(this)
        keyTracker = KeyTracker(this)
    }

    fun rendering() {
        rendering.render()
    }

    fun initClient() {
        if (isClient && clientState == ClientState.UNINITIALIZED) {
            clientState = ClientState.INITIALIZED
            clientController = ClientController(this, Settings.addres)
            rendering.setScene(scenesContainer.getScene("")!!)
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
    }

    fun initScenes() {
        scenesContainer = ScenesContainer()
        scenesContainer.addScene(LScene("", "", gameController = this))
        scenesContainer.addScene(scenesLoader.newScene("main", "main"))
        scenesContainer.addScene(scenesLoader.newScene("disconnect", "disconnect"))
        rendering.setScene(scenesContainer.getScene("main")!!)
    }

    fun getCanvas(): LCanvas {
        return rendering.LScene!!.canvas
    }

    fun getScene(): LScene {
        return rendering.LScene!!
    }

    fun UITick() {
        getScene().canvas.tick(mouseController.moux, mouseController.mouy, this)
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

    fun destroyClient() {
        println("destroyClient")
        if (clientState == ClientState.STARTED || clientState == ClientState.INITIALIZED) {
            clientController.destroy()
            clientState = ClientState.DESTROYED
            rendering.setScene(scenesContainer.getScene("disconnect")!!)
            if (serverState != ServerState.STARTED) {
                destroyGame()
            } else if (isLocal) {
                serverController.destroy()
                serverState = ServerState.DESTROYED
                destroyGame()
            }
        }
    }

    fun destroyServer() {
        println("destroyServer")
        if (serverState == ServerState.STARTED || serverState == ServerState.INITIALIZED) {
            serverController.destroy()
            serverState = ServerState.DESTROYED
            if (clientState != ClientState.STARTED) {
                destroyGame()
            } else if (isLocal) {
                clientController.destroy()
                clientState = ClientState.DESTROYED
                destroyGame()
            }
        }
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