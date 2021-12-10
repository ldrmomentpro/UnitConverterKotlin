enum class Units(val rate: Double, val singular: String, val plural: String, val twin: List<String>, val type: String) {
    MILLIMETER(0.001, "millimeter", "millimeters", listOf("mm", "millimeter", "millimeters"), "length"),
    CENTIMETER(0.01, "centimeter", "centimeters", listOf("cm", "centimeter", "centimeters"), "length"),
    METER(1.0, "meter", "meters", listOf("m", "meter", "meters"), "length"),
    KILOMETER(1000.0, "kilometer", "kilometers", listOf("km", "kilometer", "kilometers"), "length"),
    MILE(1609.35, "mile", "miles", listOf("mi", "mile", "miles"), "length"),
    YARD(0.9144, "yard", "yards", listOf("yd", "yard", "yards"), "length"),
    FOOT(0.3048, "foot", "feet", listOf("ft", "foot", "feet"), "length"),
    INCH(0.0254, "inch", "inches", listOf("in", "inch", "inches"), "length"),
    MILLIGRAM(0.001, "milligram", "milligrams", listOf("mg", "milligram", "milligrams"), "weight"),
    GRAM(1.0, "gram", "grams", listOf("g", "gram", "grams"), "weight"),
    KILOGRAM(1000.0, "kilogram", "kilograms", listOf("kg", "kilogram", "kilograms"), "weight"),
    POUND(453.592, "pound", "pounds", listOf("lb", "pound", "pounds"), "weight"),
    OUNCE(28.3495, "ounce", "ounces", listOf("oz", "ounce", "ounces"), "weight"),
    CELSIUS(
        0.0,
        "degree Celsius",
        "degrees Celsius",
        listOf("degree Celsius", "degrees Celsius", "celsius", "dc", "c"),
        "temperature"
    ),
    FAHRENHEIT(
        0.0,
        "degree Fahrenheit",
        "degrees Fahrenheit",
        listOf("degree Fahrenheit", "degrees Fahrenheit", "fahrenheit", "df", "f"),
        "temperature"
    ),
    KELVIN(0.0, "kelvin", "kelvins", listOf("kelvin", "kelvins", "k"), "temperature"),
    NULL(0.0, "???", "???", listOf(" "), "unknown");

    companion object {
        fun findTwin(word: String): Units {
            return try {
                values().first() { word in it.twin }
            } catch (e: NoSuchElementException) {
                NULL
            }
        }
    }

    fun checkPlural(value: Double) = if (value != 1.0) plural else singular
}


fun main() {
    var temp: Double
    var result: Double
    while (true) {
        print("Enter what you want to convert (or exit): ")
        val input = readLine()!!
        if (input == "exit") break
        var (rawValue, source, _, target) = input.split(" ")
        if (source.lowercase() == "degree" || source.lowercase() == "degrees") {
            source = input.split(" ")[2]
            target = input.split(" ").last()
        }
        val checkTarget = input.split(" ")[input.split(" ").size - 2].lowercase()
        if (checkTarget == "degree" || checkTarget == "degrees") {
            target = input.split(" ").last()
        }
        val value: Double
        try {
            value = rawValue.toDouble()
        } catch (e: Exception) {
            println("Parse error")
            continue
        }
        val sourceUnit = Units.findTwin(source.lowercase())
        val targetUnit = Units.findTwin(target.lowercase())

        if (sourceUnit.type != targetUnit.type ||
            sourceUnit.type == Units.NULL.type && targetUnit.type == Units.NULL.type) {
            println("Conversion from ${sourceUnit.plural} to ${targetUnit.plural} is impossible")
        } else {
            when {
                sourceUnit.name == Units.CELSIUS.name && targetUnit.name == Units.FAHRENHEIT.name -> {
                    result = value * 9 / 5 + 32
                }
                sourceUnit.name == Units.CELSIUS.name && targetUnit.name == Units.CELSIUS.name -> {
                    result = value
                }
                sourceUnit.name == Units.FAHRENHEIT.name && targetUnit.name == Units.CELSIUS.name -> {
                    result = (value - 32) * 5 / 9
                }
                sourceUnit.name == Units.FAHRENHEIT.name && targetUnit.name == Units.FAHRENHEIT.name -> {
                    result = value
                }
                sourceUnit.name == Units.CELSIUS.name && targetUnit.name == Units.KELVIN.name -> {
                    result = value + 273.15
                }
                sourceUnit.name == Units.KELVIN.name && targetUnit.name == Units.CELSIUS.name -> {
                    result = value - 273.15
                }
                sourceUnit.name == Units.KELVIN.name && targetUnit.name == Units.KELVIN.name -> {
                    result = value
                }
                sourceUnit.name == Units.FAHRENHEIT.name && targetUnit.name == Units.KELVIN.name -> {
                    result = (value - 32) * 5 / 9 + 273.15
                }
                sourceUnit.name == Units.KELVIN.name && targetUnit.name == Units.FAHRENHEIT.name -> {
                    result = (value - 273.15) * 9 / 5 + 32
                }
                else -> {
                    if (value < 0) {
                        println("${sourceUnit.type.first().uppercase()}${sourceUnit.type.drop(1)} shouldn't be negative")
                        continue
                    } else {
                        temp = value * sourceUnit.rate
                        result = temp / targetUnit.rate
                    }
                }
            }
            println("$value ${sourceUnit.checkPlural(value)} is $result ${targetUnit.checkPlural(result)}\n")
        }
    }
}