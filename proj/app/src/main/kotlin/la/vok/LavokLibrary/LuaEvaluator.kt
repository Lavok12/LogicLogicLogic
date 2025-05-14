package la.vok.LavokLibrary

import org.luaj.vm2.*
import org.luaj.vm2.lib.jse.JsePlatform
import org.luaj.vm2.lib.OneArgFunction

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
}
