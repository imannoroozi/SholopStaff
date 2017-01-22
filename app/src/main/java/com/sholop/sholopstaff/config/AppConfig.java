package com.sholop.sholopstaff.config;

/**
 * Created by Iman on 10/22/2015.
 */
public class AppConfig {


    //Server IP
    public static String SERVER_IP = "http://sholop.com/service/";
    public static String URL_READ_APPOINTMENTS = SERVER_IP + "app_service.php";
    public static String URL_READ_NOTIFICATIONS = SERVER_IP + "app_service.php";
    public static String URL_LOGIN = SERVER_IP + "app_service.php";
    public static final String URL_UPDATE_PROFILE = SERVER_IP + "profileUpdate.php";

    // File upload url (replace the ip with your server address)
    public static final String FILE_UPLOAD_URL = SERVER_IP + "profileImageUpload.php";

    // Directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";

    // Intent extras
    public static final String EXTRA_KEY_USER_ID = "CURRENT_USER_ID";
    public static final String EXTRA_KEY_AUTHOR_OBJECT = "AUTHOR_OBJECT";
    public static final String EXTRA_KEY_APPOINTMENT_ID = "POST_D";
    public static final String EXTRA_KEY_USER_NAME =  "USER_NAME";
    public static final String EXTRA_KEY_USER_PASSWORD_OLD = "OLD_PASSWORD";
    public static final String EXTRA_KEY_USER_PASSWORD = "PASSWORD";
    public static final String EXTRA_KEY_IMAGE_OBJECT = "IMAGE_OBJECT";
    public static final String EXTRA_KEY_POST_OBJECT = "PROJECT_OBJECT";
    public static final String EXTRA_KEY_NOTIFICATION_ID = "EXTRA_KEY_NOTIFICATION_ID";


    public static final String EXTRA_KEY_RECEIVER_INTENT = "EXTRA_KEY_RECEIVER_INTENT";
    public static final String EXTRA_KEY_MESSAGE = "EXTRA_KEY_MESSAGE";

    public enum NOTIFICATION_TYPES {ARRIVAL, APPOINTMENT, YOU_GOT_APPOINTMENTS, CANCELLED, EDITED, VISIT_WEB_PAGE}

    //GSM Configurations

    // flag to identify whether to show single line
    // or multi line test push notification tray
    public static boolean appendNotificationMessages = true;

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // type of push messages
    public static final int PUSH_TYPE_CHATROOM = 1;
    public static final int PUSH_TYPE_USER = 2;

    // id to handle the notification in the notification try
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final int SPLASH_DISPLAY_LENGTH = 1000;
}
