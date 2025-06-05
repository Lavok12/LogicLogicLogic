package la.vok.GameController.Content.Map

import la.vok.GameController.GameController
import la.vok.GameController.Content.Logic.LogicElement
import la.vok.GameController.Content.Logic.LogicWire
import la.vok.GameController.Client.Rendering.*

class LogicMap(var gameController: GameController) {
    var elementsCount = 0L;
    var wiresCount = 0L;

    var map: ArrayList<LogicElement> = ArrayList()
    var wires: HashSet<LogicWire> = HashSet()

    fun addWire(wire: LogicWire) {
        wire.start.output.add(wire)
        wire.end.input.add(wire)
        wires.add(wire)
    }
    fun removeWire(wire: LogicWire) {
        wire.start.output.remove(wire)
        wire.end.input.remove(wire)
        wires.remove(wire)
    }

    fun clear() {
        map.clear()
        wires.clear() 
    }
    fun updateWireSet() {
        wires.clear()
        for (element in gameController.clientController.logicMap.list()) {
            wires.addAll(element.input)
            wires.addAll(element.output)
        }
    }
    fun addElement(PX: Float, PY: Float, type: String, standartInit: Boolean = true): LogicElement {
        var logic = LogicElement(PX, PY, type, gameController, this, standartInit)
        map.add(logic)
        return logic
    }
    fun addWire(start: LogicElement, end: LogicElement, standartInit: Boolean = true): LogicWire {
        val wire = LogicWire(start, end, gameController, this, standartInit)
        wires.add(wire)
        start.output.add(wire)
        end.input.add(wire)
        return wire
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

    fun checkElementFromId(id: Long): Boolean {
        return (getElementFromId(id).id == id)
    }

    fun checkWireFromId(id: Long): Boolean {
        return (getElementFromId(id).id == id)
    }

    fun getElementFromId(id: Long): LogicElement {
        for (i in map) {
            if (id == i.id) {
                return i
            }
        }
        return map[0]
    }
    
    fun getWireFromId(id: Long): LogicWire {
        for (i in wires.toList()) {
            if (id == i.id) {
                return i
            }
        }
        return wires.toList()[0]
    }

    fun tick() {
        for (i in map) {
            i.tick()
        }
    }

    fun update() {
        for (i in map) {
            i.update()
        }
        for (i in wires) {
            i.update()
        }
    }

    fun renderUpdate(renderBuffer: RenderBuffer) {
        for (i in map) {
            i.renderUpdate(renderBuffer)
        }
        for (i in wires) {
            i.renderUpdate(renderBuffer)
        }
    }
}