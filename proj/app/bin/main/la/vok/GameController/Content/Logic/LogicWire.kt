package la.vok.GameController.Content.Logic

import la.vok.GameController.Content.Logic.LogicElement
import la.vok.GameController.Content.Map.LogicMap
import la.vok.GameController.Client.Camera
import la.vok.GameController.GameController
import la.vok.UI.Rendering
import processing.data.*

class LogicWire(
    var start: LogicElement,
    var end: LogicElement,
    var gameController: la.vok.GameController.GameController,
    var logicMap: LogicMap,
    var standartInit: Boolean = true
) {
    companion object {
        fun fromJsonObject(json: JSONObject, gameController: GameController, logicMap: LogicMap): LogicWire {
            var id: Long  = json.getLong("id", 0L)
            var start: Long = json.getLong("start", 0L)
            var end: Long = json.getLong("end", 0L)
            var otherData: JSONObject = json.setJSONObject("otherData", JSONObject())
        
            var startElement = logicMap.getElementFromId(start)
            var endElment = logicMap.getElementFromId(end)

            var logic = logicMap.addWire(startElement, endElment, false)
            logic.id = id
            logic.loadOtherData(otherData)
            return logic
        }
    }

    var id: Long = 0

    init {
        if (standartInit) {
            id = logicMap.wiresCount
            logicMap.wiresCount++
        }
    }

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

    fun getOtherData(): JSONObject {
        var json = JSONObject()
        return json
    }

    fun toJsonObject(): JSONObject {
        var json = JSONObject()
        json.put("id", id)
        json.put("start", start.id)
        json.put("end", end.id)
        json.put("otherData", getOtherData())
        return json
    }

    fun loadOtherData(otherData: JSONObject) {

    }
}
