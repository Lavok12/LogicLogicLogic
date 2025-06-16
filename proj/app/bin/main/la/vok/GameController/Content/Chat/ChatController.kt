package la.vok.GameController.Content.Chat

import la.vok.GameController.*
import la.vok.LavokLibrary.copy
import java.awt.Color

open class ChatController (var gameController: GameController, var maxHistorySize: Int) {
    var history = ArrayList<ChatMessage>()
    var count = -1

    open fun addMessage(autor: String, text: String, color: Color) {
        var cm = ChatMessage(gameController, autor, text)
        cm.color = color.copy()
        addMessage(cm)
    }
    protected open fun addMessage(chatMessage: ChatMessage) {
        count++
        chatMessage.number = count
        history += chatMessage
        if (history.size == maxHistorySize) {
            if (history[0].textElement != null) {
                history[0].textElement!!.parentCanvas.removeElement(history[0].textElement!!)
            }
            history.removeAt(0)
        }
    }
    open fun getLastMessage(): ChatMessage? {
        return if (history.isNotEmpty()) history.last() else null
    }
}