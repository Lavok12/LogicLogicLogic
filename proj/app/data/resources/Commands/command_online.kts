val connects = gameController.serverController.connectsContainer

val allNames = connects.getAllNames()
gameController.serverController.serverChatController.addLocalMessage(
    commandObject.senderId,
    "System",
    "Количество игроков: ${allNames.size}",
    Settings.systemMessage
)
var id = 0
var print = " - "
for (i in allNames) {
    id++
    print += i
    print += "  "
    if (id % 3 == 0) {
        gameController.serverController.serverChatController.addLocalMessage(
            commandObject.senderId,
            "",
            "$print",
            Settings.systemMessage2
        )
        var print = " - "
        id = 0
    }
}
if (print != " - ") {
    gameController.serverController.serverChatController.addLocalMessage(
        commandObject.senderId,
        "",
        "$print",
        Settings.systemMessage2
    )
}