package la.vok.GameController.TransferModel

import processing.data.JSONObject

class TransferPackage(var header: String, var sender: String, var recipient: String, var data: JSONObject) {
    companion object {
        var ALL = "ALL"
        var SERVER = "SERVER"

        fun fromString(jsonString: String): TransferPackage {
            val json = JSONObject.parse(jsonString)
            return TransferPackage(
                json.getString("header"),
                json.getString("sender"),
                json.getString("recipient"),
                json.getJSONObject("data")
            )
        }
    }

    override fun toString(): String {
        val json = JSONObject()
        json.setString("header", header)
        json.setString("sender", sender)
        json.setString("recipient", recipient)
        json.setJSONObject("data", data)
        return json.toString()
    }

    fun send(transferModel: TransferModel) {
        transferModel.sendData(this)
    }

    fun copy(): TransferPackage {
        val clonedJson = JSONObject.parse(data.toString())
        return TransferPackage(header, sender, recipient, clonedJson)
    }

}