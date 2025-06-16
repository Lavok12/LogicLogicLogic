package la.vok.GameController.Content.Chat

import la.vok.GameController.Server.ServerController
import la.vok.LavokLibrary.copy
import processing.data.JSONObject
import java.awt.Color

class ServerChatController(
    var serverController: ServerController,
    maxHistorySize: Int
) : ChatController(serverController.gameController, maxHistorySize) {

    private val argsClassifier = ArgsClassifier(serverController)
    private val commandExecutor = CommandExecutor(gameController, serverController, argsClassifier)

    fun addLocalMessage(id: String, autor: String, text: String, color: Color) {
        val cm = ChatMessage(gameController, autor, text)
        cm.color = color.copy()
        addLocalMessage(id, cm)
    }

    override fun addMessage(autor: String, text: String, color: Color) {
        if (text.startsWith("/")) {
            commandExecutor.handleCommand(autor, text, this::addLocalMessage)
        } else {
            val cm = ChatMessage(gameController, autor, text)
            cm.color = color.copy()
            addMessage(cm)
        }
    }

    override fun addMessage(chatMessage: ChatMessage) {
        println("MESSAGE: ${chatMessage.getFullText()}")
        val json = JSONObject()
        json.put("data", chatMessage.getRawData())
        serverController.serverFunctions.sendToAll("new_message", json)
        super.addMessage(chatMessage)
    }

    fun addLocalMessage(id: String, chatMessage: ChatMessage) {
        println("MESSAGE: ${chatMessage.getFullText()}")
        val json = JSONObject()
        json.put("data", chatMessage.getRawData())
        serverController.serverFunctions.sendToClient("new_message", id, json)
        super.addMessage(chatMessage)
    }
}
