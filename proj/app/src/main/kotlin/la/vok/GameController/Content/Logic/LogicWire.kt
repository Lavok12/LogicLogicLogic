package la.vok.GameController.Content.Logic

import la.vok.GameController.Content.Logic.LogicElement
import la.vok.GameController.Content.Map.LogicMap
import la.vok.GameController.Client.Camera
import la.vok.GameController.GameController
import la.vok.UI.MainRender
import processing.data.*
import la.vok.GameController.Client.Rendering.*
import la.vok.LavokLibrary.Vectors.Vec2

class LogicWire(
    var start: LogicElement,
    var end: LogicElement,
    var gameController: la.vok.GameController.GameController,
    var logicMap: LogicMap,
    var standartInit: Boolean = true
) : IRender {
    override var renderLayersData: RenderLayersData = RenderLayersData(
        RenderLayer(Layers.B1, 5, this::render)
    )
    override var isVisible = true
    override val updateVisualF: (MainRender) -> Unit by lazy { this::updateVisual }

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
    var isActive: Boolean = false
    

    init {
        if (standartInit) {
            id = logicMap.wiresCount
            logicMap.wiresCount++
        }
    }
    
    fun activate() {
        isActive = true
    }
    fun deactivate() {
        isActive = false
    }
    var visualpos1 = Vec2(0f)
    var visualpos2 = Vec2(0f)
    var VZ = 0f

    fun tick() {

    }
    fun update() {

    }
    fun updateVisual(mainRender: MainRender) {
        visualpos1 = mainRender.camera.cam(start.pos)
        visualpos2 = mainRender.camera.cam(end.pos)
        VZ  = mainRender.camera.camZ(5f)
    
    }
    fun render(mainRender: MainRender) {
        mainRender.lg.fill(255f,0f,0f)
        mainRender.lg.setLine(
            visualpos1,
            visualpos2,
            VZ,
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
