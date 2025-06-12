package la.vok.GameController.Content.Chat

import la.vok.GameController.GameController
import la.vok.GameController.Server.ServerController

class HelpCommand(
    serverController: ServerController,
) : ChatCommand(serverController) {

    override val name = "help"
    override val description = "Показать список доступных команд или подробности о конкретной"

    override fun execute(args: List<String>, context: ChatContext) {
        var id = serverController.connectsContainer.getIdByName(context.sender)!!
        when (args.size) {
            0 -> {
                context.controller.addLocalMessage(id, "System", "Доступные команды:", 180,200,220)
                var commands = ""
                var index = 0
                for (i in serverController.serverChatController.commands) {
                    index++
                    commands += "/${serverController.serverChatController.commands[i.key]!!.name}    "
                    if (index % 3 == 0) {
                        context.controller.addLocalMessage(id, "", " - " + commands, 120,230,120)
                        commands = ""
                    }
                }
                if (commands != "") {
                    context.controller.addLocalMessage(id, "System", " - " + commands, 120,230,120)
                }
            }

            1 -> {
                val commandName = args[0].lowercase()
                val command = serverController.serverChatController.commands[commandName]
                if (command != null) {
                    context.controller.addMessage("System", "Описание команды /$commandName:",180,230,220)
                    val lines = command.description.trim().split("\n")
                    for (line in lines) {
                        context.controller.addLocalMessage(id, "", " - $line", 120, 230, 120)
                    }
                } else {
                    context.controller.addLocalMessage(id, "System", "Команда '/$commandName' не найдена.",230,50,50)
                }
            }

            else -> {
                context.controller.addLocalMessage(id, "System", "Использование: /help [команда]",230,50,50)
            }
        }
    }
}


class PingCommand(serverController: ServerController) : ChatCommand(serverController) {
    override val name = "ping"
    override val description = """
        Проверить пинг
        /ping — ваш пинг
        /ping <игрок> — пинг другого игрока
    """.trimIndent()

    override fun execute(args: List<String>, context: ChatContext) {
        var id = serverController.connectsContainer.getIdByName(context.sender)!!
        val connects = serverController.connectsContainer

        when (args.size) {
            0 -> {
                if (id == null) {
                    context.controller.addLocalMessage(id, "System", "Не удалось определить ваш ID.", 230, 50, 50)
                    return
                }
                val ping = connects.getConnect(id).ping
                context.controller.addLocalMessage(id, "System", "Ваш пинг: $ping мс", 180,230,220)
            }

            1 -> {
                val targetName = args[0]
                val targetId = connects.getIdByName(targetName)
                if (targetId == null) {
                    context.controller.addLocalMessage(id, "System", "Игрок '$targetName' не найден.", 230, 50, 50)
                    return
                }
                val ping = connects.getConnect(targetId).ping
                context.controller.addLocalMessage(id, "System", "Пинг игрока '$targetName': $ping мс", 180,230,220)
            }

            else -> {
                context.controller.addLocalMessage(id, "System", "Использование: /ping [игрок]", 230, 50, 50)
            }
        }
    }
}


class TeleportCommand(serverController: ServerController) : ChatCommand(serverController) {
    override val name = "tp"
    override val description = """
        Телепортация игроков
        /tp <x> <y> — телепортироваться в координаты
        /tp <игрок> — телепортироваться к игроку
        /tp <игрок1> <игрок2> — телепортировать игрока к другому
        /tp <игрок> <x> <y> — телепортировать игрока в координаты
        """.trimIndent()

