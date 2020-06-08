import java.lang.NumberFormatException

/*
* ToDo app
*  User
*  Task*/

fun main() {
    val workEnvironment = WorkEnvironment();
    var exit = true

    workEnvironment.createUser("Test User")
    workEnvironment.createTask(workEnvironment.getUserById(1), "Task00")
/*
    workEnvironment.createUser("Daniel")


    workEnvironment.listUsers();

    workEnvironment.createTask(mutableListOf(workEnvironment.getUserById(1)), "Task00")

    workEnvironment.listTasksByUser()
    workEnvironment.listTasks()

    workEnvironment.changeStatus(1, STATUS.INPROGRESS)
    workEnvironment.listTasks()
    workEnvironment.listTasksByUser()
 */

    do {
        showMenu()
        runMainApp(getUserInput(), workEnvironment)
    } while (exit)

}

fun showMenu() {
    println("\t\t *** ToDo App *** ")
    println("1. Create user")
    println("2. Create task")
    println("3. Change task status")
    println("4. Re-assign task")
    println("5. List tasks")
    println("0. Exit")

    print("\n\tR: ")
}

fun getUserInput(): Int{
    var userInput: Int = 0

    try {
        userInput = readLine()?.toInt() ?: -1
        if(userInput < 0){
            throw NumberFormatException()
        }
    } catch (e: NumberFormatException) {
        println("Error! Not a valid input")
    }

    return userInput
}

fun runMainApp(userInput: Int, workEnvironment: WorkEnvironment) {
    when(userInput) {
        1 -> createUser(workEnvironment)
        2 -> createTask(workEnvironment)
        3 -> changeTaskStatus(workEnvironment)
        4 -> println("comming soon")
        5 -> listTasks(workEnvironment)
    }
}

fun createUser(workEnvironment: WorkEnvironment) {
    print("User name: ")
    val userName = readLine() ?: "user"
    workEnvironment.createUser(userName)
    println("User created!")
}

fun createTask(workEnvironment: WorkEnvironment) {
    print("Task title: ")
    val taskTitle = readLine() ?: "new task"
    workEnvironment.listUsers()
    print("Select an user id to assign the new task: ")
    var userId: Int = 0

    try {
        userId = readLine()?.toInt() ?: -1
        if(userId < 0){
            throw NumberFormatException()
        }
    } catch (e: NumberFormatException) {
        println("Error! Not a valid input")
    }

    workEnvironment.createTask(workEnvironment.getUserById(userId), taskTitle)
}

fun listTasks(workEnvironment: WorkEnvironment) {
    workEnvironment.listTasksByUser()
}

fun changeTaskStatus(workEnvironment: WorkEnvironment) {
    // list tasks
    workEnvironment.listTasks()
    // read input
    // list status
    workEnvironment.listStatus()
    // read input
    //workEnvironment.changeStatus(taskId, newStatus)
}