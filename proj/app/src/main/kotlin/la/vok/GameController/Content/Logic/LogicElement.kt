package la.vok.GameController.Content.Logic

import la.vok.GameController.Client.Camera
import la.vok.Storages.Storage
import la.vok.LavokLibrary.LGraphics
import la.vok.GameController.Content.Logic.LogicWire
import la.vok.UI.Rendering

class LogicElement(
    var PX: Float,
    var PY: Float,
    var type: String,
    var gameController: la.vok.GameController.GameController,
) {
    var input = ArrayList<LogicWire>();
    var output = ArrayList<LogicWire>();

    fun tick() {

    }
    fun update() {

    }
    fun render(camera: Camera, rendering: Rendering) {
        rendering.lg.fill(100f, 100f, 200f)
        rendering.lg.setEps(
            camera.camX(PX),
            camera.camY(PY),
            camera.camZ(50f),
            camera.camZ(50f)
        )
    }
}