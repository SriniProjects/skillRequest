package com.optimustechproject.project2.service;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.optimustechproject.project2.Activity.NotificationActivity;
import com.optimustechproject.project2.Models.NotificationsPOJO;
import com.optimustechproject.project2.app.Config;
import com.optimustechproject.project2.app.DbHandler;
import com.optimustechproject.project2.app.NotificationUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;
    Gson gson = new Gson();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, String.valueOf(remoteMessage));
        if (remoteMessage == null)
            return;
        if(!DbHandler.getBoolean(getApplicationContext(),"isLoggedIn",false))
            return;
        if (remoteMessage.getNotification() != null) {
            handleNotification(remoteMessage.getNotification().getBody());
        }
        if (remoteMessage.getData().size() > 0) {

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                Log.e("hello",String.valueOf(json));
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
        }
    }


    private void handleDataMessage(JSONObject json) {

        try {
            JSONObject data = json.getJSONObject("data");
            Log.e("dat12",String.valueOf(data));
            NotificationsPOJO notification = new NotificationsPOJO();
            notification.setTitle(data.getString("title"));
            notification.setMessage(data.getString("message"));
            notification.setBackground(data.getBoolean("is_background"));
            notification.setImageUrl(data.getString("image"));
            notification.setTimeStamp(DateFormat.getDateTimeInstance().format(new Date()));
            notification.setPayload(data.getJSONObject("payload"));

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", notification.getMessage());
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            }
            Intent resultIntent = new Intent(getApplicationContext(), NotificationActivity.class);
            resultIntent.putExtra("message", notification.getMessage());
            if (TextUtils.isEmpty(notification.getImageUrl())) {
                showNotificationMessage(getApplicationContext(), notification.getTitle(), notification.getMessage(), notification.getTimeStamp(), resultIntent);
            } else {
                showNotificationMessageWithBigImage(getApplicationContext(), notification.getTitle(), notification.getMessage(), notification.getMessage(), resultIntent, notification.getImageUrl());
            }
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
            DbHandler.putInt(getApplicationContext(),"count",1);
            List<NotificationsPOJO> notificationList = new ArrayList<>();
            String notificationData = DbHandler.getString(getApplicationContext(),"notificationList","");
            if(!notificationData.equals(""))
            {
                notificationList = gson.fromJson(notificationData, new TypeToken<List<NotificationsPOJO>>(){}.getType());
                notificationList.add(notification);
                DbHandler.putString(getApplicationContext(), "notificationList", gson.toJson(notificationList));
            }
            else
            {
                notificationList.add(notification);
                DbHandler.putString(getApplicationContext(), "notificationList", gson.toJson(notificationList));
            }

        }
           catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}