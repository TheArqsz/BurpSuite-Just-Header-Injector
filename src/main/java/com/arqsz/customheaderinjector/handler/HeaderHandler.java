package com.arqsz.customheaderinjector.handler;

import burp.api.montoya.scope.Scope;
import burp.api.montoya.http.handler.*;
import burp.api.montoya.http.message.requests.HttpRequest;
import com.arqsz.customheaderinjector.model.HeaderConfig;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class HeaderHandler implements HttpHandler {

    private final Scope scope;
    private final List<HeaderConfig> configList = new CopyOnWriteArrayList<>();

    private volatile boolean globalEnabled = true;
    private volatile boolean scopeOnly = false;

    public HeaderHandler(Scope scope) {
        this.scope = scope;
    }

    @Override
    public RequestToBeSentAction handleHttpRequestToBeSent(HttpRequestToBeSent requestToBeSent) {
        if (!globalEnabled) {
            return RequestToBeSentAction.continueWith(requestToBeSent);
        }

        if (scopeOnly && !scope.isInScope(requestToBeSent.url())) {
            return RequestToBeSentAction.continueWith(requestToBeSent);
        }

        HttpRequest modifiedRequest = requestToBeSent;
        boolean wasModified = false;

        for (HeaderConfig config : configList) {
            if (config.isValid()) {
                modifiedRequest = modifiedRequest.withHeader(config.getName(), config.getValue());
                wasModified = true;
            }
        }

        if (wasModified) {
            return RequestToBeSentAction.continueWith(modifiedRequest);
        }
        return RequestToBeSentAction.continueWith(requestToBeSent);
    }

    @Override
    public ResponseReceivedAction handleHttpResponseReceived(
            HttpResponseReceived responseReceived) {
        return ResponseReceivedAction.continueWith(responseReceived);
    }

    public List<HeaderConfig> getConfigList() {
        return configList;
    }

    public boolean isGlobalEnabled() {
        return globalEnabled;
    }

    public void setGlobalEnabled(boolean globalEnabled) {
        this.globalEnabled = globalEnabled;
    }

    public boolean isScopeOnly() {
        return scopeOnly;
    }

    public void setScopeOnly(boolean scopeOnly) {
        this.scopeOnly = scopeOnly;
    }
}
