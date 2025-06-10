package la.vok.GameController.Server

import la.vok.GameController.Content.Chat.*
import processing.data.JSONObject

class ServerChatController(var serverController : ServerController, maxHistorySize: Int) : ChatController (serverController.gameController, maxHistorySize) {
    override fun addMessage(autor: String, text: String) {
        addMessage(ChatMessage(gameController, autor, text))
    }
    override fun addMessage(chatMessage: ChatMessage) {
        println("MESSAGE: ${chatMessage.getFullText()}")
        var json: JSONObject = JSONObject()
        json.put("data", chatMessage.getRawData())
        serverController.sendToAll("new_message", json)
        super.addMessage(chatMessage)
    }
}