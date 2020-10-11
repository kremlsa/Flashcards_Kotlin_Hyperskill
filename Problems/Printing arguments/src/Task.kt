fun main(args: Array<String>) {
    if (args.size == 3) {
        println("Argument 1: ${args[0]}. It has ${args[0].length} characters")
        println("Argument 2: ${args[1]}. It has ${args[1].length} characters")
        println("Argument 3: ${args[2]}. It has ${args[2].length} characters")
    } else {
        println("Invalid number of program arguments")
    }
}
