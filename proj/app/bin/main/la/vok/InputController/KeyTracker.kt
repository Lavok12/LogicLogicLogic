package la.vok.InputController

import processing.event.KeyEvent
import la.vok.GameController.*

class KeyTracker(var gameController: GameController) {
    private val pressedKeys = mutableSetOf<Int>()

    var lastKeyEvent: KeyEvent? = null

    fun keyPressed(event: KeyEvent) {
        pressedKeys.add(event.keyCode)

        if (gameController.textFieldController.isEditing) {
            gameController.textFieldController.input(event)
            gameController.textFieldController.repeatCounter = 0
            lastKeyEvent = event
        }

        if (!gameController.textFieldController.isEditing) {
            when (event.keyCode) {
                KeyCode.SPACE -> {
                    if (gameController.gameStarted &&
                        gameController.clientState == ClientState.STARTED &&
                        gameController.clientController.connect_success
                    ) {
                        gameController.clientController.clientFunctions.setLogicElement()
                    }
                }
                KeyCode.x -> gameController.destroyClient()
                KeyCode.z -> gameController.destroyServer()
            }
        }

        println("${event.key} ! ${event.keyCode}")
    }

    fun keyReleased(event: KeyEvent) {
        pressedKeys.remove(event.keyCode)

        if (lastKeyEvent?.keyCode == event.keyCode) {
            lastKeyEvent = null
            gameController.textFieldController.repeatCounter = 0
        }
    }

    fun isPressed(keyCode: Int): Boolean {
        return keyCode in pressedKeys
    }

    fun getPressedKeys(): Set<Int> {
        return pressedKeys.toSet()
    }

    fun tick() {
        if (!gameController.textFieldController.isEditing) return
        gameController.textFieldController.tick(this)
        
    }
}
