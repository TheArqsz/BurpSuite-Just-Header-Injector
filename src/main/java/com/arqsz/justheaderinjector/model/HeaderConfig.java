package com.arqsz.justheaderinjector.model;

import java.util.regex.Pattern;

public class HeaderConfig {
    // RFC 7230 token characters
    public static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9!#$%&'*+.^_`|~-]+$");

    private volatile String name;
    private volatile String value;
    private volatile boolean enabled;

    public HeaderConfig(String name, String value, boolean enabled) {
        this.name = name;
        this.value = value;
        this.enabled = enabled;
    }

    public boolean isValid() {
        return enabled && !name.isEmpty() && !value.isEmpty()
                && NAME_PATTERN.matcher(name).matches();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
