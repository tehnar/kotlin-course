package ru.spbau.mit

import java.io.Closeable
import java.io.OutputStreamWriter
import java.io.Writer

class TexWriter(private val identSize: Int, private val out: Writer): Closeable {

    override fun close() = out.close()

    fun indent(indentLevel: Int) = out.write(" ".repeat(indentLevel * identSize))

    fun args(args: List<String>) {
        if (args.isNotEmpty()) {
            out.write(args.joinToString("}{", "{", "}"))
        }
    }

    fun optionalArgs(args: Array<out String>) {
        if (args.isNotEmpty()) {
            out.write(args.joinToString(",", "[", "]"))
        }
    }

    fun text(text: String) = out.write(text)

    fun newLine() = out.write("\n")
}

interface Element {

    fun writeTo(writer: TexWriter, indentLevel: Int)

    companion object Element {
    }
}

class TextElement(val text: String): Element {
    override fun writeTo(writer: TexWriter, indentLevel: Int) {
        writer.indent(indentLevel)
        writer.text(text)
    }
}

@DslMarker annotation class TexTagMaker

@TexTagMaker
abstract class InlineCommand(private val name: String,
                             private val args: List<String> = listOf(),
                             private vararg val optionalArgs: String): Element {
    override fun writeTo(writer: TexWriter, indentLevel: Int) {
        writer.indent(indentLevel)
        writer.text("\\$name")
        writer.optionalArgs(optionalArgs)
        writer.args(args)
    }
}


@TexTagMaker
abstract class BlockCommand(private val name: String,
                            private val args: List<String> = listOf(),
                            private vararg val optionalArgs: String): Element {
    val children: MutableList<Element> = mutableListOf()

    override fun writeTo(writer: TexWriter, indentLevel: Int) {
        writer.indent(indentLevel)
        writer.text("\\begin{$name}")
        writer.optionalArgs(optionalArgs)
        writer.args(args)
        writer.newLine()
        children.forEach {
            it.writeTo(writer, indentLevel + 1)
            writer.newLine()
        }
        writer.indent(indentLevel)
        writer.text("\\end{$name}")
    }

    fun customInlineCommand(name: String, args: List<String> = listOf(), vararg optionalArgs: String) {
        initAndAddElement(CustomInlineCommand(name, args, *optionalArgs), {})
    }

    fun customBlockCommand(name: String, args: List<String> = listOf(), vararg optionalArgs: String,
                           init: CustomBlockCommand.() -> Unit) {
        initAndAddElement(CustomBlockCommand(name, args, *optionalArgs), init)
    }

    operator fun String.unaryPlus() {
        this.trimIndent().split("\n").forEach {
            children.add(TextElement(it))
        }
    }

    protected fun <T: Element> initAndAddElement(element: T, init: T.() -> Unit) {
        element.init()
        children.add(element)
    }
}

class CustomInlineCommand(name: String,
                          args: List<String> = listOf(),
                          vararg optionalArgs: String): InlineCommand(name, args, *optionalArgs)

class CustomBlockCommand(name: String,
                         args: List<String> = listOf(),
                         vararg optionalArgs: String): BlockCommand(name, args, *optionalArgs)

abstract class ListCommand(name: String,
                           args: List<String> = listOf(),
                           vararg optionalArgs: String): TextBlockCommand(name, args, *optionalArgs) {
    fun item(init: TextBlockCommand.() -> Unit) {
        initAndAddElement(ItemCommand(), init)
    }
}

class ItemCommand : TextBlockCommand("item") {
    override fun writeTo(writer: TexWriter, indentLevel: Int) {
        writer.indent(indentLevel)
        writer.text("\\item")
        writer.newLine()
        children.forEach {
            it.writeTo(writer, indentLevel + 1)
            writer.newLine()
        }
    }
}

class EnumerateCommand : ListCommand("enumerate")

class ItemizeCommand : ListCommand("itemize")

