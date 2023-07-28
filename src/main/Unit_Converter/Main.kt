import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

open class Node(val symbol: String) {
    val adjList = HashMap<Node, Float>()
    var currentVal = 0f
    var visited = false

    fun addConnection (node: Node, conversion: Float) {
        adjList[node] = conversion
        //Then we also want to include the inverse conversion
        node.adjList[this] = 1/conversion
    }
    fun print () {
        print("Node $symbol: ")
        for(node in adjList) {
            print(node.key.symbol + " (" + node.value + ") ")
        }
        println()
    }
}

open class Graph {
    private val nodeMap = HashMap<String, Node>()

    fun addNode (node: Node) {
        nodeMap[node.symbol] = node
    }

    fun getNode (sym: String): Node? {
        return if (nodeMap.containsKey(sym)) nodeMap[sym] else null
    }

    fun printNodes() {
        for(node in nodeMap) {
            node.value.print()
        }
    }

    fun doBfs(start:String, end:String, amount: Float): Float? {
        val bfsQueue: Queue<Node> = LinkedList()
        assert(nodeMap.containsKey(start))
        val startNode = nodeMap[start]
        bfsQueue.add(startNode)
        startNode!!.visited = true
        startNode.currentVal = amount
        while(!bfsQueue.isEmpty()) {
            val curNode = bfsQueue.remove()
            for (node in curNode.adjList.keys) {
                if (!node.visited) {
                    bfsQueue.add(node)
                    node.visited = true
                    node.currentVal = curNode.currentVal * curNode.adjList[node]!!
                    if(node.symbol == end) {
                        return node.currentVal
                    }
                }
            }
        }
        return null
    }
}

//val is immutable constant
//var is mutable
fun readConvFile(fileName: String) : ArrayList<ArrayList<String>> {
    val lines = ArrayList<ArrayList<String>>()
    File(fileName).forEachLine {
        lines.add(ArrayList(it.split(" ")))
    }
    return lines
}

fun createGraphFromFile(path: String, convGraph: Graph) {

    val lines = readConvFile(path)

    for (line in lines) {
        val conv = line[0]
        val res = line[1]
        val base = line[2]
        val baseNode = when(val node = convGraph.getNode(base)) {
            is Node -> node
            else -> {
                val newNode = Node(base)
                convGraph.addNode(newNode)
                newNode
            }
        }
        val resNode = when(val node = convGraph.getNode(res)) {
            is Node -> node
            else -> {
                val newNode = Node(res)
                convGraph.addNode(newNode)
                newNode
            }
        }
        baseNode.addConnection(resNode, conv.toFloat())
    }
}

fun pollUser(convGraph: Graph) : Triple<String, String, Float> {
    with(
        Scanner(System.`in`)) {
        println("Welcome to the unit convertor machine!")
        while(true) {
            println("What would you like to convert?")
            val start = nextLine()
            val end = nextLine()
            if (convGraph.getNode(start) !is Node) {
                println("Invalid Start Unit! Try Again!")
                continue
            }
            if (convGraph.getNode(end) !is Node) {
                println("Invalid end Unit! Try Again!")
                continue
            }
            val amount = nextFloat()
            return Triple(start, end, amount)
        }
    }
}


fun main() {
    val convGraph = Graph()

    createGraphFromFile("src/main/Unit_Converter/input.txt", convGraph)
    convGraph.printNodes()

    while(true) {
        val userReq = pollUser(convGraph)
        when (val x = convGraph.doBfs(userReq.first, userReq.second, userReq.third)) {
            is Float -> println(x)
            else -> println("You cannot convert ${userReq.first} to ${userReq.second}")
        }
    }
}
