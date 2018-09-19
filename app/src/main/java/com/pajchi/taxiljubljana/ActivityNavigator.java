package com.pajchi.taxiljubljana;

import android.content.Context;
import android.content.Intent;

class ActivityNavigator {

    public static void goToPerformCallActivity(Context context) {
        Intent intent = new Intent(context, PerformCallActivity.class);
        context.startActivity(intent);
    }

    public static void goToChooseTaxiCompanyActivity(Context context) {
        Intent intent = new Intent(context, ChooseTaxiCompanyActivity.class);
        context.startActivity(intent);
    }

    public static void goToEnterHomeAddressActivity(Context context) {
        Intent intent = new Intent(context, EnterHomeAddressActivity.class);
        context.startActivity(intent);
    }

    public static void goToNewDestinationActivity(Context context) {
        Intent intent = new Intent(context, NewDestinationActivity.class);
        context.startActivity(intent);
    }
}
