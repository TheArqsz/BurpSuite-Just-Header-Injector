package com.arqsz.justheaderinjector.ui;

import burp.api.montoya.ui.UserInterface;
import com.arqsz.justheaderinjector.model.HeaderConfig;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class HeaderRow extends JPanel {
    private final HeaderConfig config;
    private final Runnable onRemove;
    private final Runnable onUpdate;
    private final JTextField txtName;

    private final Border defaultBorder;
    private final Border errorBorder;

    public HeaderRow(UserInterface uiUtils, HeaderConfig config, Runnable onRemove,
            Runnable onUpdate) {
        this.config = config;
        this.onRemove = onRemove;
        this.onUpdate = onUpdate;

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10),
                BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),
                        BorderFactory.createEmptyBorder(8, 8, 8, 8))));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 5, 0, 5);

        JCheckBox chk = new JCheckBox();
        chk.setSelected(config.isEnabled());
        chk.setOpaque(false);
        chk.addActionListener(e -> {
            config.setEnabled(chk.isSelected());
            onUpdate.run();
        });
        c.gridx = 0;
        c.weightx = 0.0;
        add(chk, c);

        txtName = new JTextField(config.getName(), 15);
        defaultBorder = txtName.getBorder();

        Insets i = defaultBorder.getBorderInsets(txtName);
        errorBorder = new CompoundBorder(
                new MatteBorder(1, 1, 1, 1, Color.RED),
                new EmptyBorder(
                    Math.max(0, i.top - 1),
                    Math.max(0, i.left - 1),
                    Math.max(0, i.bottom - 1),
                    Math.max(0, i.right - 1)
                )
        );

        txtName.getDocument().addDocumentListener((SimpleDocListener) () -> {
            config.setName(txtName.getText());
            validateInput();
        });

        txtName.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onUpdate.run();
            }
        });

        c.gridx = 1;
        c.weightx = 0.3;
        add(txtName, c);

        c.gridx = 2;
        c.weightx = 0.0;
        add(new JLabel(":"), c);

        JTextField txtValue = new JTextField(config.getValue(), 15);
        txtValue.getDocument().addDocumentListener((SimpleDocListener) () -> {
            config.setValue(txtValue.getText());
        });

        txtValue.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onUpdate.run();
            }
        });

        c.gridx = 3;
        c.weightx = 0.7;
        add(txtValue, c);

        JButton btnRemove = new JButton("Remove");
        btnRemove.setForeground(Color.RED);
        btnRemove.setFont(btnRemove.getFont().deriveFont(Font.BOLD));
        btnRemove.addActionListener(e -> onRemove.run());
        c.gridx = 4;
        c.weightx = 0.0;
        add(btnRemove, c);

        uiUtils.applyThemeToComponent(this);
        setOpaque(false);
        validateInput();
    }

    private void validateInput() {
        if (txtName.getText().isEmpty()) {
            txtName.setBorder(errorBorder);
            txtName.setToolTipText("Header name cannot be empty");
        } else if (!HeaderConfig.NAME_PATTERN.matcher(txtName.getText()).matches()) {
            txtName.setBorder(errorBorder);
            txtName.setToolTipText("Invalid characters in header name");
        } else {
            txtName.setBorder(defaultBorder);
            txtName.setToolTipText(null);
        }
    }

    @FunctionalInterface
    interface SimpleDocListener extends DocumentListener {
        void update();

        @Override
        default void insertUpdate(DocumentEvent e) {
            update();
        }

        @Override
        default void removeUpdate(DocumentEvent e) {
            update();
        }

        @Override
        default void changedUpdate(DocumentEvent e) {
            update();
        }
    }
}
