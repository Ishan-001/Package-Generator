package com.ssup.packagegenerator.ui;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.JBColor;
import com.ssup.packagegenerator.util.PsiUtils;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import javax.swing.*;
import java.awt.event.*;

public class YamlDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private RSyntaxTextArea codeEditor;
    private JScrollPane scrollPanel;
    private JTextField errorField;
    private final AnActionEvent event;

    public YamlDialog(AnActionEvent event) {
        this.event = event;

        setupContent();
        setupButtons();
        setupCodeEditor();
    }

    private void setupCodeEditor() {
        scrollPanel.setBorder(null);
        codeEditor.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        codeEditor.setSelectedTextColor(JBColor.WHITE);
        codeEditor.setUseSelectedTextColor(true);
        codeEditor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_YAML);

        errorField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        errorField.setOpaque(false);

        String module = PsiUtils.getModuleName(event);
        String defaultYaml = PsiUtils.DEFAULT_YAML;

        // Make packages and files adhere to current package name
        codeEditor.setText(String.format(defaultYaml, module));
    }

    private void setupButtons() {
        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());
    }

    private void setupContent() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Package Generator");
        setCancellableActions();
    }

    private void setCancellableActions() {
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );
    }

    private void onOK() {
        try {
            PsiUtils.createFilesAndDirectoriesFromYaml(
                    codeEditor.getText(),
                    event.getProject(),
                    PsiUtils.getPath(event)
            );
            errorField.setText(null);
        } catch (Exception e) {
            errorField.setText("Incorrect YAML");
        }
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public static void main(AnActionEvent event) {
        YamlDialog dialog = new YamlDialog(event);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        System.exit(0);
    }
}
