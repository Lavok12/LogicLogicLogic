package la.vok.GameController.Kts.PreLoad

import la.vok.GameController.GameController
import la.vok.LavokLibrary.Functions
import la.vok.LavokLibrary.copy
import la.vok.Storages.Settings
import la.vok.UI.Elements.LProgressBar
import la.vok.UI.Elements.LText

class ContentPreLoader(private val gameController: GameController) {

    private lateinit var preloadList: Array<String>
    private var currentId = 0
    private var maxId = -1
    private var tickCounter = -1

    var isFinished = false
        private set

    fun loadFiles() {
        val fileText = Functions.loadFile("kts_preload_list")
        preloadList = fileText.lines().filter { it.isNotBlank() }.toTypedArray()
        maxId = preloadList.size - 1
    }

    fun tick() {
        if (isFinished) return
        tickCounter++

        val textElement = gameController.getCanvas().getElementByTag("textLoad") as? LText
        val progressBar = gameController.getCanvas().getElementByTag("progressLoad") as? LProgressBar

        if (tickCounter > 1) {
            if (currentId <= maxId && maxId != -1) {
                try {
                    gameController.ktsScriptManager.compileScriptOnly(preloadList[currentId])
                    currentId++
                } catch (e: Exception) {
                    // Ошибка компиляции — показать сообщение и завершить
                    isFinished = true
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
            }

            // Все скрипты скомпилированы
            if (currentId > maxId) {
                isFinished = true
                gameController.mainRender.setScene(gameController.scenesContainer.getScene("main")!!)
            }
        }

        // Обновление UI (если ещё не завершено)
        if (!isFinished && currentId <= maxId) {
            textElement?.text =
                "Компиляция скриптов ${currentId + 1} / ${maxId + 1}\n" +
                        "${preloadList[currentId]} - ${gameController.scriptsLoader.getKtsPath(preloadList[currentId])}"

            progressBar?.progress = (currentId + 1) / (maxId + 1).toFloat()
        }
    }
}
