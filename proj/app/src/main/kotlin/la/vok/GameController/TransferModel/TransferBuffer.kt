package la.vok.GameController.TransferModel

class TransferBuffer(var updater: TransfeUpdater) {
    var packageList: ArrayList<TransferPackage> = ArrayList()

    fun add(pack: TransferPackage) {
        packageList += pack
    }
    fun processingAll() {
        var c = packageList.size
        for (i in 0..(c-1)) {
            updater.processingData(packageList[i])
        }
        for (i in 0..(c-1)) {
            packageList.removeFirst()
        }
    }
}