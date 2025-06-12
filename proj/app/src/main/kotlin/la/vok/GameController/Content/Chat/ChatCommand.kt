package la.vok.GameController.Content.Chat

import la.vok.GameController.GameController
import la.vok.GameController.Server.ServerController

abstract class ChatCommand(val serverController: ServerController) {
    abstract val name: String
    abstract val description: String
    abstract fun execute(args: List<String>, context: ChatContext)
}

data class ChatContext(
    val controller: ServerChatController,
    val sender: String
)
