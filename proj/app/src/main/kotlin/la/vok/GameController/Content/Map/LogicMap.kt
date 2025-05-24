package la.vok.GameController.Content.Map

import la.vok.GameController.GameController
import la.vok.GameController.Content.Logic.LogicElement
import la.vok.gameController.Content.Logic.LogicWire


class LogicMap(var gameController: GameController) {
    var map: ArrayList<LogicElement> = ArrayList()

    var wires: HashSet<LogicWire> = HashSet()

    fun updateWireSet() {
        wires.clear()
        for (element in gameController.clientController.logicMap.list()) {
            wires.addAll(element.input)
            wires.addAll(element.output)
        }
    }
    
    fun addElement(element: LogicElement) {
        map.add(element)
    }

    fun removeElement(element: LogicElement) {
        map.remove(element)
    }

    fun list(): ArrayList<LogicElement> {
        return map
    }
}