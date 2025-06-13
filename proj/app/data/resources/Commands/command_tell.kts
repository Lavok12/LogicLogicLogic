val name = commandObject.args.getOrNull(0) ?: "?"
val targetId = gameController.serverController.connectsContainer.getIdByName(name)!!

var text = ""
for (i in 1..commandObject.args.size-1) {
    text += "${commandObject.args.getOrNull(i)!!} "
}
gameController.serverController.serverChatController.addLocalMessage(
    targetId,
    "${commandObject.sender}",
    "шепчет вам: $text",
    Settings.localMessage
)
gameController.serverController.serverChatController.addLocalMessage(
    commandObject.senderId,
    "",
    " - вы прошептали $name: $text",
    Settings.localMessage
)