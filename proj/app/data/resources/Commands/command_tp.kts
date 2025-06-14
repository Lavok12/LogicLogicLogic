when (commandObject.configuratorId) {
    0 -> {
        val name = commandObject.args.getOrNull(0) ?: "?"
        val targetId = gameController.serverController.connectsContainer.getIdByName(name)
        val player = gameController.serverController.connectsContainer.getPlayerData(targetId!!)!!
        gameController.serverController.serverFunctions.playerSetPosition(commandObject.senderId, player.pos
        )
        gameController.serverController.serverChatController.addLocalMessage(
            commandObject.senderId,
            "System",
            "Вы телепортированы к $name",
            Settings.localMessage
        )
        gameController.serverController.serverChatController.addMessage(
            "System",
            "Игрок ${commandObject.sender} телепортирован к $name",
            Settings.systemMessage
        )

    }

    1 -> {
        val pos1 = commandObject.args.getOrNull(0) ?: "0"
        val pos2 = commandObject.args.getOrNull(1) ?: "0"

        gameController.serverController.serverFunctions.playerSetPosition(
            commandObject.senderId,
            Vec2(pos1.toFloat() ?: 0f,
            pos2.toFloat() ?: 0f)
        )
        gameController.serverController.serverChatController.addLocalMessage(
            commandObject.senderId,
            "System",
            "Вы телепортированы в $pos1, $pos2",
            Settings.localMessage
        )
        gameController.serverController.serverChatController.addMessage(
            "System",
            "Игрок ${commandObject.sender} телепортирован в $pos1, $pos2",
            Settings.systemMessage
        )
    }

    2 -> {
        val name1 = commandObject.args.getOrNull(0) ?: "?"
        val id1 = gameController.serverController.connectsContainer.getIdByName(name1)!!
        val name = commandObject.args.getOrNull(1) ?: "?"
        val targetId = gameController.serverController.connectsContainer.getIdByName(name)!!
        val player = gameController.serverController.connectsContainer.getPlayerData(targetId!!)!!
        gameController.serverController.serverFunctions.playerSetPosition(id1, player.pos)
        gameController.serverController.serverChatController.addLocalMessage(
            id1,
            "System",
            "Вы телепортированы к $name",
            Settings.localMessage
        )
        gameController.serverController.serverChatController.addMessage(
            "System",
            "Игрок ${name1} телепортирован к $name",
            Settings.systemMessage
        )
    }

    3 -> {
        val name1 = commandObject.args.getOrNull(0) ?: "?"
        val pos1 = commandObject.args.getOrNull(1) ?: "0"
        val pos2 = commandObject.args.getOrNull(2) ?: "0"

        val id1 = gameController.serverController.connectsContainer.getIdByName(name1)!!

        val x = pos1.toFloatOrNull() ?: 0f
        val y = pos2.toFloatOrNull() ?: 0f

        gameController.serverController.serverFunctions.playerSetPosition(id1, Vec2(x, y))
        gameController.serverController.serverChatController.addLocalMessage(
            id1,
            "System",
            "Вы телепортированы в $x, $y",
            Settings.localMessage
        )
        gameController.serverController.serverChatController.addMessage(
            "System",
            "Игрок $name1 телепортирован в $x, $y",
            Settings.systemMessage
        )
    }
}