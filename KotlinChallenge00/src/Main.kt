/*
Crea un programa para una tienda de ropa que permita:
    Pedir datos del cliente (nombres, fecha de nacimiento y número de contacto)
    Crear una factura al cliente esta debe llevar
        Productos
        Cantidad por producto
        Precio por producto
        Total de la compra.
El sistema debe imprimir una lista de los productos comprados, el valor total de los productos y el valor total de la compra con los impuestos (los impuestos son del 10% de la compra)
*/

const val tax = 0.10
val customerData = Array(3) { "" }

val productsAndPrices = mutableMapOf(
    "Cardigan" to 480.0,
    "Jeans" to 124.0,
    "Polo" to 68.0,
    "Hoodie" to 160.0,
    "Sweater" to 106.0
    )
val arraySize = productsAndPrices.size

val productsQuantity = Array(arraySize){ 0 }
val productsArray = Array(arraySize) { "" }
val pricesArray = Array(arraySize) { 0.0 }


fun main() {
    updateProductsAndPricesArray()

    getCustomerData()

    purchaseSimulation()

    createBill()
}

        // *** Initial setter functions
fun updateProductsAndPricesArray() {
    var index = 0
    productsAndPrices.forEach {
        pricesArray[index] = it.value
        productsArray[index] = it.key
        index++
    }
}

        // *** Business logic functions

fun purchaseSimulation() {
    var continueShopping = true;
    var product = 0
    var quantity = 0

    showProductsAndPrices()

    do {
        print("\nAdd to cart (0 -> exit): ")
        product = readLine()?.toInt() ?: -1

        when(product) {
            0 -> {continueShopping = false}
            in 1 .. arraySize -> {
                print("Quantity: ")
                quantity = readLine()?.toInt() ?: -1
                purchaseProduct(product, quantity)
            }
            else -> println("Error! Please try again")
        }
    } while (continueShopping)
}

fun showProductsAndPrices() {
    var i = 1
    println("\n *** Products catalog *** ")
    productsAndPrices.forEach {
        println("${i++}. ${it.key} \t\t\t ${it.value}")
    }
}

fun purchaseProduct(product : Int, quantity : Int) {
    productsQuantity[product-1] += quantity
}

fun createBill() {
    var total = 0.0

    println("\n-----------------------" +
            "\n\t *** YOUR BILL *** ")
    println("\nItem \t\t # \t\t $ \t\t Total")

    for ((index, itemQuantity) in productsQuantity.withIndex()) {
        total += itemQuantity * pricesArray[index]


        if (itemQuantity > 0) {
            println("${productsArray[index]} \t\t ${itemQuantity} \t\t ${pricesArray[index]} \t\t ${itemQuantity * pricesArray[index]}")
        }
    }
    println("\n-----------------------" +
            "\nSubtotal: $ ${total}")
    println("Tax : $ ${total * tax}")
    println("Total: $ ${total*(1+tax)}" +
            "\n-----------------------")
}

        // *** Customer data getters ***

fun getCustomerData() {
    println("\n *** Add customer data *** ")
    customerData[0] = getCustomerName()
    customerData[1] = getCustomerBirthday()
    customerData[2] = getCustomerContactNumber()
}

fun getCustomerName() : String {
    print("Name: ")
    return readLine() ?: ""
}
fun getCustomerBirthday() : String {
    print("Birthday: ")
    return readLine() ?: ""
}
fun getCustomerContactNumber() : String {
    print("Contact number: ")
    return readLine() ?: ""
}

        // *** Adding products functions

fun addProduct(product: String, price: Double) {
    // this fun will not work, because productsArray and pricesArray initial size
    productsAndPrices.put(product, price)
    updateProductsAndPricesArray()
}
