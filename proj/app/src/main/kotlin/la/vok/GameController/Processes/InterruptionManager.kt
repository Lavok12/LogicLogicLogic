package la.vok.GameController.Processes

class InterruptionManager<T : Process> {
    private val list = ArrayDeque<T>()

    val isEmpty: Boolean
        get() = list.isEmpty()

    fun add(interruption: T) {
        list.addLast(interruption)
        interruption.start()
    }

    fun tick() {
        if (list.firstOrNull()?.pause == false) {
            list.firstOrNull()?.tick()
        }
        if (list.firstOrNull()?.isEnded() == true) {
            list.removeFirst()
        }
    }

    fun forceEndAll() {
        list.forEach { it.forcedEnd() }
        list.clear()
    }

    fun current(): T? = list.firstOrNull()
}