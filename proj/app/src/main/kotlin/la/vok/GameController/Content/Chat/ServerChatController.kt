package la.vok.GameController.Content.Chat

import la.vok.GameController.Server.ServerController
import processing.data.JSONObject

class ServerChatController(var serverController : ServerController, maxHistorySize: Int) : ChatController (serverController.gameController, maxHistorySize) {
    val commands = mutableMapOf<String, ChatCommand>()

    init {
        registerCommand(HelpCommand(serverController))
        registerCommand(PingCommand(serverController))
        registerCommand(TeleportCommand(serverController))
        registerCommand(WhoCommand(serverController))
        registerCommand(SpawnCommand(serverController))
        registerCommand(KickCommand(serverController))
    }

    private fun registerCommand(command: ChatCommand) {
        commands[command.name.lowercase()] = command
    }

    override fun addMessage(autor: String, text: String, r: Int, g: Int, b: Int) {
        if (text.startsWith("/")) {
            handleCommand(autor, text)
        } else {
            var cm = ChatMessage(gameController, autor, text)
            cm.r = r
            cm.g = g
            cm.b = b
            addMessage(cm)
        }
    }

    private fun handleCommand(autor: String, text: String) {
        val parts = text.removePrefix("/").split(" ")
        val commandName = parts.firstOrNull()?.lowercase() ?: return
        val args = parts.drop(1)

        val command = commands[commandName]
        if (command != null) {
            command.execute(args, ChatContext(this, autor))
        } else {
            addMessage("System", "Неизвестная команда: $commandName")
        }
    }

    override fun addMessage(chatMessage: ChatMessage) {
        println("MESSAGE: ${chatMessage.getFullText()}")
        var json: JSONObject = JSONObject()
        json.put("data", chatMessage.getRawData())
        serverController.serverFunctions.sendToAll("new_message", json)
        super.addMessage(chatMessage)
    }

    fun lastMessageSetColor(f1: Int, f2: Int, f3: Int) {
        getLastMessage()!!.updateElement(f1, f2, f3)
    }
}