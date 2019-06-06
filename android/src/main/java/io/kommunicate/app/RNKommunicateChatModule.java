
package io.kommunicate.app;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.applozic.mobicomkit.ApplozicClient;
import com.applozic.mobicomkit.api.account.register.RegistrationResponse;
import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.applozic.mobicomkit.channel.service.ChannelService;
import com.applozic.mobicommons.ApplozicService;
import com.applozic.mobicommons.json.GsonUtils;
import com.applozic.mobicommons.people.channel.Channel;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;

import org.json.JSONObject;

import java.util.HashMap;

import io.kommunicate.KmChatBuilder;
import io.kommunicate.Kommunicate;
import io.kommunicate.callbacks.KMLoginHandler;
import io.kommunicate.callbacks.KmCallback;
import io.kommunicate.callbacks.KmPushNotificationHandler;
import io.kommunicate.users.KMUser;

public class RNKommunicateChatModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public RNKommunicateChatModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNKommunicateChat";
    }

    @ReactMethod
    public void loginUser(final ReadableMap config, final Callback callback) {
        final Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            callback.invoke("Activity doesn't exist", null);
            return;
        }

        KMUser user = (KMUser) GsonUtils.getObjectFromJson(GsonUtils.getJsonFromObject(config.toHashMap(), HashMap.class), KMUser.class);

        if (user != null && !TextUtils.isEmpty(user.getApplicationId())) {
            Kommunicate.init(currentActivity, user.getApplicationId());
        }

        Kommunicate.login(currentActivity, user, new KMLoginHandler() {
            @Override
            public void onSuccess(RegistrationResponse registrationResponse, Context context) {
                callback.invoke("Success", GsonUtils.getJsonFromObject(registrationResponse, RegistrationResponse.class));
            }

            @Override
            public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
                callback.invoke("Error", registrationResponse != null ? GsonUtils.getJsonFromObject(registrationResponse, RegistrationResponse.class) : exception != null ? exception.getMessage() : null);
            }
        });
    }

    public void registerPushNotification(final Callback callback) {
        final Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            callback.invoke("Activity doesn't exist", null);
            return;
        }

        Kommunicate.registerForPushNotification(currentActivity, new KmPushNotificationHandler() {
            @Override
            public void onSuccess(RegistrationResponse registrationResponse) {
                callback.invoke("Success", GsonUtils.getJsonFromObject(registrationResponse, RegistrationResponse.class));
            }

            @Override
            public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
                callback.invoke("Error", registrationResponse != null ? GsonUtils.getJsonFromObject(registrationResponse, RegistrationResponse.class) : exception != null ? exception.getMessage() : null);
            }
        });
    }

    public void updatePushToken(String token, final Callback callback) {
        final Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            callback.invoke("Activity doesn't exist", null);
            return;
        }
        if (MobiComUserPreference.getInstance(currentActivity).isRegistered()) {
            try {
                Kommunicate.registerForPushNotification(currentActivity, token, new KmPushNotificationHandler() {
                    @Override
                    public void onSuccess(RegistrationResponse registrationResponse) {
                        callback.invoke("Success", GsonUtils.getJsonFromObject(registrationResponse, RegistrationResponse.class));
                    }

                    @Override
                    public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
                        callback.invoke("Error", registrationResponse != null ? GsonUtils.getJsonFromObject(registrationResponse, RegistrationResponse.class) : exception != null ? exception.getMessage() : null);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void buildConversation(final ReadableMap readableMap, final Callback callback) {
        final Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            callback.invoke("Error", "Activity doesn't exist");
            return;
        }

        try {
            KmChatBuilder chatBuilder = (KmChatBuilder) GsonUtils.getObjectFromJson(GsonUtils.getJsonFromObject(readableMap.toHashMap(), HashMap.class), KmChatBuilder.class);
            /*final JSONObject jsonObject = new JSONObject(data.getString(0));

            if (jsonObject.has("appId")) {
                chatBuilder.setApplicationId(jsonObject.getString("appId"));
            }

            if (jsonObject.has("withPreChat")) {
                chatBuilder.setWithPreChat(jsonObject.getBoolean("withPreChat"));
            }

            if (jsonObject.has("kmUser")) {
                chatBuilder.setKmUser((KMUser) GsonUtils.getObjectFromJson(jsonObject.getString("kmUser"), KMUser.class));
            }

            if (jsonObject.has("isUnique")) {
                chatBuilder.setSingleChat(jsonObject.getBoolean("isUnique"));
            }

            if (jsonObject.has("agentIds")) {
                chatBuilder.setAgentIds((List<String>) GsonUtils.getObjectFromJson(jsonObject.getString("agentIds"), List.class));
            }

            if (jsonObject.has("botIds")) {
                chatBuilder.setBotIds((List<String>) GsonUtils.getObjectFromJson(jsonObject.getString("botIds"), List.class));
            }

            if (jsonObject.has("groupName")) {
                chatBuilder.setChatName(jsonObject.getString("groupName"));
            }

            if (jsonObject.has("deviceToken")) {
                chatBuilder.setDeviceToken(jsonObject.getString("deviceToken"));
            }

            if (jsonObject.has("metadata")) {
                chatBuilder.setMetadata((Map<String, String>) GsonUtils.getObjectFromJson(jsonObject.getString("metadata"), Map.class));
            }*/

            if (jsonObject.has("createOnly") && jsonObject.getBoolean("createOnly")) {
                chatBuilder.createChat(new KmCallback() {
                    @Override
                    public void onSuccess(Object message) {
                        Channel channel = ChannelService.getInstance(currentActivity).getChannelByChannelKey((Integer) message);
                        callback.invoke("Success", channel != null && !TextUtils.isEmpty(channel.getClientGroupId()) ? channel.getClientGroupId() : (String) message);
                    }

                    @Override
                    public void onFailure(Object error) {
                        callback.invoke("Error", error != null ? error.toString() : "Unknown error occurred");
                    }
                });
            } else {
                chatBuilder.launchChat(new KmCallback() {
                    @Override
                    public void onSuccess(Object message) {
                        callback.invoke("Success", message != null ? message.toString() : "Success");
                    }

                    @Override
                    public void onFailure(Object error) {
                        callback.invoke("Error", error != null ? error.toString() : "Unknown error occurred");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            callback.invoke("Error", e.getMessage());
        }
    }
}