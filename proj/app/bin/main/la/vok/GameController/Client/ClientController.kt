package la.vok.GameController.Client

import ClientProcess
import la.vok.GameController.GameController;
import la.vok.Storages.Storage
import la.vok.Storages.Settings
import la.vok.GameController.Content.Map.LogicMap
import la.vok.GameController.Content.PlayersContainer
import la.vok.LavokLibrary.Functions
import la.vok.GameController.TransferModel.*
import la.vok.GameController.Content.PlayerData
import la.vok.GameController.Client.Rendering.*
import la.vok.GameController.Content.Chat.ClientChatController
import la.vok.GameController.Processes.ClientProcesses.ClientLoadProcess
import la.vok.GameController.Processes.InterruptionManager
import la.vok.GameController.Processes.ProcessesManager
import la.vok.InputController.KeyCode
import la.vok.LavokLibrary.MetaStorage
import processing.data.JSONObject
import java.net.URI

class ClientController(var gameController: GameController, var address: String = "") : MetaStorage() {
    var logicMap = LogicMap(gameController)
    var mainCamera: Camera
    var clientId: String = Functions.getUniqueDeviceId()
    var name: String = Storage.name
    var frame: Long = -1L

    var isLoaded = false;
    lateinit var onlineWebSocketClient: OnlineWebSocketClient
    var player: PlayerData
    var playersContainer: PlayersContainer
    var clientChatController: ClientChatController
    var clientTransferModel: ClientTransferModel
    var clientFunctions: ClientFunctions = ClientFunctions(this)

    var serverConnect: ServerConnect = ServerConnect(this)

    val interruptions = InterruptionManager<ClientProcess>()
    val processes = ProcessesManager<ClientProcess>()

    var clientLoadProcess: ClientLoadProcess? = null

    var connect_success: Boolean = false

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
        println("->ClientController started")
        isLoaded = true
        clientLoadProcess = ClientLoadProcess(this, gameController)
        interruptions.add(clientLoadProcess!!)
    }

    fun tick() {
        frame++;
        processes.tick()

        clientTransferModel.clientTransferUpdater.transferBuffer.processingAll()
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
