package com.kotlin.mvp.component;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

/**
 * Created by flyjiang on 2019/8/2.
 */

public class InitializeService extends IntentService {

    private static final String ACTION_INIT = "initApplication";

    public InitializeService() {
        super("InitializeService");
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, InitializeService.class);
        intent.setAction(ACTION_INIT);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INIT.equals(action)) {
              //  initApplication();
            }
        }
    }
}
