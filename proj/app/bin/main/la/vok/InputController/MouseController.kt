package la.vok.InputController

import la.vok.GameController.*
import processing.event.MouseEvent
import la.vok.Storages.*

class MouseController(val gameController: GameController) {
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

    fun updateCoord(mouseX: Float, mouseY: Float) { 
        this.moux = (mouseX - Storage.gameController.mainRender.disW2) * (Storage.lg.disW / Storage.gameController.mainRender.disW)
        this.mouy = (-mouseY + Storage.gameController.mainRender.disH2) * (Storage.lg.disH / Storage.gameController.mainRender.disH)
    }
    fun updateCoord(mouseX: Float, mouseY: Float, pmouseX: Float, pmouseY: Float) {
        this.moux = (mouseX - Storage.gameController.mainRender.disW2) * (Storage.lg.disW / Storage.gameController.mainRender.disW)
        this.mouy = (-mouseY + Storage.gameController.mainRender.disH2) * (Storage.lg.disH / Storage.gameController.mainRender.disH)
        this.pmoux = (pmouseX - Storage.gameController.mainRender.disW2) * (Storage.lg.disW / Storage.gameController.mainRender.disW)
        this.pmouy = (-pmouseY + Storage.gameController.mainRender.disH2) * (Storage.lg.disH / Storage.gameController.mainRender.disH)
    }

    fun mousePressed(event: MouseEvent) {
        updateCoord(event.x.toFloat(), event.y.toFloat())
        pressedButtons.add(event.button)
        gameController.lCanvasController.mouseClicked(this)
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
