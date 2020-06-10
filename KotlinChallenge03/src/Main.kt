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
    val shop = Shop()

    shop.newBill(customer, itemCatalog)
    shop.printCustomerBills(customer)
}

object DataSetter {
    val printRead = { message: String ->
        print(message)
        readLine() ?: ""
    }

    val setCustomer = {
        println("\n *** Add customer data *** ")
        val name = printRead("Name: ")
        val birthday = printRead("Birthday: ")
        val contact = printRead("Contact number: ")

        Customer(name, birthday, contact)
    }

    val setCatalog = {
        ItemCatalog(mutableListOf(
            Item(1, "Cardigan", 480.0),
            Item(2, "Jeans", 124.0),
            Item(3, "Polo", 68.0),
            Item(4, "Hoodie", 160.0),
            Item(5, "Sweater", 106.0)
        )
        )
    }
}

data class Customer(val name : String = "no_name",
                    val birthday : String = "no_birthday",
                    val contact : String = "no_contact",
                    val bills : MutableList<Bill> = mutableListOf()
)

data class Item(val id : Int, val itemName : String, val price : Double)

class ItemCatalog(val items : MutableList<Item>) {
    val showCatalog = {
        println("id \t item \t $")
        items.forEach {
            println("${it.id} \t ${it.itemName} \t ${it.price}")
        }
        println()
    }
}

class Bill {
    private var purchasedItems = mutableListOf<Item>()

    /* I'd like to get an item and a quantity and just assign that to a pair<k, v>, to avoid creating n items in memory*/
    val addItem = { item : Item, quantity : Int -> Unit
        for (i in 1..quantity)
            purchasedItems.plusAssign(item)
    }

    val printBill = {
        var total = 0.0
        val itemsCount = purchasedItems.groupingBy {it}.eachCount()

        println("\n\t------ Your Bill ------")
        println("\n\tItem \t\t # \t\t $ \t\t Total")

        purchasedItems.distinct().forEach {
            total += itemsCount.getValue(it) * it.price
            println("\t ${it.itemName} \t ${itemsCount.getValue(it)} \t ${it.price} \t ${itemsCount.getValue(it) * it.price}")
        }

        println("\nSub total:  $total")
        println("Total: ${total * (Shop.TAX + 1)}")
        println("\t-----------------------")
    }
}

class Shop {

    companion object {
        const val TAX = 0.10
    }

    val printRead = { message: String ->
        print(message)
        readLine() ?: ""
    }

    val newBill = { customer : Customer, itemCatalog: ItemCatalog -> Unit
        var continueShopping = true
        do {
            println("\n *** New bill opened :) *** ")

            val bill = purchaseSimulation(itemCatalog)
            customer.bills.add(bill)

            println("\n1. Open new bill")
            println("0. Finish shopping")

            if (printRead("R: ") == "0"){
                continueShopping = false
                println(" *** Good bye! ***")
            }

        } while (continueShopping)
    }


    private val purchaseSimulation  = { itemCatalog: ItemCatalog ->
        var continueShopping = true
        val bill = Bill()

        itemCatalog.showCatalog()

        do {
            var itemId = -1
            try {
                itemId = printRead("Add to cart (0 -> exit): ").toInt()
            } catch (e: NumberFormatException) {
                println("Error! Not a valid input")
            }

            val exist = validateItemId(itemCatalog, itemId)

            /* I have a hunch that this could be better => maybe not using nested if's*/
            if (itemId == 0) {
                continueShopping = false
                println(" *** Bill closed! ***")
            } else {
                if (exist) {
                    var quantity = 0
                    try {
                        quantity = printRead("Quantity: ").toInt()
                        if(quantity < 0){
                            throw NumberFormatException()
                        }
                    } catch (e: NumberFormatException) {
                        println("Error! Not a valid input")
                    }

                    val items = itemCatalog.items.filter {
                        it.id == itemId
                    }

                    bill.addItem(items[0], quantity)
                } else {
                    println("Error: Item not found!!!")
                }
            }
        } while (continueShopping)

        bill
    }

    val printCustomerBills = { customer : Customer -> Unit
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