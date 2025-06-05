package la.vok.GameController.Client.Rendering

import la.vok.GameController.*
import la.vok.UI.*

class RenderBuffer(val gameController: GameController, val mainRender: MainRender) {
    // 15 списков под 15 слоёв (0..14), каждый содержит список из пар sublayer и action
    private val layers: Array<MutableList<Pair<Int, (MainRender) -> Unit>>> =
        Array(15) { mutableListOf() }

    private val updateVisual = ArrayList<(MainRender) -> Unit>()

    fun addData(updateVisualF: (MainRender) -> Unit, data: RenderLayersData) {
        addInBufferUpdateVisual(updateVisualF)
        for (i in data.layers) {
            addLayer(i)
        }
    }

    fun addInBufferUpdateVisual(updateVisualF: (MainRender) -> Unit) {
        updateVisual += updateVisualF
    }

    fun addLayer(renderLayer: RenderLayer) {
        val layerIndex = renderLayer.layer
        val sublayer = renderLayer.sublayer
        if (layerIndex in 0..14) {
            layers[layerIndex].add(sublayer to renderLayer.f)
        } else {
            error("RenderBuffer::Invalid layer index: $layerIndex")
        }
    }

    fun updateVisualAll() {
        for (i in updateVisual) {
            i(mainRender)
        }
    }

    fun clearUpdateVisual() {
        updateVisual.clear()
    }

    fun renderA() {
        renderLayersInRange(0..4)
    }

    fun renderB() {
        renderLayersInRange(5..9)
    }

    fun renderC() {
        renderLayersInRange(10..14)
    }

    private fun renderLayersInRange(range: IntRange) {
        for (layerIndex in range) {
            val sublayers = layers[layerIndex]
            // Рисуем в порядке sublayer (чем меньше sublayer — тем раньше)
            for ((_, action) in sublayers) {
                action(mainRender)
            }
        }
    }

    fun clearA() = clearLayersInRange(0..4)
    fun clearB() = clearLayersInRange(5..9)
    fun clearC() = clearLayersInRange(10..14)

    private fun clearLayersInRange(range: IntRange) {
        for (layerIndex in range) {
            layers[layerIndex].clear()
        }
    }
}
