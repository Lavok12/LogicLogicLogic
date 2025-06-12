package la.vok.GameController.Content.Chat

import la.vok.GameController.Client.ClientController
import la.vok.Storages.Settings
import la.vok.UI.Elements.LText
import java.awt.Color

class ClientChatController(var clientController : ClientController, maxHistorySize: Int) : ChatController (clientController.gameController, maxHistorySize) {
    var UIhistory = ArrayList<ChatMessage>()
    override fun addMessage(autor: String, text: String, r: Int, g: Int, b: Int) {
        var cm = ChatMessage(gameController, autor, text)
        cm.r = r
        cm.g = g
        cm.b = b
        addMessage(cm)
    }
    override fun addMessage(chatMessage: ChatMessage) {
        super.addMessage(chatMessage)
        UIhistory += chatMessage
        addMesageToCanvas(chatMessage)
    }

    fun addMesageToCanvas(chatMessage: ChatMessage) {
        var le = clientController.gameController.getCanvas().getElementByTag("chatPanel")
        if (le != null) {
            var textE = le.elementCanvas.addChild("chatText", "text${count}") as LText
            chatMessage.textElement = textE
            chatMessage.updateElement()
        }
    }

    fun removeOldMessages() {
        val currentTime = System.currentTimeMillis()
        val iterator = UIhistory.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (currentTime - Settings.clientChatHistoryTime > item.time) {
                if (item.textElement != null) {
                    item.textElement!!.parentCanvas.removeElement(item.textElement!!)
                    item.textElement = null
                }
                iterator.remove()
            }
        }
    }
}