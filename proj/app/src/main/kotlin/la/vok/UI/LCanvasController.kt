package la.vok.UI

import la.vok.UI.Elements.*
import la.vok.GameController.*
import la.vok.Storages.*


class LCanvasController(var gameController: GameController) {
    var canvasList = HashSet<LCanvas>()

    var canvasRenderList = ArrayList<ArrayList<LCanvas>>()
    init {
        for (i in 0..Settings.canvasRenderLayers) {
            canvasRenderList += ArrayList<LCanvas>()
        }
    }

    fun add(lCanvas: LCanvas) {
        if (lCanvas.layer == -1) {
            return
        }
        canvasList.add(lCanvas)
        lCanvas.isActive = true
        canvasListUpdate()
    }

    fun remove(lCanvas: LCanvas) {
        if (canvasList.contains(lCanvas)) {
            canvasList.remove(lCanvas)
        }
        lCanvas.isActive = false
        canvasListUpdate()
    }

    fun canvasAddInRender(layer: Int, canvas: LCanvas) {
        canvasRenderList[layer] += canvas
    }

    fun canvasListUpdate() {
        for (j in canvasRenderList) {
            j.clear()
        }
        for (i in canvasList) {
            canvasAddInRender(i.layer, i)
        }
    }

    fun canvasListRender() {
        for (j in canvasRenderList) {
            for (i in j) {
                i.renderElements()
            }
        }
    }
}