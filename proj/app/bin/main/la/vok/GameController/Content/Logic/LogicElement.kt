package la.vok.GameController.Content.Logic

import la.vok.GameController.Client.Camera
import la.vok.Storages.Storage
import la.vok.LavokLibrary.LGraphics
import la.vok.GameController.Content.Logic.LogicWire
import la.vok.GameController.Content.Map.LogicMap
import la.vok.GameController.GameController
import la.vok.UI.MainRender
import processing.data.*
import la.vok.GameController.Client.Rendering.*
import la.vok.LavokLibrary.MetaStorage
import la.vok.LavokLibrary.getVec2
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.Vec3
import la.vok.LavokLibrary.putVec
import la.vok.UI.Canvas.*
import la.vok.UI.Scenes.*

class LogicElement(
    var pos: Vec2,
    var type: String,
    var gameController: la.vok.GameController.GameController,
    var logicMap: LogicMap,
    var standartInit: Boolean = true
) : MetaStorage(), IRender {
    override var renderLayersData: RenderLayersData = RenderLayersData(
        RenderLayer(Layers.B2, 5, this::render)
    )
    override var isVisible = true
    override val updateVisualF: (MainRender) -> Unit by lazy { this::updateVisual }

    companion object {
        fun fromJsonObject(json: JSONObject, gameController: GameController, logicMap: LogicMap): LogicElement {
            var id: Long  = json.getLong("id", 0L)
            var pos: Vec2 = json.getVec2("pos", Vec2(0f, 0f))
            var type: String = json.getString("type", "")
            var otherData: JSONObject = json.setJSONObject("otherData", JSONObject())

            var logic = logicMap.addElement(pos, type, false)
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

    var visualpos = Vec3(0f, 0f)
    var VCR = 0f
    var VCG = 0f
    var VCB = 0f

    fun updateVisual(mainRender: MainRender) {
        visualpos.xy = mainRender.camera.cam(pos)
        visualpos.z = mainRender.camera.camZ(50f)

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
            visualpos.xy,
            Vec2(visualpos.z)
        )
    }
    fun renderCanvas(camera: Camera, mainRender: MainRender) {
        if (canvas == null) {
            canvas = LCanvas(
                pos = camera.cam(pos),
                wh = Vec2(500f),
                scale = Vec2(0.5f),
                textScale = 1f,
                gameController = gameController
            )
            canvas!!.addChild("panel")
        }
        canvas!!.pos = camera.cam(pos)
        canvas!!.renderElements()
    }

    fun getOtherData(): JSONObject {
        var json = JSONObject()
        return json
    }

    fun toJsonObject(): JSONObject {
        var json = JSONObject()
        json.put("id", id)
        json.putVec("pos", pos)
        json.put("type", type)
        json.put("otherData", getOtherData())
        return json
    }

    fun loadOtherData(otherData: JSONObject) {

    }
}