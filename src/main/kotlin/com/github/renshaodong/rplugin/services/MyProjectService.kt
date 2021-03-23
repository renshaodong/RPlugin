package com.github.renshaodong.rplugin.services

import com.github.renshaodong.rplugin.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
