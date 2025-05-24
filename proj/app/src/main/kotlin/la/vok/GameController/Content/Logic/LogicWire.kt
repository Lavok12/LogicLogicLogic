package la.vok.gameController.Content.Logic

import la.vok.GameController.Content.Logic.LogicElement

class LogicWire(
    var start: LogicElement,
    var end: LogicElement,
    var gameController: la.vok.GameController.GameController,
) {
    var isActive: Boolean = false
    
    fun activate() {
        isActive = true
    }
    fun deactivate() {
        isActive = false
    }
}