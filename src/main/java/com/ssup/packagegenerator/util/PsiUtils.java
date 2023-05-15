package com.ssup.packagegenerator.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;

public class PsiUtils {

    public static void createFilesAndDirectoriesFromYaml(String yamlString, Project project, PsiDirectory baseDirectory) {
        // YAML accepts double spaces instead of tabs
        String formattedYamlString = yamlString.replace("\t", "  ");

        // Parse YAML into Hashmap
        Map<String, Object> yamlMap = new Yaml().load(formattedYamlString);

        // Setting scope to write files and directories
        WriteCommandAction.runWriteCommandAction(project, () -> {
            createFilesAndDirectoriesFromMap(yamlMap, baseDirectory);
        });
    }

    // recursive dfs type algorithm write directories and files from HashMap
    private static void createFilesAndDirectoriesFromMap(Object data, PsiDirectory baseDirectory) {
        if (data instanceof Map) {
            ((Map<String, Object>) data).forEach((key, value) -> {
                PsiDirectory subDirectory = baseDirectory.createSubdirectory(key);
                createFilesAndDirectoriesFromMap(value, subDirectory);
            });
        } else if (data instanceof List) {
            ((List<?>) data).forEach(it -> {
                createFilesAndDirectoriesFromMap(it, baseDirectory);
            });
        } else {
            String name = (String) data;
            if (name.contains("."))
                baseDirectory.createFile(name);
            else
                baseDirectory.createSubdirectory(name);
        }
    }

    public static PsiDirectory getPath(AnActionEvent event) {
        return (PsiDirectory) event.getData(CommonDataKeys.NAVIGATABLE);
    }

    public static String getModuleName(AnActionEvent event) {
        String moduleName = getPath(event).getName();
        return moduleName.replace(moduleName.substring(0, 1), moduleName.substring(0, 1).toUpperCase());
    }

    // Default YAML for Clean MVVM package structure
    public static String DEFAULT_YAML =
            "di: \n" +
            "  - %1$sNetworkModule.kt \n" +
            "  - %1$sRepositoryModule.kt \n" +
            "presentation:\n" +
            "  - viewmodel: %1$sViewModel.kt \n" +
            "  - component \n" +
            "  - %1$sScreen.kt \n" +
            "data: \n" +
            "  - repository: %1$sRepository.kt \n" +
            "  - network: \n" +
            "      request: %1$sRequest.kt \n" +
            "      response: %1$sResponse.kt \n" +
            "      service: %1$sService.kt \n"
            ;
}
