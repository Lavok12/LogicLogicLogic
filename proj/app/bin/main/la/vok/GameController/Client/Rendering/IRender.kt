package la.vok.GameController.Client.Rendering

import la.vok.UI.*

interface IRender {
    var renderLayersData: RenderLayersData
    var isVisible: Boolean
    val updateVisualF: (MainRender) -> Unit

    fun renderUpdate(renderBuffer: RenderBuffer) {
        if (isVisible) {
            renderBuffer.addData(updateVisualF, renderLayersData)
        }
    }
}