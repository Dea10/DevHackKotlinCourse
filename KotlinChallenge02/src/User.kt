data class User(val id: Int, val name: String, var tasks: MutableMap<Int, Task> = mutableMapOf())