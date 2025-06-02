package la.vok.GameController.Client

import la.vok.UI.Rendering
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
import la.vok.InputController.KeyCode
import processing.data.JSONObject
import java.net.URI

enum class LoadState {
    NULL,
    CONNECT,
    GET_POS,
    LOAD_MAP,
    LOAD_PLAYERS,
    STARTED
}

class ClientController(var gameController: GameController, var addres: String = "") {
    var logicMap = LogicMap(gameController)
    var mainCamera: Camera
    var clientId: String = Functions.getUniqueDeviceId()
    var name: String = "Name1234"
    var clientTransferModel: ClientTransferModel
    var frame: Long = 0L
    var loadState: LoadState = LoadState.NULL

    lateinit var onlineWebSocketClient: OnlineWebSocketClient

    var player: PlayerData = PlayerData(clientId, name, gameController)
    var playersContainer: PlayersContainer = PlayersContainer(gameController)
    
    var isLoaded = false;

    init {
        println("ClientController initialized")
        clientTransferModel = ClientTransferModel(this)
        mainCamera = Camera(0f, 0f, 1f)
    }

    fun initOnline() {
        onlineWebSocketClient = OnlineWebSocketClient(this, URI(addres))
        onlineWebSocketClient.connect()
    }

    fun start() {
        println("->ClientController started")
        logicMap.updateWireSet()
        isLoaded = true
    }

    fun tick() {
        frame++;

        if (loadState == LoadState.NULL) {
            loadState = LoadState.CONNECT
        } else if (loadState == LoadState.CONNECT) {
            println("LoadState.CONNECT")
            connect()
        } else if (loadState == LoadState.GET_POS) {
            println("LoadState.GET_POS")
            get_pos()
        } else if (loadState == LoadState.LOAD_MAP) {
            println("LoadState.LOAD_MAP")
            load_map()
        } else if (loadState == LoadState.LOAD_PLAYERS) {
            println("LoadState.LOAD_PLAYERS")
        } else if (loadState == LoadState.STARTED) {
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
            
            for (i in playersContainer.getAllPlayersWithout(clientId)) {
                i.updateOtherPlayer()
            }
            
            if (frame % Settings.updateIntervalFrames == 0L) {
                sendToServer("update_player_data", player.toJsonObject())
            }
        }
    }

    fun setLogicElement() {
        if (loadState == LoadState.STARTED) {
            var json = JSONObject()
            json.put("PX", player.PX)
            json.put("PY", player.PY)
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

    fun RenderLogicElements() {
        gameController.rendering.RenderLogicElements(clientController = this)
        gameController.rendering.RenderWires(clientController = this)
    }

    fun renderPlayer(player: PlayerData) {
        for (i in playersContainer.getAllPlayersWithout(clientId)) {
            gameController.rendering.renderPlayer(i, clientController = this)
        }
        gameController.rendering.renderPlayer(player, clientController = this)
    }

    fun destroy() {
        
    }


    fun sendToServer(header: String, data: JSONObject = JSONObject()) {
        data.put("clientId", clientId)
        TransferPackage(header, clientId, TransferPackage.SERVER, data).send(clientTransferModel)
    }
}
