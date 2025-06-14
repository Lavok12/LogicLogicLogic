package la.vok.GameController.Client

import la.vok.UI.MainRender
import la.vok.GameController.GameController;
import la.vok.GameController.Content.Logic.LogicElement
import la.vok.Storages.Storage
import la.vok.Storages.Settings
import la.vok.GameController.Content.Map.LogicMap
import la.vok.GameController.Content.PlayersContainer
import la.vok.LavokLibrary.Functions
import la.vok.GameController.TransferModel.*
import la.vok.GameController.Content.PlayerData
import la.vok.GameController.Client.LoadState
import la.vok.GameController.Client.Rendering.*
import la.vok.GameController.Content.Chat.ClientChatController
import la.vok.InputController.KeyCode
import processing.data.JSONObject
import java.net.URI
import la.vok.UI.Canvas.*
import la.vok.UI.Scenes.*

enum class LoadState {
    STANDART,
    NULL,
    CONNECT,
    GET_POS,
    LOAD_MAP,
    LOAD_PLAYERS,
    STARTED
}

class ClientController(var gameController: GameController, var address: String = "") {
    var logicMap = LogicMap(gameController)
    var mainCamera: Camera
    var clientId: String = Functions.getUniqueDeviceId()
    var name: String = Storage.name
    var clientTransferModel: ClientTransferModel
    var frame: Long = -1L
    var loadState: LoadState = LoadState.NULL

    lateinit var onlineWebSocketClient: OnlineWebSocketClient

    var player: PlayerData
    var playersContainer: PlayersContainer
    var clientChatController: ClientChatController

    var clientFunctions: ClientFunctions = ClientFunctions(this)
    var isLoaded = false;
    var serverConnect: ServerConnect = ServerConnect(this)


    init {
        player = PlayerData(clientId, name, gameController)
        playersContainer = PlayersContainer(gameController)
        clientChatController = ClientChatController(this, Settings.clientChatHistory)
    }

    fun renderUpdate(renderBuffer: RenderBuffer) {
        player.renderUpdate(renderBuffer)
        playersContainer.renderUpdate(renderBuffer)
        logicMap.renderUpdate(renderBuffer)
        player.updateCanvas()
        playersContainer.updateCanvas()
    }

    fun checkLastTime() {
        if (serverConnect.lastUpdate + Settings.playerDisconnectTime < Storage.main.millis()) {
            println("SERVER NE OTVECHAET")
            gameController.destroyClient()
        }
    }

    init {
        println("ClientController initialized")
        clientTransferModel = ClientTransferModel(this)
        mainCamera = Camera(0f, 0f, 1f)
        player.isVisible = false
    }

    fun initOnline() {
        onlineWebSocketClient = OnlineWebSocketClient(this, URI(address))
        onlineWebSocketClient.connect()
    }

    fun start() {
        loadState = LoadState.STANDART
        println("->ClientController started")
        logicMap.updateWireSet()
        isLoaded = true
    }

    fun checkLoad() {
        if (loadState == LoadState.STANDART) {
            loadState = LoadState.CONNECT
        } else if (loadState == LoadState.CONNECT) {
            println("LoadState.CONNECT")
            loadState = LoadState.NULL
            clientFunctions.connect()
        } else if (loadState == LoadState.GET_POS) {
            println("LoadState.GET_POS")
            loadState = LoadState.NULL
            clientFunctions.get_pos()
        } else if (loadState == LoadState.LOAD_MAP) {
            println("LoadState.LOAD_MAP")
            loadState = LoadState.NULL
            clientFunctions.load_map()
        } else if (loadState == LoadState.LOAD_PLAYERS) {
            println("LoadState.LOAD_PLAYERS")
        }
    }
    fun tick() {
        frame++;
        clientTransferModel.clientTransferUpdater.transferBuffer.processingAll()
        checkLoad()
        if (loadState == LoadState.STARTED) {
            checkLastTime()
            if (!gameController.textFieldController.isEditing) {
                if (gameController.keyTracker.isPressed(KeyCode.w)) {
                    player.pos.y += 20
                }
                if (gameController.keyTracker.isPressed(KeyCode.a)) {
                    player.pos.x -= 20
                }
                if (gameController.keyTracker.isPressed(KeyCode.s)) {
                    player.pos.y -= 20
                }
                if (gameController.keyTracker.isPressed(KeyCode.d)) {
                    player.pos.x += 20
                }
            }
            
            mainCamera.position = player.pos
            
            if (frame % Settings.updateIntervalFrames == 0L) {
                clientFunctions.sendToServer("update_player_data", player.toJsonObject())
            }
            if (frame % Settings.pingInterval == 0L) {
                var json = JSONObject()
                json.put("time", System.currentTimeMillis())
                clientFunctions.sendToServer("ping", json)
            }
            clientChatController.removeOldMessages()
        }
    }
}
