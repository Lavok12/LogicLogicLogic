package la.vok.InputController

import processing.event.KeyEvent
import la.vok.GameController.*
import la.vok.GameController.Client.LoadState

class KeyTracker(val gameController: GameController) {
    private val pressedKeys = mutableSetOf<Int>()

    fun keyPressed(event: KeyEvent) {
        pressedKeys.add(event.keyCode)
        if (event.keyCode == KeyCode.SPACE) {
            if (gameController.gameStarted && gameController.clientState == ClientState.STARTED) {
                if (gameController.clientController.loadState == LoadState.STARTED) {
                    gameController.clientController.setLogicElement()
                }
            }
        }
        if (event.keyCode == KeyCode.x) {
            gameController.destroyClient()
        }
        if (event.keyCode == KeyCode.z) {
            gameController.destroyServer()
        }
        println(event.key + " ! " + event.keyCode)
    }

    fun keyReleased(event: KeyEvent) {
        pressedKeys.remove(event.keyCode)
    }

    fun isPressed(keyCode: Int): Boolean {
        return keyCode in pressedKeys
    }

    fun getPressedKeys(): Set<Int> {
        return pressedKeys.toSet()
    }
}