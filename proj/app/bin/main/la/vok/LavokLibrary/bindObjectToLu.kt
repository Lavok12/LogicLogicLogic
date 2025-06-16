package la.vok.LavokLibrary

import org.luaj.vm2.*
import org.luaj.vm2.lib.*
import kotlin.reflect.*
import kotlin.reflect.full.*
import kotlin.reflect.jvm.*

object LuaBinder {
    fun bindObject(obj: Any): LuaTable {
        val table = LuaTable()
        val kClass = obj::class

        // Кэш: свойства по имени
        val props = kClass.memberProperties.associateBy { it.name }

        // Добавление полей
        for ((name, prop) in props) {
            prop.isAccessible = true
            table.set(name, wrap(prop.getter.call(obj)))

            // Добавление setter
            if (prop is KMutableProperty1) {
                table.set("set_$name", object : OneArgFunction() {
                    override fun call(value: LuaValue): LuaValue {
                        val unpacked = unpack(value, prop.returnType.classifier as? KClass<*>)
                        prop.setter.call(obj, unpacked)
                        table.set(name, value) // обновить значение
                        return LuaValue.NIL
                    }
                })
            }
        }

        // Добавление методов
        val methods = kClass.memberFunctions
            .filter { it.visibility == KVisibility.PUBLIC }
            .associateBy { it.name }

        for ((name, method) in methods) {
            if (name == "equals" || name == "hashCode" || name == "toString") continue

            table.set(name, object : VarArgFunction() {
                override fun invoke(args: Varargs): Varargs {
                    val params = method.parameters
                    val argList = mutableListOf<Any?>()
                    argList.add(obj) // receiver

                    for (i in 1 until params.size) {
                        val paramType = params[i].type.classifier as? KClass<*>
                        argList.add(unpack(args.arg(i), paramType))
                    }

                    val result = method.call(*argList.toTypedArray())
                    return wrap(result)
                }
            })
        }

        // Добавление вложенного companion object (как "static")
        kClass.companionObjectInstance?.let { companion ->
            table.set("static", bindObject(companion))
        }

        return table
    }

    private fun wrap(value: Any?): LuaValue {
        return when (value) {
            null -> LuaValue.NIL
            is String -> LuaValue.valueOf(value)
            is Number -> LuaValue.valueOf(value.toDouble())
            is Boolean -> LuaValue.valueOf(value)
            is List<*> -> {
                val table = LuaTable()
                value.forEachIndexed { i, v -> table.set(i + 1, wrap(v)) }
                table
            }
            is Map<*, *> -> {
                val table = LuaTable()
                for ((k, v) in value) {
                    table.set(wrap(k), wrap(v))
                }
                table
            }
            else -> bindObject(value) // рекурсивная обёртка
        }
    }

    private fun unpack(value: LuaValue, type: KClass<*>?): Any? {
        return when (type) {
            Int::class -> value.toint()
            Float::class -> value.tofloat() 
            Double::class -> value.todouble()
            Boolean::class -> value.toboolean()
            String::class -> value.tojstring()
            else -> value // передаём как есть
        }
    }
}

/*
val globals = JsePlatform.standardGlobals()
val game = GameController()
globals.set("game", bindObjectToLua(game))

*/
/*
print("Player name:", game.player.name)
print("Player HP before:", game.player.hp)
game.player:damage(30)
print("Player HP after:", game.player.hp)

game:restart()
print("Player HP after restart:", game.player.hp)

*/