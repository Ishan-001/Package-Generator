package com.ssup.packagegenerator.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.ssup.packagegenerator.ui.YamlDialog


class CreatePackagesAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        YamlDialog.main(e)
    }
}
