package la.vok.LoadData

import la.vok.LavokLibrary.Functions
import la.vok.LoadData.LanguageData
import processing.data.JSONObject


class LanguageController(var lang: String = "eng") {
    var langData = HashMap<String, LanguageData>(); 

    init {
        addLang(lang);
    }

    fun changeLang(lang: String) {
        if (!langData.containsKey(lang)) {
            addLang(lang);
        }
        this.lang = lang;
    }

    fun getText(key: String): String {
        if (!langData.containsKey(lang)) {
            return key;
        }
        return langData[lang]?.getString(key) ?: key
    }


    fun addLang(lang: String) {
        if (langData.containsKey(lang)) {
            return;
        }
        langData[lang] = LanguageData(lang);
        langData[lang]?.loadData();
    }   

    fun removeLang(lang: String) {
        langData.remove(lang);
    }
}


class LanguageData(var lang: String) : JsonDataLoader() {
    override fun loadDataFromFolder(patch: String) {
        data.clear()
        super.loadDataFromFolder(patch)
    }

    fun loadData() {
        val patch = Functions.resourceDir("lang/$lang")
        loadDataFromFolder(patch)
    }
}
