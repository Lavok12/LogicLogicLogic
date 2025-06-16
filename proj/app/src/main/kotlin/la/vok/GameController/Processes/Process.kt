package la.vok.GameController.Processes

import la.vok.GameController.GameController
import la.vok.LavokLibrary.MetaStorage

open class Process(protected val gameController: GameController) : MetaStorage() {
    protected var ended = false
    open var ticks = 0
    open fun start() {}
    open fun tick() {}
    open fun end() { ended = true }
    open fun forcedEnd() { ended = true }
    fun isEnded(): Boolean = ended
    open var pause = false
}
