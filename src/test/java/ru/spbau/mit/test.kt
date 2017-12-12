package ru.spbau.mit
import kotlin.test.assertEquals
import org.junit.Test
import java.io.StringWriter

class TestSource {
    private val stringWriter = StringWriter()
    private val writer = TexWriter(4, stringWriter)

    @Test(expected = TexException::class)
    fun testEmpty() {
        document {  }.writeTo(writer, 0)

    }

    @Test
    fun testEmptyWithClass() {
        document { documentClass("testClass") }.writeTo(writer, 0)
        assertEquals("""
            \documentclass{testClass}
            \begin{document}
            \end{document}

        """.trimIndent(), stringWriter.toString())
    }

    @Test
    fun testPackage() {
        document {
            documentClass("testClass")
            usePackage("package1", "1=2", "3=4")
            usePackage("package2")
            usePackage("package3", "123")
        }.writeTo(writer, 0)

        assertEquals("""
            \documentclass{testClass}
            \usepackage[1=2,3=4]{package1}
            \usepackage{package2}
            \usepackage[123]{package3}
            \begin{document}
            \end{document}

        """.trimIndent(), stringWriter.toString())
    }

    @Test
    fun testLists() {
        document {
            documentClass("testClass")
            enumerate {
                item { +"item1" }
                item { +"item3" }
            }

        }.writeTo(writer, 0)

        assertEquals("""
            \documentclass{testClass}
            \begin{document}
                \begin{enumerate}
                    \item
                        item1

                    \item
                        item3

                \end{enumerate}
            \end{document}

        """.trimIndent(), stringWriter.toString())
    }

    @Test
    fun testMath() {
        document {
            documentClass("testClass")
            math {
                +"(1 + 2)"
                +"/"
                +"3"
            }
        }.writeTo(writer, 0)

        assertEquals("""
            \documentclass{testClass}
            \begin{document}
                \begin{displaymath}
                    (1 + 2)
                    /
                    3
                \end{displaymath}
            \end{document}

        """.trimIndent(), stringWriter.toString())

    }

    @Test
    fun testAlign() {
        document {
            documentClass("testClass")
            rightAlign { +"rightAlignText" }
            leftAlign { +"leftAlignText" }
            centerAlign {
                math {
                    +"centerAlignMath"
                }
            }
        }.writeTo(writer, 0)

        assertEquals("""
            \documentclass{testClass}
            \begin{document}
                \begin{flushright}
                    rightAlignText
                \end{flushright}
                \begin{flushleft}
                    leftAlignText
                \end{flushleft}
                \begin{center}
                    \begin{displaymath}
                        centerAlignMath
                    \end{displaymath}
                \end{center}
            \end{document}

        """.trimIndent(), stringWriter.toString())

    }

    @Test
    fun testCustom() {
        document {
            documentClass("testClass")
            customInlineCommand("inline1")
            customBlockCommand("block1") {
                customInlineCommand("inline2", listOf("arg1", "arg2"))
                customInlineCommand("inline3", listOf("arg3", "arg4"), "opt1", "opt2")
                customBlockCommand("block2", listOf("blockArg1")) {
                    customInlineCommand("inline4", listOf(), "opt1", "opt2")
                    +"blockText"
                }
            }
        }.writeTo(writer, 0)

        assertEquals("""
            \documentclass{testClass}
            \begin{document}
                \inline1
                \begin{block1}
                    \inline2{arg1}{arg2}
                    \inline3[opt1,opt2]{arg3}{arg4}
                    \begin{block2}{blockArg1}
                        \inline4[opt1,opt2]
                        blockText
                    \end{block2}
                \end{block1}
            \end{document}

        """.trimIndent(), stringWriter.toString())
    }
}
