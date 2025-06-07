package la.vok.GameController.Content.Logic

import la.vok.GameController.Client.Camera
import la.vok.Storages.Storage
import la.vok.LavokLibrary.LGraphics
import la.vok.GameController.Content.Logic.LogicWire
import la.vok.GameController.Content.Map.LogicMap
import la.vok.GameController.GameController
import la.vok.UI.MainRender
import la.vok.UI.LCanvas
import processing.data.*
import la.vok.GameController.Client.Rendering.*

class LogicElement(
    var PX: Float,
    var PY: Float,
    var type: String,
    var gameController: la.vok.GameController.GameController,
    var logicMap: LogicMap,
    var standartInit: Boolean = true
) : IRender {
    override var renderLayersData: RenderLayersData = RenderLayersData(
        RenderLayer(Layers.B2, 5, this::render)
    )
    override var isVisible = true
    override val updateVisualF: (MainRender) -> Unit by lazy { this::updateVisual }

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
    
    var VX = 0f
    var VY = 0f
    var VZ = 0f
    var VCR = 0f
    var VCG = 0f
    var VCB = 0f

    fun updateVisual(mainRender: MainRender) {
        VX = mainRender.camera.camX(PX)
        VY = mainRender.camera.camY(PY)
        VZ = mainRender.camera.camZ(50f)
        
        when (type) {
            "test" -> {
                isVisible = true
                VCR = 60f
                VCG = 90f
                VCB = 200f
            }
            "new" -> {
                isVisible = true
                VCR = 200f
                VCG = 90f
                VCB = 60f
            } 
            else -> {
                isVisible = false
            }
        }
    }
    
    fun render(mainRender: MainRender) {
        mainRender.lg.fill(VCR, VCG, VCB)
        mainRender.lg.setEps(
            VX,
            VY,
            VZ,
            VZ,
        )
    }
    fun renderCanvas(camera: Camera, mainRender: MainRender) {
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