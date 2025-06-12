package la.vok.GameController.Client.Rendering

import la.vok.UI.*

object Layers {
    val A1 = 0   
    val A2 = 1   
    val A3 = 2   
    val A4 = 3   
    val A5 = 4   
    val B1 = 5
    val B2 = 6
    val B3 = 7
    val B4 = 8
    val B5 = 9
    val C1 = 10
    val C2 = 11
    val C3 = 12
    val C4 = 13
    val C5 = 14

    val MAX_LAYER = 15
    val MAX_SUBLAYER = 4
    val TOTAL_LAYERS = MAX_LAYER * MAX_SUBLAYER
}

data class RenderLayer(val layer: Int, val sublayer: Int, val f: (MainRender) -> Unit)

class RenderLayersData(vararg vlayers: RenderLayer) {
    var layers = vlayers
}

