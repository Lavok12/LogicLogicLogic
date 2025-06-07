package la.vok.InputController

import la.vok.GameController.GameController
import la.vok.UI.Elements.*
import processing.event.KeyEvent

class TextFieldController(var gameController: GameController) {
    var lTextField: LTextField? = null
    var isEditing = false

    fun input(key: KeyEvent) {
        if (isEditing) {
            lTextField!!.inputString += key.key
        }
    }
    fun startEditing(lTextField: LTextField) {
        isEditing = true
        this.lTextField = lTextField
    }
    fun stopEditing() {
        isEditing = false
    }
}