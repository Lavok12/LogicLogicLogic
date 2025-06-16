package la.vok.GameController.Processes

class ProcessesManager<T : Process> {
    private val list = ArrayDeque<T>()

    val isEmpty: Boolean
        get() = list.isEmpty()

    fun add(process: T) {
        list.addLast(process)
        process.start()
    }

    fun tick() {
        val iterator = list.iterator()
        while (iterator.hasNext()) {
            val process = iterator.next()
            if (!process.pause) {
                process.tick()
            }
            if (process.isEnded()) {
                iterator.remove()
            }
        }
    }


    fun forceEndAll() {
        list.forEach { it.forcedEnd() }
        list.clear()
    }

    fun current(): T? = list.firstOrNull()
}