    override fun execute(args: List<String>, context: ChatContext) {
        var id = serverController.connectsContainer.getIdByName(context.sender)!!
        val connects = serverController.connectsContainer

        fun getId(name: String): String? = connects.getIdByName(name)
        fun getPos(id: String) = connects.getConnect(id).playerData
        fun teleport(id: String, x: Float, y: Float) {
            serverController.serverFunctions.playerSetPosition(id, x, y)
        }

        when (args.size) {
            1 -> {
                val targetId = getId(args[0])
                val senderId = getId(context.sender)
                if (targetId == null || senderId == null) {
                    context.controller.addLocalMessage(id, "System", "Игрок '${args[0]}' не найден.", 230, 50, 50)
                    return
                }
                val targetPos = getPos(targetId)
                teleport(senderId, targetPos.PX, targetPos.PY)
                context.controller.addLocalMessage(id, "System", "Вы телепортированы к '${args[0]}'.",180,230,220)
                context.controller.addMessage("System", "Игрок '${context.sender}' телепортирован к '${args[0]}'.",150,150,150)
            }

            2 -> {
                val x = args[0].toFloatOrNull()
                val y = args[1].toFloatOrNull()
                if (x != null && y != null) {
                    val senderId = getId(context.sender)
                    if (senderId == null) {
                        context.controller.addLocalMessage(id, "System", "Не удалось найти ваш ID.", 230, 50, 50)
                        return
                    }
                    teleport(senderId, x, y)
                    context.controller.addLocalMessage(id, "System", "Вы телепортированы в ($x, $y).",180,230,220)
                    context.controller.addMessage("System", "Игрок '${context.sender}' телепортирован в ($x, $y).",150,150,150)
                } else {
                    val fromId = getId(args[0])
                    val toId = getId(args[1])
                    if (fromId == null) {
                        context.controller.addLocalMessage(id, "System", "Игрок '${args[0]}' не найден.", 230, 50, 50)
                        return
                    }
                    if (toId == null) {
                        context.controller.addLocalMessage(id, "System", "Игрок '${args[1]}' не найден.", 230, 50, 50)
                        return
                    }
                    val toPos = getPos(toId)
                    teleport(fromId, toPos.PX, toPos.PY)
                    context.controller.addLocalMessage(serverController.connectsContainer.getIdByName(args[0])!!, "System", "Вы телепортированы в ($x, $y).",180,230,220)
                    context.controller.addMessage("System", "Игрок '${args[0]}' телепортирован к '${args[1]}'.",150,150,150)
                }
            }

            3 -> {
                val targetId = getId(args[0])
                val x = args[1].toFloatOrNull()
                val y = args[2].toFloatOrNull()
                if (targetId == null) {
                    context.controller.addLocalMessage(id, "System", "Игрок '${args[0]}' не найден",180,230,220)
                    return
                }
                if (x == null || y == null) {
                    context.controller.addLocalMessage(id, "System", "Координаты некорректны",180,230,220)
                    return
                }
                teleport(targetId, x, y)
                context.controller.addMessage("System", "Игрок '${args[0]}' телепортирован в ($x, $y).",150,150,150)
            }

            else -> {
                context.controller.addLocalMessage(id, "System", "Ошибка синтаксиса",230,50,50)
                context.controller.addLocalMessage(id, "System", "/help tp",180,230,220)
            }
        }
    }
}

class WhoCommand(serverController: ServerController) : ChatCommand(serverController) {
    override val name = "who"
    override val description = "Показать список онлайн-игроков"

    override fun execute(args: List<String>, context: ChatContext) {
        var id = serverController.connectsContainer.getIdByName(context.sender)!!
        val onlineNames = serverController.connectsContainer.getAllNames()
        if (onlineNames.isEmpty()) {
            context.controller.addLocalMessage(id, "System", "На сервере нет игроков.", 180, 230, 220)
        } else {
            context.controller.addLocalMessage(id, "System", "Онлайн (${onlineNames.size}):", 180, 230, 220)
            val list = onlineNames.joinToString(", ")
            context.controller.addLocalMessage(id, "", " - "+list, 120, 230, 120)
        }
    }
}

class SpawnCommand(serverController: ServerController) : ChatCommand(serverController) {
    override val name = "spawn"
    override val description = "Телепортироваться в точку спавна (0, 0)"

    override fun execute(args: List<String>, context: ChatContext) {
        var id = serverController.connectsContainer.getIdByName(context.sender)!!
        if (id == null) {
            context.controller.addLocalMessage(id, "System", "Не удалось определить ваш ID.", 230, 50, 50)
            return
        }

        serverController.serverFunctions.playerSetPosition(id, 0f, 0f)
        context.controller.addLocalMessage(id, "System", "Вы телепортированы в точку спавна (0, 0).", 180, 230, 220)
        context.controller.addMessage("System", "${context.sender} телепортирован в точку спавна (0, 0).", 150, 150, 150)
    }
}

class KickCommand(serverController: ServerController) : ChatCommand(serverController) {
    override val name = "kick"
    override val description = """
        Отключить игрока с сервера
        /kick <игрок> — отключить игрока
    """.trimIndent()

    override fun execute(args: List<String>, context: ChatContext) {
        var id = serverController.connectsContainer.getIdByName(context.sender)!!
        if (args.size != 1) {
            context.controller.addLocalMessage(id, "System", "Использование: /kick <игрок>", 230, 50, 50)
            return
        }

        val targetName = args[0]
        val targetId = serverController.connectsContainer.getIdByName(targetName)

        if (targetId == null) {
            context.controller.addLocalMessage(id, "System", "Игрок '$targetName' не найден.", 230, 50, 50)
            return
        }

        serverController.serverFunctions.disconnectPlayer(targetId)
        context.controller.addMessage("System", "Игрок '$targetName' был отключён.", 150, 150, 150)
    }
}
