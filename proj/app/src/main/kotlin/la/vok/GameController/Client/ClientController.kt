package la.vok.GameController.Client

import la.vok.Render.MainRender

class ClientController {
    var mainRender: MainRender
    init {
        mainRender = MainRender()
    }
    fun mainRender() {
        mainRender.render()
    }
}