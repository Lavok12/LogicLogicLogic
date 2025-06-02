package la.vok.GameController.TransferModel

interface TransferModel {
    fun sendData(transferPackage: TransferPackage)
    fun getData(transferPackage: TransferPackage)
}