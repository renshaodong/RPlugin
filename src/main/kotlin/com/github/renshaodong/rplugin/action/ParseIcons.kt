package com.github.renshaodong.rplugin.action

import com.github.renshaodong.rplugin.entity.IconFont
import com.github.renshaodong.rplugin.util.findOrCreateTargetFile
import com.google.gson.Gson
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory

class ParseIcons : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val desc = FileChooserDescriptorFactory.createSingleFileDescriptor("json")
        desc.description = "choose iconfont.json"
        FileChooser.chooseFile(desc, e.project, null) {
            ApplicationManager.getApplication().runWriteAction {
                try {
                    val content = String(it.contentsToByteArray())
                    val iconFont = Gson().fromJson(content, IconFont::class.java)
                    e.project?.run {
                        findOrCreateTargetFile(arrayOf("lib", "generated", "icon", "${iconFont.font_family}.dart"))
                    }?.run {
                        setBinaryContent(iconFont.generateClassContent().toByteArray())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun IconFont.generateClassContent(): String {
        val className = font_family.upperInitials()
        val sb = StringBuilder("import 'package:flutter/widgets.dart';\n\n")
        sb.append("class $className{\n")
        sb.append("\t$className._();").appendln()
        glyphs.forEach {
            sb.append("\tstatic const IconData ${it.font_class} = IconData(0x${it.unicode}, fontFamily: " +
                "\"${font_family}\");\n")
        }
        sb.append("}")
        return sb.toString()
    }

    private fun String.upperInitials(): String {
        if (length <= 1) {
            return toUpperCase()
        }
        return "${substring(0, 1).toUpperCase()}${substring(1)}"
    }
}
