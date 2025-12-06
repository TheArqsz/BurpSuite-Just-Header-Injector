package com.arqsz.customheaderinjector.settings;

import burp.api.montoya.persistence.Preferences;
import com.arqsz.customheaderinjector.handler.HeaderHandler;
import com.arqsz.customheaderinjector.model.HeaderConfig;
import org.json.JSONArray;
import org.json.JSONObject;
import burp.api.montoya.logging.Logging;
import java.util.ArrayList;
import java.util.List;

public class SettingsManager {
    private static final String KEY = "header_injector_data";
    private final Preferences preferences;
    private final HeaderHandler handler;
    private final Logging logging;

    public SettingsManager(Preferences preferences, HeaderHandler handler, Logging logging) {
        this.preferences = preferences;
        this.handler = handler;
        this.logging = logging;
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

            JSONArray arr = root.optJSONArray("headers");
            if (arr != null) {
                List<HeaderConfig> newConfigs = new ArrayList<>();
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    newConfigs.add(new HeaderConfig(
                        obj.optString("n"),
                        obj.optString("v"),
                        obj.optBoolean("e", true)
                    ));
                }
                handler.replaceConfigs(newConfigs); 
            }
        } catch (Exception e) {
            logging.logToError("Failed to load settings: " + e.getMessage());
        }
    }
}
