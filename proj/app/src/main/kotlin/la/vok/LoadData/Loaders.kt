package la.vok.LoadData

import la.vok.GameController.GameController
import la.vok.Storages.Settings

class Loaders(val game: GameController) {
    val ui = UILoader(game)
    val sprites = SpriteLoader(game)
    val scripts = ScriptsLoader(game)
    val scenes = ScenesLoader(game)
    val language = LanguageController(Settings.language, game)
    val commands = CommandsLoader(game)

    fun loadAll() {
        language.loadData()
        sprites.loadData()
        ui.loadData()
        scripts.loadData()
        scenes.loadData()
        commands.loadData()
    }
}
