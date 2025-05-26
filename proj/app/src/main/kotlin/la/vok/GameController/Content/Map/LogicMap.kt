package la.vok.GameController.Content.Map

import la.vok.GameController.GameController
import la.vok.GameController.Content.Logic.LogicElement
import la.vok.GameController.Content.Logic.LogicWire


class LogicMap(var gameController: GameController) {
    var map: ArrayList<LogicElement> = ArrayList()
    var wires: HashSet<LogicWire> = HashSet()

    fun addWire(wire: LogicWire) {
        wires.add(wire)
    }
    fun removeWire(wire: LogicWire) {
        wires.remove(wire)
    }
    fun updateWireSet() {
        wires.clear()
        for (element in gameController.clientController.logicMap.list()) {
            wires.addAll(element.input)
            wires.addAll(element.output)
        }
    }
    fun addWire(start: LogicElement, end: LogicElement) {
        val wire = LogicWire(start, end, gameController)
        wires.add(wire)
        start.output.add(wire)
        end.input.add(wire)
    }
    fun removeWire(start: LogicElement, end: LogicElement) {
        val wire = wires.find { it.start == start && it.end == end }
        if (wire != null) {
            wires.remove(wire)
            start.output.remove(wire)
            end.input.remove(wire)
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