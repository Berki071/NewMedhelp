package com.medhelp.medhelp;

public class Constants {
    String DB_NAME = "medhelp_mhchat.db";

    public static final String ANDROID_BOOT_COMPLETED ="android.intent.action.BOOT_COMPLETED";
    public static final String ANDROID_ACTION_BOOT_COMPLETED ="android.intent.action.ACTION_BOOT_COMPLETED";
    public static final String ANDROID_QUICKBOOT_POWERON ="android.intent.action.QUICKBOOT_POWERON";
    public static final String HTC_QUICKBOOT_POWERON ="com.htc.intent.action.QUICKBOOT_POWERON";
    public static final String ANDROID_PACKAGE_REPLACED ="android.intent.action.PACKAGE_REPLACED";
    public static final String ANDROID_PACKAGE_ADDED="android.intent.action.PACKAGE_ADDED";

    public final static String CLOSED_CONNECTION="CLOSED_CONNECTION";
    public final static String PING="PING";
    public final static String ANSWERED="ANSWERED";

    public final static int SERVER_PORT=8080;

    //public final static String IP ="192.168.0.104";  //паша
    public final static String IP ="192.168.0.110";   //мой

    //main menu
    public static final int MENU_THE_MAIN=0;
    public static final int MENU_FINANCES_SERVICES=1;
    public static final int MENU_STAFF=2;
    public static final int MENU_RECORD=3;
    public static final int MENU_PRICE=4;
    public static final int MENU_ANALISE_PRICE=5;

    public static final int MENU_ELECTRONIC_CONCLUSIONS=6;

    public static final int MENU_LIST_ANALISES=7;
    public static final int MENU_RESULT_ANALISES=8;
    public static final int MENU_TAX_CERTIFICATE =9;
    public static final int MENU_ONLINE_CONSULTATION=10;
    public static final int MENU_CHAT=11;
   // public static final int MENU_SALE=10;
    public static final int MENU_SETTINGS=12;
    public static final int MENU_CONTACTING_SUPPORT=13;
    public static final int MENU_RATE=14;
    public static final int MENU_LOGOUT=15;
    public static final int FRAGMENT_SCHEDULE=16;
    public static final int ACTIVITY_SERVICE=17;
   // public static final int MENU_NEWS=17;

    public static final String  TYPE_DOP_VIDEO_CALL="videocall";
    public static final int MIN_TIME_BEFORE_VIDEO_CALL=10;   //минут, до того как можно будет запустить видеочат от начала приема
    public static final String SCENARIO_NON="non";
    public static final String SCENARIO_VIDEO="video";
    public static final String SCENARIO_INCOMING_WAIT="wait";

    public static final int REQUEST_CODE_FOR_UPDATE_APP=156;

    //type message
    public static final String DATE="DATE";
    public static final String TEXT="text";
    public static final String TEXT_ALL="textall";
    public static final String IMAGE="image";
    public static final String YOUTUBE_LINK="youtube";

    public static final int NOTIFICATION_ID_SUPPORT =1111;

   //public static final String SERVER_KEY_FCM = "AAAAqRPcBpk:APA91bE27b3ntJIkdeb8XGKry5gYtDatQQ3nieB8xlZ7dHB0-1RJaXa1fHSBp_Vc2qjqPnnt4H4F5a8Mi48yoU4X4PMlRdm_fyOrM7aB0QWm3wl8l-47p8zTkZ2TbPNnPSDMtdlD4MjX";
    public static final String SENDER_ID_FCM = "384956299518";
}
