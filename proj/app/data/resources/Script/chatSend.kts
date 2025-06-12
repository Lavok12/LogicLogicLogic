val tf = gameController.getCanvas().findElementByTag("chat") as? la.vok.UI.Elements.LTextField
if (tf != null) {
    val sendData = processing.data.JSONObject()
    sendData.put("text", tf.inputString)
    tf.clearInput()
    gameController.clientController.clientFunctions.sendToServer("chat_message", sendData)
}
