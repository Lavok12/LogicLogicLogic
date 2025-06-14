package la.vok.LoadData

import la.vok.LavokLibrary.Functions
import la.vok.Storages.Settings
import la.vok.GameController.GameController;


class CommandsLoader(var gameController: GameController) {
    val commandsFilesLoader = CommandsFilesLoader()

    fun loadData() {
        commandsFilesLoader.loadData()
    }

    /** Получить команду по имени */
    fun getCommand(name: String): CommandData? {
        return commandsFilesLoader.getCommand(name)
    }

    fun hasCommand(name: String): Boolean {
        return commandsFilesLoader.hasCommand(name)
    }

    /** Получить вариацию команды по имени и типам параметров */
    fun getCommandVariation(name: String, paramTypes: List<String>): CommandVariation? {
        return commandsFilesLoader.getCommandVariation(name, paramTypes)
    }

    /** Проверить, существует ли такая вариация */
    fun hasCommandVariation(name: String, paramTypes: List<String>): Boolean {
        return commandsFilesLoader.hasCommandVariation(name, paramTypes)
    }

    fun getCommandVariationIndex(name: String, paramTypes: List<String>): Int {
        return commandsFilesLoader.getCommandVariationIndex(name, paramTypes)
    }
}

class CommandsFilesLoader : JsonDataLoader() {
    val commandsMap = mutableMapOf<String, CommandData>()

    override fun loadDataFromFolder(path: String) {
        val filesList = Functions.scanDirRecursive(path)
        for (file in filesList) {
            try {
                val json = Functions.loadJSONObject(file)
                for (key in json.keys()) {
                    val commandJson = json.getJSONObject(key as String)
                    val description = commandJson.getString("description", "")
                    val variationsArray = commandJson.getJSONArray("command-variations")

                    val variations = mutableListOf<CommandVariation>()
                    for (i in 0 until variationsArray.size()) {
                        val item = variationsArray.getJSONObject(i)
                        val vars = List(item.getJSONArray("vars").size()) { j -> item.getJSONArray("vars").getString(j) }
                        val script = item.getString("script")
                        variations.add(CommandVariation(vars, script))
                    }

                    commandsMap[key] = CommandData(description, variations)
                }
            } catch (e: Exception) {
                println("Error loading command JSON '$file': ${e.message}")
            }
        }
    }

    fun loadData() {
        data.clear()
        loadDataFromFolder(Functions.resourceDir(Settings.commandsPath))
    }

    fun hasCommand(name: String): Boolean {
        return  commandsMap.contains(name)
    }
    fun getCommand(name: String): CommandData? = commandsMap[name]

    fun getCommandVariation(name: String, paramTypes: List<String>): CommandVariation? =
        getCommand(name)?.getVariationByTypes(paramTypes)

    fun hasCommandVariation(name: String, paramTypes: List<String>): Boolean =
        getCommand(name)?.hasVariationByTypes(paramTypes) ?: false

    fun getCommandVariationIndex(name: String, paramTypes: List<String>): Int =
        getCommand(name)?.getVariationIndex(paramTypes) ?: -1
}



class CommandVariation(
    val vars: List<String>,
    val script: String
)

class CommandData(
    val description: String,
    val commandVariations: List<CommandVariation>
) {
    /** Найти вариацию команды по типам аргументов */
    fun getVariationByTypes(paramTypes: List<String>): CommandVariation? {
        return commandVariations.firstOrNull {
            isMatchingParamTypes(it.vars, paramTypes)
        }
    }

    /** Проверить наличие подходящей вариации */
    fun hasVariationByTypes(paramTypes: List<String>): Boolean {
        return getVariationByTypes(paramTypes) != null
    }

    /** Найти индекс подходящей вариации */
    fun getVariationIndex(paramTypes: List<String>): Int {
        return commandVariations.indexOfFirst {
            isMatchingParamTypes(it.vars, paramTypes)
        }
    }

    fun isArgs(args: String): Boolean {
        when(args) {
            "@string...", "@player...", "@float...", "@command..." -> {
                return true
            }
        }
        return false
    }
    fun argsToArg(args: String): String {
        when (args) {
            "@string..." -> return "@string"
            "@player..." -> return "@player"
            "@float..." -> return "@float"
            "@command..." -> return "@command"
        }
        return args
    }
    private fun isMatchingParamTypes(definedVars: List<String>, actualParamTypes: List<String>): Boolean {
        var localDefinedVars = ArrayList<String>()
        localDefinedVars.addAll(definedVars)
        if (definedVars.size != actualParamTypes.size) {
            if (definedVars.size > 0 && isArgs(definedVars.last())) {
                if (definedVars.size < actualParamTypes.size) {
                    var ata = argsToArg(definedVars.last())
                    localDefinedVars.removeLast()
                    for (i in 1..actualParamTypes.size - localDefinedVars.size) {
                        localDefinedVars += ata
                    }
                    println(localDefinedVars)
                } else if (definedVars.size == actualParamTypes.size) {
                    localDefinedVars[localDefinedVars.size-1] = argsToArg(localDefinedVars.last())
                }  else if (definedVars.size == actualParamTypes.size + 1) {
                    localDefinedVars.removeLast()
                } else {
                    return false
                }
            } else {
                return false
            }
        }

        for (i in localDefinedVars.indices) {
            val def = localDefinedVars[i]
            val act = actualParamTypes[i]
            if (def != act && (def != "@string") && (def != "@string...")) {
                return false
            }
        }

        return true
    }
}
