package la.vok.GameController.Content.Chat

import la.vok.GameController.*

open class ChatController (var gameController: GameController, var maxHistorySize: Int) {
    var history = ArrayList<ChatMessage>()

    open fun addMessage(autor: String, text: String) {
        addMessage(ChatMessage(gameController, autor, text))
    }
    open fun addMessage(chatMessage: ChatMessage) {
        history += chatMessage
        if (history.size == maxHistorySize) {
            history.removeAt(0)
        }
    }
}