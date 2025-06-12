package la.vok.GameController.TransferModel

import la.vok.GameController.*
import la.vok.GameController.Client.*
import processing.data.*
import la.vok.GameController.Content.Logic.LogicElement
import la.vok.GameController.Content.Logic.LogicWire

interface ClientTransferHandler {
    fun handle(data: JSONObject, updater: ClientTransferUpdater)
}
interface ServerTransferHandler {
    fun handle(data: JSONObject, sender: String, updater: ServerTransferUpdater)
}
