package la.vok.GameController.Client

import la.vok.Render.MainRender

class ClientController {
    lateinit var mainRender: MainRender
    init {
        
    }

    fun initRender() {
        mainRender = MainRender()
    }
    fun mainRender() {
        mainRender.render()
    }
}