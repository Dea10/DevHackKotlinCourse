import java.lang.Exception
import java.lang.NumberFormatException

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
    val shop = DataSetter.setShop()

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

    fun setShop() : Shop {
        return Shop()
    }
}

data class Customer(val name : String = "no_name",
                    val birthday : String = "no_birthday",
                    val contact : String = "no_contact",
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

    /* I'd like to get an item and a quantity and just assign that to a pair<k, v>, to avoid creating n items in memory*/
    fun addItem(item : Item, quantity : Int) {
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
            var itemId = -1
            print("Add to cart (0 -> exit): ")
            try {
                itemId = readLine()?.toInt() ?: -1
            } catch (e: NumberFormatException) {
                println("Error! Not a valid input")
            }

            var exist = validateItemId(itemCatalog, itemId)

            /* I have a hunch that this could be better => maybe not using nested if's*/
            if (itemId == 0) {
                continueShopping = false
                println(" *** Bill closed! ***")
            } else {
                if (exist) {
                    var quantity = 0
                    print("Quantity: ")
                    try {
                        quantity = readLine()?.toInt() ?: -1
                        if(quantity < 0){
                            throw NumberFormatException()
                        }
                    } catch (e: NumberFormatException) {
                        println("Error! Not a valid input")
                    }

                    var items = itemCatalog.items.filter {
                        it.id == itemId
                    }

                    bill.addItem(items[0], quantity)
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

    private fun validateItemId(itemCatalog: ItemCatalog, itemId: Int): Boolean {
        itemCatalog.items.forEach {
            if (it.id == itemId) {
                return true
            }
        }
        return false
    }
}
