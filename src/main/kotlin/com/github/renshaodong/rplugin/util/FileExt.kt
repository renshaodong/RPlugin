package com.github.renshaodong.rplugin.util

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile

fun VirtualFile.findOrCreateDir(dirName: String): VirtualFile {
    val dir = findChild(dirName)
    if (dir != null) {
        return dir
    }
    return createChildDirectory(null, dirName)
}

fun Project.findOrCreateTargetFile(pathList: Array<String>): VirtualFile {
    var file = LocalFileSystem.getInstance().findFileByPath(basePath!!)!!
    pathList.forEachIndexed { index, path ->
        file = if (index == pathList.size - 1) {
            file.findOrCreateChildData(null, path)
        } else {
            file.findOrCreateDir(path)
        }
    }
    return file
}
