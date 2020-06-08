/*
* ToDo app
*  User
*  Task*/

fun main() {
    val workEnvironment = WorkEnvironment();

    workEnvironment.createUser("Daniel")
    workEnvironment.createUser("Sier")

    workEnvironment.listUsers();

    workEnvironment.createTask(mutableListOf(workEnvironment.getUserById(1)), "Task00")

    workEnvironment.listTasksByUser()
    workEnvironment.listTasks()

    workEnvironment.changeStatus(1, STATUS.INPROGRESS)
    workEnvironment.listTasks()
    workEnvironment.listTasksByUser()
}

fun showMenu() {
    println()
}