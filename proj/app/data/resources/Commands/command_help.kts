when (commandObject.configuratorId) {
    0 -> {
        gameController.serverController.serverChatController.addLocalMessage(
            commandObject.senderId,
            "System",
            "Список доступных команд",
            Settings.systemMessage
        )
        var commandsLoader = gameController.commandsLoader
        for (i in commandsLoader.commandsFilesLoader.commandsMap.keys) {
            var commandData = commandsLoader.commandsFilesLoader.commandsMap[i]!!

            gameController.serverController.serverChatController.addLocalMessage(
                commandObject.senderId,
                "",
                " - /${i} : ${commandData.description}",
                Settings.systemMessage2
            )
        }
        gameController.serverController.serverChatController.addLocalMessage(
            commandObject.senderId,
            "System",
            " /help <комманда> - более подробная информация про команду",
            Settings.systemMessage
        )
    }

    1 -> {
        var commandsLoader = gameController.commandsLoader
        var commandData = gameController.commandsLoader.getCommand(commandObject.args.getOrNull(0)!!)!!

        gameController.serverController.serverChatController.addLocalMessage(
            commandObject.senderId,
            "System",
            "Информация про команду ${commandObject.args.getOrNull(0)!!}",
            Settings.systemMessage
        )
        gameController.serverController.serverChatController.addLocalMessage(
            commandObject.senderId,
            "",
            " - ${commandData.description}",
            Settings.systemMessage2
        )
        for (i in commandData.commandVariations) {
            var text = " - /${commandObject.args.getOrNull(0)!!} "
            for (ii in i.vars) {
                text += "  <${ii}>"
            }
            gameController.serverController.serverChatController.addLocalMessage(
                commandObject.senderId,
                "",
                text,
                Settings.systemMessage2
            )
        }
    }
}

