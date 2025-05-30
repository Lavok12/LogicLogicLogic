package la.vok.GameController.Content.Logic

import la.vok.GameController.Client.Camera
import la.vok.Storages.Storage
import la.vok.LavokLibrary.LGraphics
import la.vok.GameController.Content.Logic.LogicWire
import la.vok.GameController.Content.Map.LogicMap
import la.vok.GameController.GameController
import la.vok.UI.Rendering
import la.vok.UI.LCanvas
import processing.data.*

class LogicElement(
    var PX: Float,
    var PY: Float,
    var type: String,
    var gameController: la.vok.GameController.GameController,
    var logicMap: LogicMap,
    var standartInit: Boolean = true
) {
    companion object {
        fun fromJsonObject(json: JSONObject, gameController: GameController, logicMap: LogicMap): LogicElement {
            var id: Long  = json.getLong("id", 0L)
            var PX: Float = json.getFloat("PX", 0f)
            var PY: Float = json.getFloat("PY", 0f)
            var type: String = json.getString("type", "")
            var otherData: JSONObject = json.setJSONObject("otherData", JSONObject())
        
            var logic = logicMap.addElement(PX, PY, type, false)
            logic.id = id
            logic.loadOtherData(otherData)
            return logic
        }
    }

    var id: Long = 0
    init {
        if (standartInit) {
            id = logicMap.elementsCount
            logicMap.elementsCount++
        }
    }
    var input = ArrayList<LogicWire>();
    var output = ArrayList<LogicWire>();
    var canvas: LCanvas? = null

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
    fun renderCanvas(camera: Camera, rendering: Rendering) {
        if (canvas == null) {
            canvas = LCanvas(
                posX = camera.camX(PX),
                posY = camera.camY(PY),
                width = 500f,
                height = 500f,
                scaleX = 0.5f,
                scaleY = 0.5f,
                textScale = 1f,
                gameController = gameController
            )
            canvas!!.addChild("panel")
        }
        canvas!!.posX = camera.camX(PX)
        canvas!!.posY = camera.camY(PY)
        canvas!!.renderElements()
    }

    fun getOtherData(): JSONObject {
        var json = JSONObject()
        return json
    }

    fun toJsonObject(): JSONObject {
        var json = JSONObject()
        json.put("id", id)
        json.put("PX", PX)
        json.put("PY", PY)
        json.put("type", type)
        json.put("otherData", getOtherData())
        return json
    }

    fun loadOtherData(otherData: JSONObject) {

    }
}

