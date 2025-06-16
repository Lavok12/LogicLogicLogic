package la.vok.GameController.Client

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import la.vok.GameController.TransferModel.TransferPackage

class OnlineWebSocketClient(var clientController: ClientController, serverUri: URI) : WebSocketClient(serverUri) {
    var connected = false
    var onError = false

    init {
        println("OnlineWebSocketClient init")
    }

    override fun onOpen(handshakedata: ServerHandshake?) {
        connected = true
        println("Connected to the server")
    }

    override fun onMessage(message: String?) {
        try {
            clientController.clientTransferModel.clientTransferUpdater.transferBuffer.add(TransferPackage.fromString(message!!))
        } catch (e: Exception) {
            println("Error processing message: ${e.message}")
        }
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        println("Disconnected: $reason")
        clientController.gameController.destroyGame()
    }

    override fun onError(ex: Exception?) {
        println("Error: ${ex?.message}")
        onError = true
    }
}
