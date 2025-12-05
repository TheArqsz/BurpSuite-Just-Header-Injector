package com.arqsz.customheaderinjector.settings;

import burp.api.montoya.persistence.Preferences;
import com.arqsz.customheaderinjector.handler.HeaderHandler;
import com.arqsz.customheaderinjector.model.HeaderConfig;
import org.json.JSONArray;
import org.json.JSONObject;

public class SettingsManager {
    private static final String KEY = "header_injector_data";
    private final Preferences preferences;
    private final HeaderHandler handler;

    public SettingsManager(Preferences preferences, HeaderHandler handler) {
        this.preferences = preferences;
        this.handler = handler;
    }

    public void save() {
        try {
            JSONObject root = new JSONObject();
            root.put("global", handler.isGlobalEnabled());
            root.put("scope", handler.isScopeOnly());

            JSONArray arr = new JSONArray();
            for (HeaderConfig config : handler.getConfigList()) {
                JSONObject obj = new JSONObject();
                obj.put("n", config.getName());
                obj.put("v", config.getValue());
                obj.put("e", config.isEnabled());
                arr.put(obj);
            }
            root.put("headers", arr);

            preferences.setString(KEY, root.toString());
        } catch (Exception ignored) {
        }
    }

    public void load() {
        String data = preferences.getString(KEY);
        if (data == null || data.isEmpty())
            return;

        try {
            JSONObject root = new JSONObject(data);
            handler.setGlobalEnabled(root.optBoolean("global", true));
            handler.setScopeOnly(root.optBoolean("scope", false));

            handler.getConfigList().clear();
            JSONArray arr = root.optJSONArray("headers");
            if (arr != null) {
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    handler.getConfigList().add(new HeaderConfig(obj.optString("n"),
                            obj.optString("v"), obj.optBoolean("e", true)));
                }
            }
        } catch (Exception ignored) {
        }
    }
}
