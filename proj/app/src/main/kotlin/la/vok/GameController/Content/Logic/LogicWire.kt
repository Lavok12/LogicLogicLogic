package la.vok.GameController.Content.Logic

import la.vok.GameController.Content.Logic.LogicElement
import la.vok.GameController.Client.Camera
import la.vok.UI.Rendering

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
    fun render(camera: Camera, rendering: Rendering) {
        rendering.lg.fill(255f,0f,0f)
        rendering.lg.setLine(
            camera.camX(start.PX + 5f),
            camera.camY(start.PY + 5f),
            camera.camX(end.PX + 5f),
            camera.camY(end.PY + 5f),
            camera.camZ(5f),
            200f,
            100f,
            100f
        )
    }
}