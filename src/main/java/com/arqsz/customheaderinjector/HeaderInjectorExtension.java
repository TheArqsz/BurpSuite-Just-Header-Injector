package com.arqsz.customheaderinjector;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import com.arqsz.customheaderinjector.handler.HeaderHandler;
import com.arqsz.customheaderinjector.settings.SettingsManager;
import com.arqsz.customheaderinjector.ui.MainTab;

import javax.swing.*;

public class HeaderInjectorExtension implements BurpExtension {

    private String extensionName = "Custom Header Injector";

    @Override
    public void initialize(MontoyaApi api) {
        api.extension().setName(this.extensionName);

        HeaderHandler handler = new HeaderHandler(api.scope());
        api.http().registerHttpHandler(handler);

        SettingsManager settings = new SettingsManager(api.persistence().preferences(), handler);
        settings.load();

        SwingUtilities.invokeLater(() -> {
            MainTab tab = new MainTab(api.userInterface(), handler, settings, this.extensionName);
            api.userInterface().registerSuiteTab(this.extensionName, tab);
        });

        api.logging().logToOutput("Extension " + this.extensionName + " loaded successfully.");
    }
}
