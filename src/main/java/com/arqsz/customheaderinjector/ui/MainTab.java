package com.arqsz.customheaderinjector.ui;

import burp.api.montoya.ui.UserInterface;
import com.arqsz.customheaderinjector.handler.HeaderHandler;
import com.arqsz.customheaderinjector.model.HeaderConfig;
import com.arqsz.customheaderinjector.settings.SettingsManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainTab extends JPanel {

    private final UserInterface uiUtils;
    private final HeaderHandler handler;
    private final SettingsManager settings;
    private final JPanel rowsContainer;

    private static final Color BURP_ORANGE = new Color(229, 106, 26);
    private static final Color BURP_ORANGE_HOVER = new Color(200, 90, 20);

    public MainTab(UserInterface uiUtils, HeaderHandler handler, SettingsManager settings,
            String extensionName) {
        this.uiUtils = uiUtils;
        this.handler = handler;
        this.settings = settings;

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints c = new GridBagConstraints();

        JLabel title = new JLabel(extensionName);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16.0f));
        title.setForeground(new Color(229, 106, 26));
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.insets = new Insets(0, 0, 10, 0);
        topPanel.add(title, c);

        JPanel checks = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JCheckBox chkGlobal = new JCheckBox("Enable globally");
        chkGlobal.setSelected(handler.isGlobalEnabled());
        chkGlobal.addActionListener(e -> {
            handler.setGlobalEnabled(chkGlobal.isSelected());
            settings.save();
        });

        JCheckBox chkScope = new JCheckBox("Include only in-scope requests");
        chkScope.setSelected(handler.isScopeOnly());
        chkScope.addActionListener(e -> {
            handler.setScopeOnly(chkScope.isSelected());
            settings.save();
        });

        checks.add(chkGlobal);
        checks.add(Box.createHorizontalStrut(15));
        checks.add(chkScope);

        c.gridy = 1;
        c.insets = new Insets(0, 0, 5, 0);
        topPanel.add(checks, c);

        rowsContainer = new JPanel();
        rowsContainer.setLayout(new BoxLayout(rowsContainer, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(new JPanel(new BorderLayout()) {
            {
                add(rowsContainer, BorderLayout.NORTH);
            }
        });
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAdd = new JButton("+ Add Header");
        styleButton(btnAdd);
        btnAdd.addActionListener(e -> {
            HeaderConfig newConfig = new HeaderConfig("X-Custom-Header", "Value", true);
            handler.getConfigList().add(newConfig);
            addRowUI(newConfig);
            settings.save();
        });
        bottomPanel.add(btnAdd);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshList();
        uiUtils.applyThemeToComponent(this);
    }

    private void refreshList() {
        rowsContainer.removeAll();
        for (HeaderConfig config : handler.getConfigList()) {
            addRowUI(config);
        }
        rowsContainer.revalidate();
        rowsContainer.repaint();
    }

    private void addRowUI(HeaderConfig config) {
        HeaderRow row = new HeaderRow(uiUtils, config, () -> {
            handler.getConfigList().remove(config);
            refreshList();
            settings.save();
        }, settings::save);
        rowsContainer.add(row);
        rowsContainer.revalidate();
    }

    private void styleButton(JButton btn) {
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(BURP_ORANGE);

        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);

        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(BURP_ORANGE_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(BURP_ORANGE);
            }
        });
    }
}
