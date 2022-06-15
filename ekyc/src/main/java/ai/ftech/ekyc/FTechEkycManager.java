package ai.ftech.ekyc;

import android.content.Context;

public class FTechEkycManager {

    private static Context applicationContext;

    public static void init(Context context) {
        applicationContext = context;
    }

    public static Context getApplicationContext() {
        if (applicationContext != null) {
            return applicationContext;
        }
        throw new RuntimeException("applicationContext must not null");
    }
}
