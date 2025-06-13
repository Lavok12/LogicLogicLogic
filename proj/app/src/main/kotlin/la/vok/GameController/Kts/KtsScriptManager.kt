package la.vok.GameController.Kts

import kotlinx.coroutines.runBlocking
import la.vok.GameController.GameController
import la.vok.Storages.Storage
import java.io.File
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.baseClassLoader
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.JvmScriptCompiler

class KtsScriptManager(private val gameController: GameController) {
    private val compiledScripts = mutableMapOf<String, CompiledScript>()
    private val scriptingHost = BasicJvmScriptingHost()

    private val evaluationConfigs: MutableMap<String, ScriptEvaluationConfiguration> = mutableMapOf()

    private val compilationConfiguration = ScriptCompilationConfiguration {
        jvm {
            dependenciesFromCurrentContext(wholeClasspath = true)
        }
        baseClass(KtsBaseScript::class)
        defaultImports(
            "la.vok.UI.*",
            "la.vok.UI.Canvas.*",
            "la.vok.UI.Scenes.*",
            "la.vok.Storages.*",
            "la.vok.LoadData.*",
            "la.vok.GameController.GameController",
            "la.vok.GameController.Content.*",
            "la.vok.GameController.Content.Chat.*",
            "la.vok.GameController.Server.*",
            "org.luaj.vm2.*",
            "org.luaj.vm2.lib.jse.JsePlatform",
            "processing.core.PApplet",
            "processing.data.JSONObject",
            "processing.data.JSONArray",
            "la.vok.LavokLibrary.*",
            "la.vok.InputController.MouseController",
            "la.vok.UI.Elements.*",
        )
    }

    val commandObject = CommandObject(gameController)
    val defaultObject = DefaultObject(gameController)

    init {
        registerEvaluationConfig("default", ScriptEvaluationConfiguration {
            jvm {
                baseClassLoader(this@KtsScriptManager::class.java.classLoader)
            }
            constructorArgs(gameController, commandObject, defaultObject)
        })

        registerEvaluationConfig("command", ScriptEvaluationConfiguration {
            jvm {
                baseClassLoader(this@KtsScriptManager::class.java.classLoader)
            }
            constructorArgs(gameController, commandObject, defaultObject)
        })
    }

    fun registerEvaluationConfig(name: String, config: ScriptEvaluationConfiguration) {
        evaluationConfigs[name] = config
    }

    fun compileScriptOnly(key: String): CompiledScript = runBlocking {
        val file = File(Storage.main.dataPath(gameController.scriptsLoader.getKtsPath(key)))

        compiledScripts.getOrPut(file.absolutePath) {
            println("Compiling script: ${file.absolutePath}")
            val result = scriptingHost.compiler(file.toScriptSource(), compilationConfiguration)

            if (result is ResultWithDiagnostics.Failure) {
                println("Compilation failed for script: ${file.absolutePath}")
                result.reports.forEach { println(it.message) }
            } else {
                println("Compilation successful: ${file.absolutePath}")
            }

            result.valueOrThrow()
        }
    }

    fun executeScript(key: String, configName: String): ResultWithDiagnostics<EvaluationResult> = runBlocking {
        val compiled = compileScriptOnly(key)

        val baseConfig = if (evaluationConfigs.containsKey(configName)) {
            evaluationConfigs[configName]!!
        } else {
            evaluationConfigs["default"]!!
        }

        scriptingHost.evaluator(compiled, baseConfig)
    }

    private fun createDefaultEvaluationConfiguration(): ScriptEvaluationConfiguration {
        return ScriptEvaluationConfiguration {
            jvm {
                baseClassLoader(this@KtsScriptManager::class.java.classLoader)
            }
            constructorArgs(gameController)
        }
    }
}


