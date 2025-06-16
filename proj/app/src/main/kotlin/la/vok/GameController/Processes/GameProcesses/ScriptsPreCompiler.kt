package la.vok.GameController.Processes.GameProcesses

import la.vok.GameController.GameController
import la.vok.GameController.Processes.Process
import la.vok.LavokLibrary.Functions
import la.vok.LavokLibrary.copy
import la.vok.Storages.Settings
import la.vok.UI.Elements.LProgressBar
import la.vok.UI.Elements.LText

class ScriptsPreCompiler(
    gameController: GameController,
    private val scriptsList: String,
    private val onEndCallback: () -> Unit
) : Process(gameController) {

    private lateinit var preloadList: Array<String>
    private var currentId = 0
    private var maxId = -1

    private var hadError = false

    override fun start() {
        super.start()
        gameController.mainRender.setScene(gameController.scenesContainer.getScene("loading")!!)
        ticks = 0

        val fileText = Functions.loadFile(scriptsList)
        preloadList = fileText.lines().filter { it.isNotBlank() }.toTypedArray()
        maxId = preloadList.size - 1
        currentId = 0
        hadError = false
    }

    override fun tick() {
        super.tick()
        ticks++

        if (hadError || currentId > maxId) return

        val textElement = gameController.getCanvas().getElementByTag("textLoad") as? LText
        val progressBar = gameController.getCanvas().getElementByTag("progressLoad") as? LProgressBar

        if (ticks > 1) {
            try {
                gameController.ktsScriptManager.compileScriptOnly(preloadList[currentId])
                currentId++
            } catch (e: Exception) {
                hadError = true
                textElement?.apply {
                    text = buildString {
                        append("Ошибка компиляции скрипта:\n")
                        append(preloadList[currentId])
                        append("\n\n")
                        append(e::class.simpleName)
                        append(": ")
                        append(e.message)
                    }
                    fontSize = 20f
                    textColor = Settings.errorMessage.copy()
                }
                progressBar?.isActive = false
                return
            }

            if (currentId > maxId) {
                end()
                return
            }
        }

        // Обновление UI
        if (currentId <= maxId) {
            textElement?.text =
                "Компиляция скриптов ${currentId + 1} / ${maxId + 1}\n" +
                        "${preloadList[currentId]} - ${gameController.loaders.scripts.getKtsPath(preloadList[currentId])}"

            progressBar?.progress = (currentId + 1) / (maxId + 1).toFloat()
        }
    }

    override fun end() {
        super.end()
        onEndCallback()
    }

    override fun forcedEnd() {
        super.forcedEnd()
    }
}