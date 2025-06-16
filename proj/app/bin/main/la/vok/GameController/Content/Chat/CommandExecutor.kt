package la.vok.GameController.Content.Chat

import la.vok.GameController.GameController
import la.vok.GameController.Server.ServerController
import la.vok.Storages.Settings

class CommandExecutor(
    private val gameController: GameController,
    private val serverController: ServerController,
    private val argsClassifier: ArgsClassifier
) {

    fun handleCommand(autorName: String, rawText: String, sendMessage: (String, ChatMessage) -> Unit) {
        val parts = rawText.removePrefix("/").split(" ")
        val commandName = parts.firstOrNull()?.lowercase() ?: return
        val args = parts.drop(1)

        val autorID = serverController.connectsContainer.getIdByName(autorName)
            ?: return

        if (!gameController.loaders.commands.hasCommand(commandName)) {
            sendMessage(autorID, errorMessage("Неизвестная команда: $commandName"))
            return
        }

        val argTypes = argsClassifier.classifyArgs(args)
        val commandVariation = gameController.loaders.commands.getCommandVariation(commandName, argTypes)

        if (commandVariation == null) {
            val typeString = if (argTypes.isEmpty()) "null" else argTypes.joinToString(" ") { "<$it>" }
            sendMessage(
                autorID,
                errorMessage("Конфигурация $typeString для команды $commandName не найдена")
            )
            return
        }

        val confObject = gameController.ktsScriptManager.commandObject
        confObject.senderId = autorID
        confObject.sender = autorName
        confObject.commandName = commandName
        confObject.classifyArgs = ArrayList(argTypes)
        confObject.args = ArrayList(args)
        confObject.configuratorId = gameController.loaders.commands.getCommandVariationIndex(commandName, confObject.classifyArgs)
        gameController.ktsScriptManager.executeScript(commandVariation.script, "command")
    }

    private fun errorMessage(text: String): ChatMessage {
        return ChatMessage(gameController, "Error", text).apply {
            color = Settings.errorMessage
        }
    }
}
