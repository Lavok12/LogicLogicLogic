package la.vok.LoadData

import la.vok.LavokLibrary.*
import la.vok.Storages.Storage
import la.vok.UI.LCanvas
import la.vok.GameController.GameController
import la.vok.UI.Elements.*
import processing.data.JSONObject
import processing.data.JSONArray

class LoadUIList(private val gameController: GameController) {

    fun addChilds(json: JSONObject, lCanvas: LCanvas, tag: String = "") {
        lCanvas.addChild(jsonToElement(JSONUpdate(json), lCanvas), tag)
    }

    fun addChilds(key: String, lCanvas: LCanvas, tag: String = "") {
        addChilds(loadData(key), lCanvas, tag)
    }

    fun JSONUpdate(json: JSONObject): JSONObject {
        if (json.hasKey("debug")) {
            println("UI Element debug: ${json.getString("debug")}")
        }
        val newJson = JSONObject()

        if (json.hasKey("extends")) {
            val extendsArray = json.getJSONArray("extends")
            for (i in 0 until extendsArray.size()) {
                val extendJson = loadData(extendsArray.getString(i))
                extendJson.remove("extends")
                newJson.add(extendJson)
            }
        }

        newJson.add(json)
        resolveVariables(newJson)
        return newJson
    }

    fun loadData(key: String): JSONObject {
        if (!gameController.UILoader.hasKey(key)) {
            println("not $key")
            val ret = JSONObject()
            ret.setString("tag", "null")
            return ret
        }
        val json = gameController.UILoader.getJson(key)
        return JSONUpdate(json)
    }

    private fun resolveVariables(json: JSONObject) {
        for (jKey in json.keys()) {
            val value = json.get(jKey as String)

            when (value) {
                is String -> {
                    val resolved = resolveString(value)
                    json.put(jKey, resolved)
                }

                is JSONArray -> {
                    resolveArray(value)
                    json.put(jKey, value)
                }
            }
        }
    }

    private fun resolveArray(array: JSONArray) {
        for (i in 0 until array.size()) {
            val item = array.get(i)

            val resolvedItem = when (item) {
                is String -> {
                    resolveString(item)
                }
                is JSONArray -> {
                    resolveArray(item)
                    item
                }
                else -> item
            }
            array.setString(i, "$resolvedItem") 
        }
    }

    private fun resolveString(value: String): Any {
        return when {
            value.startsWith("@") -> {
                val str = value.substring(1)
                val callIndex = str.indexOf('(')
                if (callIndex != -1 && str.endsWith(")")) {
                    val varKey = str.substring(0, callIndex)
                    val paramsString = str.substring(callIndex + 1, str.length - 1)
                    val params = if (paramsString.isBlank()) listOf() else paramsString.split(",")
                    LuaEvaluator.evalFile(varKey, gameController, params)
                } else {
                    val varKey = str.substring(0, callIndex)
                    LuaEvaluator.evalFile(varKey, gameController)
                }
            }
            value.startsWith("%") ->  {
                val varKey = value.substring(1)
                val varJson = loadData(varKey)
                if (varJson.hasKey("value")) varJson.get("value") else varJson
            }
            else -> value
        }
    }
    

    fun jsonToElement(json: JSONObject, canvas: LCanvas): LElement {
        val type = if (json.hasKey("type")) json.getString("type") else ""
        return when (type) {
            "LPanel" -> LPanel.JSONToElement(json, canvas, gameController)
            "LText" -> LText.JSONToElement(json, canvas, gameController)
            "LButton" -> LButton.JSONToElement(json, canvas, gameController)
            "LProgressBar" -> LProgressBar.JSONToElement(json, canvas, gameController)
            "LTextField" -> LTextField.JSONToElement(json, canvas, gameController)
            else -> LElement.JSONToElement(json, canvas, gameController)
        }
    }
}
