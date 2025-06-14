package la.vok.UI.Canvas

import la.vok.UI.Elements.*
import la.vok.GameController.*
import la.vok.Storages.*
import processing.event.MouseEvent
import la.vok.InputController.*

class LCanvasController(var gameController: GameController) {
    var canvasList = HashSet<LCanvas>()
    var canvasRenderList = ArrayList<ArrayList<LCanvas>>()
    var updateFlag = false

    init {
        for (i in 0..Settings.canvasRenderLayers) {
            canvasRenderList += ArrayList<LCanvas>()
        }
    }

    fun UITick() {
        if (updateFlag) { 
            canvasListUpdate()
            updateFlag = false
        }
        for (j in canvasRenderList) {
            for (i in j) {
                i.tick()
            }
        }
    }

    fun add(lCanvas: LCanvas) {
        if (lCanvas.layer == -1) {
            return
        }
        canvasList.add(lCanvas)
        lCanvas.isActive = true
        updateFlag = true
    }

    fun remove(lCanvas: LCanvas) {
        if (canvasList.contains(lCanvas)) {
            canvasList.remove(lCanvas)
        }
        lCanvas.isActive = false
        updateFlag = true
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
        if (updateFlag) { 
            canvasListUpdate()
            updateFlag = false
        }
        for (j in canvasRenderList) {
            for (i in j) {
                i.renderElements()
            }
        }
        if (Settings.UIDebugRender) {
            for (j in canvasRenderList) {
                for (i in j) {
                    i.debugRender(Storage.gameController.mainRender)
                }
            }
        }
    }
    
    fun findUIElementAt(mouseX: Float, mouseY: Float): LElement? {
        for (j in canvasRenderList.asReversed()) {
            for (i in j.asReversed()) {
                if (i.hasHitbox && i.inside(mouseX, mouseY)) {
                    for (layer in i.getAllElements().asReversed()) {
                        for (element in layer.asReversed()) {
                            if (element.inside(mouseX, mouseY)) {
                                return element
                            }
                        }
                    }
                }
            }
        }
        return null
    } 
    
    fun mouseClicked(mouseController: MouseController) {
        findUIElementAt(mouseController.moux, mouseController.mouy)?.handleMouseDown(mouseController)
    }
}