/*
Crea un programa para una tienda de ropa que permita:
    Pedir datos del cliente (nombres, fecha de nacimiento y número de contacto)
    Crear una factura al cliente esta debe llevar
        * Productos
        * Cantidad por producto
        * Precio por producto
        * Total de la compra.
El sistema debe imprimir una lista de los productos comprados, el valor total de los productos y el valor total de la compra con los impuestos (los impuestos son del 10% de la compra)
*/

fun main() {
    // setup environment
    val itemCatalog = DataSetter.setCatalog()
    val customer = DataSetter.setCustomer()

    val shop = Shop()

    shop.newBill(customer, itemCatalog)
    shop.printCustomerBills(customer)
}

object DataSetter {
    fun setCustomer() : Customer {
        println("\n *** Add customer data *** ")
        val name = getCustomerName()
        val birthday = getCustomerBirthday()
        val contact = getCustomerContactNumber()

        return Customer(name, birthday, contact)
    }

    private fun getCustomerName() : String {
        print("Name: ")
        return readLine() ?: ""
    }
    private fun getCustomerBirthday() : String {
        print("Birthday: ")
        return readLine() ?: ""
    }
    private fun getCustomerContactNumber() : String {
        print("Contact number: ")
        return readLine() ?: ""
    }

    fun setCatalog() : ItemCatalog {
        return ItemCatalog(mutableListOf(
                Item(1, "Cardigan", 480.0),
                Item(2, "Jeans", 124.0),
                Item(3, "Polo", 68.0),
                Item(4, "Hoodie", 160.0),
                Item(5, "Sweater", 106.0)
            )
        )
    }
}

data class Customer(val name : String = "",
                    val birthday : String = "",
                    val contact : String = "",
                    val bills : MutableList<Bill> = mutableListOf()
)

data class Item(val id : Int, val itemName : String, val price : Double)

class ItemCatalog(val items : MutableList<Item>) {
    fun showCatalog() {
        println("id \t item \t $")
        items.forEach {
            println("${it.id} \t ${it.itemName} \t ${it.price}")
        }
        println()
    }
}

class Bill() {
    var purchasedItems = mutableListOf<Item>()

    fun addProduct(item : Item, quantity : Int) {
        for (i in 1..quantity)
        purchasedItems.plusAssign(item)
    }

    fun printBill() {

        var total = 0.0
        val itemsCount = purchasedItems.groupingBy {it}.eachCount()

        println("\n\t------ Your Bill ------")
        println("\n\tItem \t\t # \t\t $ \t\t Total")

        purchasedItems.distinct().forEach {
            total += itemsCount.getValue(it) * it.price
            println("\t ${it.itemName} \t ${itemsCount.getValue(it)} \t ${it.price} \t ${itemsCount.getValue(it) * it.price}")
        }

        println("\nSub total:  ${total}")
        println("Total: ${total * (Shop.TAX + 1)}")
        println("\t-----------------------")
    }
}



class Shop() {

    companion object {
        const val TAX = 0.10
    }

    fun newBill(customer : Customer, itemCatalog: ItemCatalog) {
        var continueShopping = true;
        do {
            println("\n *** New bill opened :) *** ")

            var bill = purchaseSimulation(itemCatalog)
            customer.bills.add(bill)

            println("\n1. Open new bill")
            println("0. Finish shopping")
            print("R: ")

            if (readLine()?.toInt() ?: -1 == 0){
                continueShopping = false
                println(" *** Good bye! ***")
            }

        } while (continueShopping)

        // add bill to customer
    }


    private fun purchaseSimulation(itemCatalog: ItemCatalog) : Bill {
        var continueShopping = true;
        var bill = Bill()

        itemCatalog.showCatalog()

        do {
            print("Add to cart (0 -> exit): ")
            var productId = readLine()?.toInt() ?: -1

            var exist = validateProductId(itemCatalog, productId)

            if (productId == 0) {
                continueShopping = false
                println(" *** Bill closed! ***")
            } else {
                if (exist) {
                    print("Quantity: ")
                    var quantity = readLine()?.toInt() ?: -1
                    var products = itemCatalog.items.filter {
                        it.id == productId
                    }

                    bill.addProduct(products[0], quantity)
                } else {
                    println("Error: Item not found!!!")
                }
            }
        } while (continueShopping)

        return bill
    }

    fun printCustomerBills(customer : Customer) {
        customer.bills.forEach {
            it.printBill()
        }
    }

    private fun validateProductId(itemCatalog: ItemCatalog, productId: Int): Boolean {
        itemCatalog.items.forEach {
            if (it.id == productId) {
                return true
            }
        }
        return false
    }
}
