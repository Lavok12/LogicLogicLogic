package la.vok.GameController.Client.Rendering

interface IRender {
    var renderLayersData: RenderLayersData
    var isVisible: Boolean

    open fun renderUpdate(renderBuffer: RenderBuffer) {
        if (isVisible) {
            renderBuffer.addData(renderLayersData)
        }
    }
}