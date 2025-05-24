package la.vok.LoadData

import la.vok.LavokLibrary.Functions

open class JsonDataLoader {
    open val data = HashMap<String, String>()

    open fun loadDataFromFolder(path: String) {
        val filesList = Functions.scanDirRecursive(path)
        for (file in filesList) {
            println("file $file")
            try {
                val json = Functions.loadJSONObject("$file")
                for (key in json.keys()) {
                    data[key as String] = json.getString(key)
                    println("   key: $key, value: ${json.getString(key)}")
                }
            } catch (e: Exception) {
                println("Error JSON '$file' -- '$path': ${e.message}")
            }
        }
    }
    
    fun getString(key: String): String {
        return data[key] ?: key
    }

    fun setString(key: String, value: String) {
        data[key] = value
    }
}