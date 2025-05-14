package la.vok.LoadData

import la.vok.LavokLibrary.Functions
import la.vok.Storages.Storage
import la.vok.Render.LCanvas
import la.vok.GameController.GameController
import la.volk.Render.Elements.*
import processing.data.JSONObject
import processing.data.JSONArray

fun JSONObject.add(obj: JSONObject) {
    for (key in obj.keys()) {
        val value = obj.get(key as String)
        when (value) {
            is String -> this.setString(key, value)
            is Int -> this.setInt(key, value)
            is Float -> this.setFloat(key, value)
            is Double -> this.setDouble(key, value)
            is Boolean -> this.setBoolean(key, value)
            is JSONArray -> this.setJSONArray(key, value)
            else -> this.setJSONObject(key, value as JSONObject)
        }
    }
}

class LoadUIList(private val gameController: GameController) {
    fun addChilds(json: JSONObject, lCanvas: LCanvas, tag: String = "") {
        lCanvas.addChild(jsonToElement(JSONUpdate(json), lCanvas), tag)
    }

    fun addChilds(key: String, lCanvas: LCanvas, tag: String = "") {
        addChilds(loadData(key), lCanvas, tag)
    }
    fun JSONUpdate(json: JSONObject): JSONObject {
        if (json.hasKey("debug")) {
            println(json.getString("debug"))
        }
        val newJson = JSONObject();
        
        if (json.hasKey("extends")) {
            val extendsArray = json.getJSONArray("extends")
            for (i in 0 until extendsArray.size()) {
                val extendJson = loadData(extendsArray.getString(i))
                println("${extendsArray.getString(i)}");
                extendJson.remove("extends")
                newJson.add(extendJson)
            }
        }
        newJson.add(json);
        resolveVariables(newJson)
        return newJson
    }
    fun loadData(key: String): JSONObject {
        if (!gameController.UILoader.hasKey(key)) {
            println("not $key");
            val ret = JSONObject();
            ret.setString("tag", "null");
            return JSONObject();
        }
        val path = gameController.UILoader.getPatch(key)
        val json = Functions.loadJSONObject(path)
        
        return JSONUpdate(json);
    }

    private fun resolveVariables(json: JSONObject) {
        for (jKey in json.keys()) {
            val value = json.get(jKey as String)
            if (value is String && value.startsWith("%")) {
                val varKey = value.substring(1)
                val varJson = loadData(varKey)
                if (varJson.hasKey("value")) {
                    val resolvedValue = varJson.get("value")
                    when (resolvedValue) {
                        is String -> json.setString(jKey, resolvedValue)
                        is Int -> json.setInt(jKey, resolvedValue)
                        is Float -> json.setFloat(jKey, resolvedValue)
                        is Double -> json.setDouble(jKey, resolvedValue)
                        is Boolean -> json.setBoolean(jKey, resolvedValue)
                        is JSONArray -> json.setJSONArray(jKey, resolvedValue)
                        else -> json.setJSONObject(jKey, resolvedValue as JSONObject)
                    }
                } else {
                    json.setJSONObject(jKey, varJson)
                }
            }
        }
    }

    fun jsonToElement(json: JSONObject, canvas: LCanvas): LElement {
        val type = if (json.hasKey("type")) json.getString("type") else ""
        return when (type) {
            "LPanel" -> LPanel.JSONToElement(json, canvas, gameController)
            "LText" -> LText.JSONToElement(json, canvas, gameController)
            "LButton" -> LButton.JSONToElement(json, canvas, gameController)
            "LProgressBar" -> LProgressBar.JSONToElement(json, canvas, gameController)
            else -> LElement.JSONToElement(json, canvas, gameController)
        }
    }
}
