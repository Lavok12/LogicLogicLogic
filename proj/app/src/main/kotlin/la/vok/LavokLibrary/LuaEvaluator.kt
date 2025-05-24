package la.vok.LavokLibrary

import org.luaj.vm2.*
import org.luaj.vm2.lib.jse.JsePlatform
import org.luaj.vm2.lib.OneArgFunction
import la.vok.GameController.GameController
import la.vok.Storages.Storage

object LuaEvaluator {
    private val globals = JsePlatform.standardGlobals()

    init {
        globals.set("round", object : OneArgFunction() {
            override fun call(arg: LuaValue): LuaValue {
                val n = arg.checkdouble()
                return LuaValue.valueOf(Math.floor(n + 0.5))
            }
        })
    }

    fun evalFile(key: String, gameController: GameController, args: List<String>): LuaValue {
        val scriptPath = gameController.scriptsLoader.getPath(key)
        return try {
            val chunk = globals.loadfile(Storage.main.dataPath(scriptPath))
            // Конвертируем List<String> в массив LuaValue
            val luaParams = args.map { LuaValue.valueOf(it.trim()) }.toTypedArray()
            // Передаём параметры в функцию через varargs
            chunk.invoke(LuaValue.varargsOf(luaParams)).arg1()
        } catch (e: Exception) {
            println("Lua file eval error: ${e.message}")
            LuaValue.NIL
        }
    }
    fun evalFile(key: String, gameController: GameController): LuaValue {
        val scriptPath = gameController.scriptsLoader.getPath(key)
        return try {
            val chunk = globals.loadfile(Storage.main.dataPath(scriptPath))
            chunk.call()
        } catch (e: Exception) {
            println("Lua file eval error: ${e.message}")
            LuaValue.NIL
        }
    }
    
    
    

    fun eval(code: String): LuaValue {
        return try {
            val chunk = globals.load("return $code")
            chunk.call()
        } catch (e: Exception) {
            println("Lua eval error: ${e.message}")
            LuaValue.NIL
        }
    }

    fun evalWithVars(code: String, vars: Map<String, LuaValue>): LuaValue {
        vars.forEach { (k, v) -> globals.set(k, v) }
        return eval(code)
    }


    fun replacePlaceholders(text: String, i: Int, varName: String, eval: (String) -> LuaValue = LuaEvaluator::eval): String {
        var result1 = text.replace(varName, i.toString())
        var result = result1.replace("\\", "")
    
        val regex = "<(.*?)>".toRegex()
        result = regex.replace(result) { matchResult ->
            val code = matchResult.groupValues[1]
            eval(code).toString()
        }
        return result
    }
}
