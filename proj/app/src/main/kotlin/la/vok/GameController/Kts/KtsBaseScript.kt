package la.vok.GameController.Kts

import la.vok.GameController.GameController

abstract class KtsBaseScript(
    val gameController: GameController,
    val commandObject: CommandObject,
    val defaultObject: DefaultObject
)

class CommandObject(var gameController: GameController) {
    var sender = ""
    var senderId = ""
    var args = ArrayList<String>()
    var classifyArgs = ArrayList<String>()
    var configuratorId = 0
    var commandName = ""
}

class DefaultObject(var gameController: GameController) {

}