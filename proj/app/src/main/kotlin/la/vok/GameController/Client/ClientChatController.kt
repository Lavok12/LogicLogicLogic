package la.vok.GameController.Client

import la.vok.GameController.Content.Chat.*
import processing.data.JSONObject
import la.vok.Storages.*

class ClientChatController(var clientController : ClientController, maxHistorySize: Int) : ChatController (clientController.gameController, maxHistorySize) {
    override fun addMessage(autor: String, text: String) {
        addMessage(ChatMessage(gameController, autor, text))
    }
    override fun addMessage(chatMessage: ChatMessage) {
        super.addMessage(chatMessage)
    }

    fun gerLatestMesssages() : ArrayList<ChatMessage> {
        var ret = ArrayList<ChatMessage>()
        var time = System.currentTimeMillis()
        for (i in history.reversed()) {
            if (time - Settings.clientChatHistoryTime < i.time) {
                ret += i
            } else {
                break
            }
        }
        return ret
    }
}