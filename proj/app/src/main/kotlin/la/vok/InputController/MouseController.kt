package la.vok.InputController

import la.vok.GameController.*
import processing.event.MouseEvent

class MouseController(private val gameController: GameController) {
    var moux: Float = 0f
    var mouy: Float = 0f
    var pmoux: Float = 0f
    var pmouy: Float = 0f

    companion object {
        var LEFT = 37
        var RIGHT = 39
        var CENTER = 3
    }

    private val pressedButtons = mutableSetOf<Int>()

    fun updateCoord(moux: Float, mouy: Float) { 
        this.moux = moux
        this.mouy = mouy
    }
    fun updateCoord(moux: Float, mouy: Float, pmoux: Float, pmouy: Float) {
        this.pmoux = pmoux
        this.pmouy = pmouy
        this.moux = moux
        this.mouy = mouy
    }

    fun mousePressed(event: MouseEvent) {
        updateCoord(event.x.toFloat(), event.y.toFloat())

        pressedButtons.add(event.button)
    }

    fun mouseReleased(event: MouseEvent) {
        updateCoord(event.x.toFloat(), event.y.toFloat())
        pressedButtons.remove(event.button)
    }

    fun mouseClicked(event: MouseEvent) {
    }

    fun mouseMoved(event: MouseEvent) {
        updateCoord(event.x.toFloat(), event.y.toFloat())
    }

    fun mouseDragged(event: MouseEvent) {
        updateCoord(event.x.toFloat(), event.y.toFloat())
    }

    fun mouseWheel(event: MouseEvent) {
    }

    // Вспомогательные методы

    fun isButtonPressed(button: Int): Boolean {
        return pressedButtons.contains(button)
    }

    fun getPressedButtons(): Set<Int> {
        return pressedButtons.toSet() // безопасная копия
    }

    fun isAnyButtonPressed(): Boolean {
        return pressedButtons.isNotEmpty()
    }
}
