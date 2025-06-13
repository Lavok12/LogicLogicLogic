when (commandObject.configuratorId) {
    0 -> {
        val ping = gameController.serverController.connectsContainer
            .getConnect(commandObject.senderId).ping
        gameController.serverController.serverChatController.addLocalMessage(
            commandObject.senderId,
            "System",
            "Ваш пинг: $ping мс",
            Settings.systemMessage
        )
    }
    1 -> {
        val name = commandObject.args.getOrNull(0) ?: "?"
        val targetId = gameController.serverController.connectsContainer.getIdByName(name)
        val ping = gameController.serverController.connectsContainer.getConnect(targetId!!).ping
        gameController.serverController.serverChatController.addLocalMessage(
            commandObject.senderId,
            "System",
            "Пинг игрока $name: $ping мс",
            Settings.systemMessage
        )
    }
}
