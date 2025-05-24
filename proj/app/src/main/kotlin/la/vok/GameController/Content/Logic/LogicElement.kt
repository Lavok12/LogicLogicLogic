package la.vok.GameController.Content.Logic

import la.vok.GameController.Client.Camera
import la.vok.Storages.Storage
import la.vok.LavokLibrary.LGraphics
import la.vok.gameController.Content.Logic.LogicWire

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
    fun render(camera: Camera, lg: LGraphics = Storage.lg) {
        lg.fill(255f, 0f, 0f)
        lg.setEps(
            camera.camX(PX),
            camera.camY(PY),
            camera.camZ(10f),
            camera.camZ(10f)
        )
    }
}