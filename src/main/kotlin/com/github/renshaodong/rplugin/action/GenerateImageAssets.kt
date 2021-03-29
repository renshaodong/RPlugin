package com.github.renshaodong.rplugin.action

import com.github.renshaodong.rplugin.util.findOrCreateTargetFile
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile

class GenerateImageAssets : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        e.project!!.run {
            LocalFileSystem.getInstance().findFileByPath(basePath!!)!!
        }.findChild("assets")?.run {
            findImage()
        }?.let {
            ApplicationManager.getApplication().runWriteAction {
                val file = e.project!!.findOrCreateTargetFile(arrayOf("lib", "generated", "image_assets.dart"))
                val start = e.project!!.basePath!!.length
                file.setBinaryContent(it.toContent(start + 1).toByteArray())
            }
        }
    }

    private fun VirtualFile.findImage(): List<VirtualFile> {
        val result = mutableListOf<VirtualFile>()
        children.forEach {
            if (it.isDirectory) {
                result.addAll(it.findImage())
            } else {
                val extension = it.extension
                if (extension == "png" || extension == "jpg") {
                    result.add(it)
                }
            }
        }
        return result
    }

    private fun List<VirtualFile>.toContent(start: Int): String {
        val sb = StringBuilder("class ImageAssets{\n")
        forEach {
            sb.append("\tstatic const String ${it.nameWithoutExtension}='${it.path.substring(start)}';\n")
        }
        sb.append("}")
        return sb.toString()
    }
}