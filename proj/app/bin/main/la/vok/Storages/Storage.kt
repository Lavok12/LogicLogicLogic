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

    var mouseController: la.vok.InputController.MouseController
    get() {
        return gameController.mouseController
    }
    set(value) {
        gameController.mouseController = value
    }
}
