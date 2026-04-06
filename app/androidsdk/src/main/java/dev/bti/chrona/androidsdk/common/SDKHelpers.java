package dev.bti.chrona.androidsdk.common;

import android.content.Context;

import dev.bti.chrona.androidsdk.client.TokenProvider;
import dev.bti.chrona.androidsdk.helpers.AiIntentHelper;
import dev.bti.chrona.androidsdk.helpers.AuthHelper;
import dev.bti.chrona.androidsdk.helpers.CalendarHelper;
import dev.bti.chrona.androidsdk.helpers.EventHelper;
import dev.bti.chrona.androidsdk.helpers.SecurityHelper;
import dev.bti.chrona.androidsdk.helpers.SyncHelper;
import dev.bti.chrona.androidsdk.helpers.UserHelper;
import lombok.Getter;

@Getter
public class SDKHelpers {

    private final TokenProvider tokenProvider;
    private final String appVersion;

    private final AuthHelper authHelper;
    private final SecurityHelper securityHelper;
    private final CalendarHelper calendarHelper;
    private final EventHelper eventHelper;
    private final SyncHelper syncHelper;
    private final UserHelper userHelper;
    private final AiIntentHelper aiIntentHelper;

    private static SDKHelpers instance;

    private SDKHelpers(Context context, TokenProvider tokenProvider, String appVersion) {
        this.tokenProvider = tokenProvider;
        this.appVersion = appVersion;

        this.authHelper = new AuthHelper(context, tokenProvider, appVersion);
        this.securityHelper = new SecurityHelper(context, appVersion);
        this.calendarHelper = new CalendarHelper(context, tokenProvider, appVersion);
        this.eventHelper = new EventHelper(context, tokenProvider, appVersion);
        this.syncHelper = new SyncHelper(context, tokenProvider, appVersion);
        this.userHelper = new UserHelper(context, tokenProvider, appVersion);
        this.aiIntentHelper = new AiIntentHelper(context, tokenProvider, appVersion);
    }

    public static synchronized SDKHelpers GetInstance(Context context, TokenProvider tokenProvider, String appVersion) {
        if (instance == null) {
            instance = new SDKHelpers(context, tokenProvider, appVersion);
        }
        return instance;
    }
}