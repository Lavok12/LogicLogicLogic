package la.vok.GameController

import la.vok.GameController.Client.ClientController
import la.vok.GameController.Server.ServerController
import la.vok.GameController.Menu.MenuController
import la.vok.LoadData.*
import la.vok.Storages.Settings
import la.vok.UI.*
import la.vok.Storages.Storage
import la.vok.InputController.*
import la.vok.GameController.Client.Rendering.*
import la.vok.GameController.Processes.InterruptionManager
import la.vok.GameController.Processes.GameProcesses.ScriptsPreCompiler
import la.vok.GameController.Kts.KtsScriptManager
import la.vok.GameController.Processes.Process
import la.vok.GameController.Processes.ProcessesManager
import la.vok.LavokLibrary.MetaStorage
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

class GameController : MetaStorage() {
    lateinit var clientController: ClientController
    lateinit var menuController: MenuController
    lateinit var serverController: ServerController
    lateinit var loadUIList: LoadUIList
    lateinit var mainRender: MainRender
    lateinit var renderBuffer: RenderBuffer

    lateinit var mouseController: MouseController
    lateinit var keyTracker: KeyTracker
    lateinit var textFieldController: TextFieldController

    lateinit var lCanvasController: LCanvasController
    lateinit var scenesContainer: ScenesContainer
    lateinit var ktsScriptManager: KtsScriptManager

    lateinit var loaders: Loaders

    var gameStarted: Boolean = false

    var clientState = ClientState.UNINITIALIZED
    var serverState = ServerState.UNINITIALIZED

    var isClient: Boolean = false
    var isServer: Boolean = false
    var isLocal: Boolean = false

    val interruptions = InterruptionManager<Process>()
    val processes = ProcessesManager<Process>()

    init {
        println("GameController initialized")
    }

    fun startGame(isClient: Boolean, isServer: Boolean, isLocal: Boolean = false) {
        textFieldController.stopEditing()
        this.isClient = isClient
        this.isServer = isServer
        this.isLocal = isLocal
        gameStarted = true

        Storage.name = (getCanvas().findElementByTag("nicknameField") as LTextField).inputString
        if (Storage.name == "") {
            Storage.name = "lvk"
        }

        interruptions.add(ScriptsPreCompiler(this, "kts_server_list", this::finalStartGame))
    }

    fun finalStartGame() {
        println("---")
        println("Game started")
        println("---")
        initServer()
        initClient()
    }

    fun initCoreComponents() {
        loaders = Loaders(this)
        loadUIList = LoadUIList(this)
        mainRender = MainRender(this)
        renderBuffer = RenderBuffer(this, mainRender)
        mouseController = MouseController(this)
        keyTracker = KeyTracker(this)
        textFieldController = TextFieldController(this)
        lCanvasController = LCanvasController(this)
        ktsScriptManager = KtsScriptManager(this)
    }

    fun rendering() {
        mainRender.render()
    }

    fun initClient() {
        if (isClient && clientState == ClientState.UNINITIALIZED) {
            clientState = ClientState.INITIALIZED
            clientController = ClientController(this, Settings.address)
            initGameScenes()
            mainRender.setScene(scenesContainer.getScene("")!!)
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
        loaders.loadAll()
    }

    fun onPreloadFinished() {
        mainRender.setScene(scenesContainer.getScene("main")!!)
    }

    fun initGameScenes() {
        scenesContainer.addScene(loaders.scenes.newScene("game", "game"))
    }

    fun initScenes() {
        scenesContainer = ScenesContainer().apply {
            addScene(LScene("", "", gameController = this@GameController))
            listOf("main", "disconnect", "loading").forEach {
                addScene(loaders.scenes.newScene(it, it))
            }
        }
        mainRender.setScene(scenesContainer.getScene("")!!)
    }

    fun scriptsPreCompilate() {
        interruptions.add(ScriptsPreCompiler(this, "kts_preload_list", this::onPreloadFinished))
    }

    fun getCanvas(): LCanvas = mainRender.LScene!!.canvas

    fun getScene(): LScene = mainRender.LScene!!

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
        if (!interruptions.isEmpty) {
            return
        }
        checkForStart()

        processes.tick()
        if (!gameStarted) return

        if (isServer && serverState == ServerState.STARTED) {
            val serverInterruptions = serverController.interruptions
            if (serverInterruptions.isEmpty) {
                serverController.tick()
            } else {
                serverInterruptions.tick()
            }
        }

        if (isClient && clientState == ClientState.STARTED) {
            val clientInterruptions = clientController.interruptions
            if (clientInterruptions.isEmpty) {
                clientController.tick()
            } else {
                clientInterruptions.tick()
            }
        }
    }


    fun tick() {
        if (interruptions.isEmpty) {
            keyTracker.tick()
        } else {
            interruptions.tick()
        }
    }

    fun destroyClient() {
        if (!clientState.isActive()) return

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
        if (!serverState.isActive()) return

        destroyServerInternal()

        if (clientState != ClientState.STARTED) {
            destroyGame()
        } else if (isLocal) {
            destroyClientInternal()
            destroyGame()
        }
    }

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

    fun ClientState.isActive() = this == ClientState.STARTED || this == ClientState.INITIALIZED
    fun ServerState.isActive() = this == ServerState.STARTED || this == ServerState.INITIALIZED
}