class MathCommand : BlockCommand("displaymath")

class FrameTitle(title: String): InlineCommand("frametitle", listOf(title))

class FrameCommand(frameTitle: String, vararg options: String): TextBlockCommand("frame", optionalArgs = *options) {
    init {
        initAndAddElement(FrameTitle(frameTitle), {})
    }
}

class LeftAlign : TextBlockCommand("flushleft")

class RightAlign : TextBlockCommand("flushright")

class CenterAlign : TextBlockCommand("center")

open class TextBlockCommand(name: String,
                            args: List<String> = listOf(),
                            vararg optionalArgs: String): BlockCommand(name, args, *optionalArgs) {
    fun enumerate(init: EnumerateCommand.() -> Unit) = initAndAddElement(EnumerateCommand(), init)


    fun itemize(init: ItemizeCommand.() -> Unit) = initAndAddElement(ItemizeCommand(), init)


    fun frame(frameTitle: String, vararg options: String, init: FrameCommand.() -> Unit) =
            initAndAddElement(FrameCommand(frameTitle, *options), init)

    fun math(init: MathCommand.() -> Unit) = initAndAddElement(MathCommand(), init)

    fun leftAlign(init: LeftAlign.() -> Unit) = initAndAddElement(LeftAlign(), init)

    fun rightAlign(init: RightAlign.() -> Unit) = initAndAddElement(RightAlign(), init)

    fun centerAlign(init: CenterAlign.() -> Unit) = initAndAddElement(CenterAlign(), init)
}

class DocumentClass(clazz: String, vararg options: String):
        InlineCommand("documentclass", listOf(clazz), *options)

class UsePackage(packageName: String, vararg options: String):
        InlineCommand("usepackage", listOf(packageName), *options)

class TexException(reason: String): Exception(reason)

class TexDocument: TextBlockCommand("") {
    private var documentClass: DocumentClass? = null
    private val packages: MutableList<UsePackage> = mutableListOf()

    fun documentClass(clazz: String, vararg options: String) {
        if (documentClass != null) {
            throw TexException("Multiple documentClass statements")
        }
        documentClass = DocumentClass(clazz, *options)
    }

    fun usePackage(packageName: String, vararg options: String) = packages.add(UsePackage(packageName, *options))

    override fun writeTo(writer: TexWriter, indentLevel: Int) {
        documentClass?.writeTo(writer, indentLevel) ?: throw TexException("Document class is not specified")
        writer.newLine()
        packages.forEach {
            it.writeTo(writer, indentLevel)
            writer.newLine()
        }
        writer.indent(indentLevel)
        writer.text("\\begin{document}")
        writer.newLine()
        children.forEach {
            it.writeTo(writer, indentLevel + 1)
            writer.newLine()
        }
        writer.indent(indentLevel)
        writer.text("\\end{document}")
        writer.newLine()
    }
}

fun document(init: TexDocument.() -> Unit): TexDocument {
    val doc = TexDocument()
    doc.init()
    return doc
}

fun main(args: Array<String>) {
    val rows = listOf("aaa", "bbb", "ccc")
    TexWriter(2, OutputStreamWriter(System.out)).use {
        document {
            documentClass("beamer")
            usePackage("babel", "russian" /* varargs */)
            frame(frameTitle="frametitle") {
                itemize {
                    for (row in rows) {
                        item { + "$row text" }
                    }
                    customInlineCommand("setcounter", listOf("enumi", "10"))
                    item { +"11th item" }
                }

                // begin{pyglist}[language=kotlin]...\end{pyglist}
                customBlockCommand(name = "gather*") {
                    +"""
                       1 + 2 = 3 \\
                       2 * 3 = 6
                    """
                }
                math {
                    customInlineCommand("frac", listOf("x + y", "z - t"))
                    + " + "
                    customInlineCommand("sin", listOf("0.123"))
                }
            }
        }.writeTo(it, 0)
    }
}