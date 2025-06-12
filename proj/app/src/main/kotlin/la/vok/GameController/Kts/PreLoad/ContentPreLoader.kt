package la.vok.GameController.Kts.PreLoad

import la.vok.GameController.GameController
import la.vok.LavokLibrary.Functions
import la.vok.UI.Elements.LProgressBar
import la.vok.UI.Elements.LText

class ContentPreLoader(var gameController: GameController) {
    lateinit var kts_preload_list: Array<String>
    var id = 0
    var max = -1

    var flagEnded = false
    fun loadFiles() {
        val fileText = Functions.loadFile("kts_preload_list")
        kts_preload_list = fileText.lines().filter { it.isNotBlank() }.toTypedArray()
        max = kts_preload_list.size - 1
    }

    var tickCounter = -1 // добавь это поле в класс

    fun tick() {
        if (!flagEnded) {
            tickCounter++
            if (tickCounter > 1) {
                if (id <= max && max != -1) {
                    gameController.ktsScriptManager.compileScriptOnly(kts_preload_list[id])
                    id++
                }

                if (id > max) {
                    flagEnded = true
                    gameController.mainRender.setScene(gameController.scenesContainer.getScene("main")!!)
                }
            }
            val el: LText? = (gameController.getCanvas().getElementByTag("textLoad") as LText?)
            if (el != null) {
                el.text =
                    "Компиляция скриптов ${id + 1} / ${max + 1}" +
                            "\n" +
                            "${kts_preload_list[id]} - ${gameController.scriptsLoader.getKtsPath(kts_preload_list[id])}"
            }
            val el2: LProgressBar? = (gameController.getCanvas().getElementByTag("progressLoad") as LProgressBar?)
            if (el2 != null) {
                el2.progress = ((id + 1) / (max + 1).toFloat())

            }
        }
    }
}