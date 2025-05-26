package la.vok.Storages


object Storage {
    lateinit var main: la.vok.App
    lateinit var gameController: la.vok.GameController.GameController

    var lg: la.vok.LavokLibrary.LGraphics
    get() {
        return gameController.rendering.lg
    }
    set(value) {
        gameController.rendering.lg = value
    }

    var fix: Float = 0f
    var mouy: Float = 0f
    var moux: Float = 0f
    var pmouy: Float = 0f
    var pmoux: Float = 0f
}
