import server.ServerChat
import server.ServerMain

fun main(args: Array<String>) {
    var server = ServerMain(3000)
    server.connect()
    //var server = ServerChat()
    //server.init()
    //server.createSmallRoom(1,"HI", 1)
    //server.enterSmallRoom(1,1,11)
    //server.destroySmallRoom(1,12)
    println("Hello World!")
}