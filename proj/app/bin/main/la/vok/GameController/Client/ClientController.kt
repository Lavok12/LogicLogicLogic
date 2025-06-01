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
import la.vok.InputController.KeyCode
import processing.data.JSONObject
import java.net.URI

class ClientController(var gameController: GameController) {
    var logicMap = LogicMap(gameController)
    var mainCamera: Camera
    var clientId: String = Functions.getUniqueDeviceId()
    var name: String = "Name1234"
    var clientTransferModel: ClientTransferModel
    var frame: Long = 0L

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
        onlineWebSocketClient = OnlineWebSocketClient(this, URI("ws://26.229.52.4:8800"))
        onlineWebSocketClient.connect()
    }

    fun start() {
        println("->ClientController started")
        connect()
        logicMap.updateWireSet()
        isLoaded = true
    }

    fun tick() {
        frame++;

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
            TransferPackage("update_player_data", clientId, TransferPackage.SERVER, player.toJsonObject()).send(clientTransferModel)
        }
    }

    fun connect() {
        var json = JSONObject()
        json.put("name", name)
        TransferPackage("connect_new_player", clientId, TransferPackage.SERVER, json).send(clientTransferModel)
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
}
