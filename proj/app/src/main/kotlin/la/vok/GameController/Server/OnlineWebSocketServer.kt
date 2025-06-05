package la.vok.GameController.Server

import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress
import la.vok.GameController.TransferModel.TransferPackage

class OnlineWebSocketServer(var serverController: ServerController, port: Int) : WebSocketServer(InetSocketAddress(port)) {
    init {
        println("OnlineWebSocketServer init")
    }
    
    override fun onOpen(conn: WebSocket, handshake: ClientHandshake) {
        println("Client connected: ${conn.remoteSocketAddress}")
    }

    override fun onClose(conn: WebSocket, code: Int, reason: String, remote: Boolean) {
        println("Client disconnected: ${conn.remoteSocketAddress}")
    }

    override fun onMessage(conn: WebSocket, message: String) {
        try {
            serverController.serverTransferModel.serverTransferUpdater.transferBuffer.add(TransferPackage.fromString(message!!))
        } catch (e: Exception) {
            println("Error processing message: ${e.message}")
        }
    }

    override fun onError(conn: WebSocket?, ex: Exception) {
        println("Error: ${ex.message}")
    }

    override fun onStart() {
        println("Server started on port ${this.port}")
    }
}
