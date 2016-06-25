
class FormattedStringBuffer(val indentString: String = "    ") {
    internal var indent = 0
    val sb = StringBuilder()

    fun indent() {
        indent++
    }

    fun deindent() {
        indent--
    }

    fun indented(block: () -> Unit) {
        indent()
        block()
        deindent()
    }

    fun appendIndentation() {
        for (i in 1 .. indent) {
            sb.append(indentString)
        }
    }

    fun append(any: Any) {
        sb.append(any)
    }

    fun appendln(any: Any) {
        append(any)
        newline()
    }

    fun newline() {
        sb.append("\n")
        // appendIndentation()
    }

    override fun toString() = sb.toString()
}

fun <T> formatRec(obj: T, fsb: FormattedStringBuffer = FormattedStringBuffer(), printer: FormattedStringBuffer.(T, (T) -> Unit) -> Unit): String {

    fsb.printer(obj) { subObj ->
        formatRec(subObj, fsb, printer)
    }

    return fsb.toString()
}