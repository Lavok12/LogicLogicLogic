package la.vok.GameController.Client

import la.vok.UI.MainRender
import la.vok.GameController.GameController;
import la.vok.GameController.Content.Logic.LogicElement
import la.vok.UI.LCanvas
import la.vok.Storages.Storage
import la.vok.Storages.Settings
import la.vok.GameController.Content.Map.LogicMap
import la.vok.GameController.Content.PlayersContainer
import la.vok.LavokLibrary.Functions
import la.vok.GameController.TransferModel.*
import la.vok.GameController.Content.PlayerData
import la.vok.GameController.Client.LoadState
import la.vok.GameController.Client.Rendering.*
import la.vok.InputController.KeyCode
import processing.data.JSONObject
import java.net.URI

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
    var name: String = "Lvk1"
    var clientTransferModel: ClientTransferModel
    var frame: Long = -1L
    var loadState: LoadState = LoadState.NULL

    lateinit var onlineWebSocketClient: OnlineWebSocketClient

    var player: PlayerData = PlayerData(clientId, name, gameController)
    var playersContainer: PlayersContainer = PlayersContainer(gameController)
    var isLoaded = false;

    var serverConnect: ServerConnect = ServerConnect(this)

    fun renderUpdate(renderBuffer: RenderBuffer) {
        player.updateCanvas()
        playersContainer.updateCanvas()
        player.renderUpdate(renderBuffer)
        playersContainer.renderUpdate(renderBuffer)
        logicMap.renderUpdate(renderBuffer)
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
            connect()
        } else if (loadState == LoadState.GET_POS) {
            println("LoadState.GET_POS")
            loadState = LoadState.NULL
            get_pos()
        } else if (loadState == LoadState.LOAD_MAP) {
            println("LoadState.LOAD_MAP")
            loadState = LoadState.NULL
            load_map()
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
            if (gameController.keyTracker.isPressed(KeyCode.w)) {
                player.PY += 20
            }
            if (gameController.keyTracker.isPressed(KeyCode.a)) {
                player.PX -= 20
            }
            if (gameController.keyTracker.isPressed(KeyCode.s)) {
                player.PY -= 20
            }
            if (gameController.keyTracker.isPressed(KeyCode.d)) {
                player.PX += 20
            }
            
            mainCamera.PX = player.PX
            mainCamera.PY = player.PY
            
            if (frame % Settings.updateIntervalFrames == 0L) {
                sendToServer("update_player_data", player.toJsonObject())
            }
            if (frame % Settings.pingInterval == 0L) {
                var json = JSONObject()
                json.put("time", System.currentTimeMillis())
                sendToServer("ping", json)
            }
        }
    }

    fun setLogicElement() {
        if (loadState == LoadState.STARTED) {
            var json = JSONObject()
            json.put("PX", player.PX)
            json.put("PY", player.PY)
            json.put("type", "new")
            sendToServer("add_logicElement", json)
        }
    }
    fun connect() {
        var json = JSONObject()
        json.put("name", name)
        sendToServer("loadState_connect_client", json)
    }
    
    fun get_pos() {
        sendToServer("loadState_getPos_client")
    }

    fun load_map() {
        sendToServer("loadState_loadMap_client")
    }

    fun destroy() {
        player.destroy()
        playersContainer.clear()
    }

    fun sendToServer(header: String, data: JSONObject = JSONObject()) {
        data.put("clientId", clientId)
        TransferPackage(header, clientId, TransferPackage.SERVER, data).send(clientTransferModel)
    }
}
