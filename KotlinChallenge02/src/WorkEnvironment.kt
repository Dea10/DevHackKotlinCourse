class WorkEnvironment() {
    val users = mutableMapOf<Int, User>()
    val tasks = mutableMapOf<Int, Task>()

    companion object {
        var usersCount = 0
        var tasksCount = 0
    }

    fun listStatus() {
        STATUS.values().forEach {
            println("${it.ordinal} - ${it.name}")
        }
    }

    fun createUser(name: String) {
        val user = User(++usersCount, name)
        users.put(usersCount, user)
    }

    fun listUsers() {
        users.forEach {
            println("${it.key} - ${it.value.name}")
        }
    }

    fun createTask(user: User, taskTitle: String) {
        val task = Task(++tasksCount, taskTitle)
        tasks.put(tasksCount, task)
        user.tasks.put(tasksCount, task)
    }

    fun listTasks() {
        tasks.forEach {
            println("${it.key} - ${it.value.title} - ${it.value.status}")
        }
    }

    fun listTasksByUser() {
        users.forEach { user ->
            user.value.tasks.forEach {task ->
                println("${user.value.name} - ${task.value.id} ${task.value.title} - ${task.value.status}")
            }
        }
    }

    fun changeStatus(taskId: Int, newStatus: STATUS) {
        tasks[taskId]!!.status = newStatus

        users.forEach {
            it.value.tasks[taskId]?.status ?: newStatus
        }
    }

    fun deleteTask(taskId: Int) {
        tasks.remove(taskId)

        users.forEach {
            it.value.tasks.remove(taskId)
        }
    }

    fun reassignTask(oldUserId: Int, newUserId: Int, taskId: Int) {
        val task = getTaskById(taskId)
        val oldUser = getUserById(oldUserId)
        val newUser = getUserById(newUserId)
        // delete task
        oldUser.tasks.remove(taskId)
        assignTask(newUser, task)
    }

    fun assignTask(user: User, task: Task) {
        user.tasks.put(task.id, task)
    }

    fun getUserById(userId: Int): User = users[userId]!!
    fun getTaskById(taskId: Int): Task = tasks[taskId]!!
}