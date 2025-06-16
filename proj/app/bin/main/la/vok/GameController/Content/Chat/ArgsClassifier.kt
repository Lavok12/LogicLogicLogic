package la.vok.GameController.Content.Chat

import la.vok.GameController.Server.ServerController

class ArgsClassifier(private val serverController: ServerController) {

    fun classifyArgs(args: List<String>): List<String> {
        return args.map { arg ->
            arg.toFloatOrNull()?.let { return@map "@float" }

            if (serverController.gameController.loaders.commands.hasCommand(arg)) {
                return@map "@command"
            }
            if (serverController.connectsContainer.hasName(arg)) {
                return@map "@player"
            }
            "@string"
        }
    }
}
