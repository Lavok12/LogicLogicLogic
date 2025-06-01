package la.vok.InputController

import processing.event.KeyEvent

class KeyTracker {
    private val pressedKeys = mutableSetOf<Int>()

    fun keyPressed(event: KeyEvent) {
        pressedKeys.add(event.keyCode)
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