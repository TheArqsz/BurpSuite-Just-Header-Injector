package com.arqsz.justheaderinjector;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import com.arqsz.justheaderinjector.handler.HeaderHandler;
import com.arqsz.justheaderinjector.settings.SettingsManager;
import com.arqsz.justheaderinjector.ui.MainTab;

import javax.swing.*;

public class HeaderInjectorExtension implements BurpExtension {

    private String extensionName = "Just Header Injector";

    @Override
    public void initialize(MontoyaApi api) {
        api.extension().setName(this.extensionName);

        HeaderHandler handler = new HeaderHandler(api.scope());
        api.http().registerHttpHandler(handler);

        SettingsManager settings = new SettingsManager(api.persistence().preferences(), handler, api.logging());
        settings.load();

        SwingUtilities.invokeLater(() -> {
            MainTab tab = new MainTab(api.userInterface(), handler, settings, this.extensionName);
            api.userInterface().registerSuiteTab(this.extensionName, tab);
        });

        api.logging().logToOutput("Extension " + this.extensionName + " loaded successfully.");
    }
}
