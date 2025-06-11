package la.vok.GameController

import com.jogamp.opengl.GLAnimatorControl
import java.io.File
import la.vok.UI.Canvas.*
import la.vok.UI.Scenes.*
import kotlin.script.experimental.api.*
import kotlin.script.experimental.jvm.*
import kotlin.script.experimental.host.*
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlinx.coroutines.runBlocking
import la.vok.LavokLibrary.Functions
import la.vok.Storages.Storage


class KtsScriptManager(var gameController: GameController) {
    private val compiledScripts = mutableMapOf<String, CompiledScript>()
    private val scriptingHost = BasicJvmScriptingHost()

    private val compilationConfiguration = ScriptCompilationConfiguration {
        jvm {
            dependenciesFromCurrentContext(wholeClasspath = true)
        }
        defaultImports("la.vok.GameController.GameController") // чтобы не писать полный путь в каждом скрипте
    }

    private fun createEvaluationConfiguration(): ScriptEvaluationConfiguration {
        return ScriptEvaluationConfiguration {
            jvm {
                baseClassLoader(this@KtsScriptManager::class.java.classLoader)
            }
            providedProperties(
                "gameController" to GameController::class
            )
        }
    }

    fun executeScript(key: String): ResultWithDiagnostics<EvaluationResult> = runBlocking {
        val file = File(Storage.main.dataPath(gameController.scriptsLoader.getPath(key)))

        val compiled = compiledScripts.getOrPut(file.absolutePath) {
            val result = scriptingHost.compiler(file.toScriptSource(), compilationConfiguration)
            result.valueOrThrow()
        }

        val evalConfig = createEvaluationConfiguration().with {
            constructorArgs(gameController) // передаём аргумент
        }

        scriptingHost.evaluator(compiled, evalConfig)
    }
}
