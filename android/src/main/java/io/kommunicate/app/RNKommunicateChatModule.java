
package io.kommunicate.app;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.applozic.mobicomkit.api.account.register.RegistrationResponse;
import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.applozic.mobicomkit.channel.service.ChannelService;
import com.applozic.mobicommons.json.GsonUtils;
import com.applozic.mobicommons.people.channel.Channel;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //creates the native android toast (for testing)
    @ReactMethod
    public void createToast(String message) {
        Toast.makeText(getReactApplicationContext(), message, Toast.LENGTH_LONG).show();
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

    @ReactMethod
    public void loginAsVisitor(final String applicationId, final Callback callback) {
        final Activity activity = getCurrentActivity();
        if(activity == null) {
            callback.invoke("Error", "Activity not present.");
            return;
        }

        if(applicationId != null && !TextUtils.isEmpty(applicationId)) {
            Kommunicate.init(activity, applicationId);
        }

        Kommunicate.loginAsVisitor(activity, new KMLoginHandler() {
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

    @ReactMethod
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

    @ReactMethod
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

    @ReactMethod
    public  void openConversation(Callback callback) {
        final Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            callback.invoke("Error", "Activity doesn't exist");
            return;
        }

        Kommunicate.openConversation(currentActivity, new KmCallback() {
            @Override
            public void onSuccess(Object message) {
                callback.invoke("Success", message.toString());
            }

            @Override
            public void onFailure(Object error) {
                callback.invoke("Error", error.toString());
            }
        });
    }

    @ReactMethod
    public void buildConversation(final ReadableMap jsonObject, final Callback callback) {
        final Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            callback.invoke("Error", "Activity doesn't exist");
            return;
        }

        try {
            KmChatBuilder chatBuilder = new KmChatBuilder(currentActivity);
            //chatBuilder = (KmChatBuilder) GsonUtils.getObjectFromJson(GsonUtils.getJsonFromObject(readableMap.toHashMap(), HashMap.class), KmChatBuilder.class);

            if (jsonObject.hasKey("applicationId")) {
                chatBuilder.setApplicationId(jsonObject.getString("applicationId"));
            }

            if (jsonObject.hasKey("withPreChat")) {
                chatBuilder.setWithPreChat(jsonObject.getBoolean("withPreChat"));
            }

            if (jsonObject.hasKey("kmUser")) {
                chatBuilder.setKmUser((KMUser) GsonUtils.getObjectFromJson(jsonObject.getString("kmUser"), KMUser.class));
            }

            if (jsonObject.hasKey("isUnique")) {
                chatBuilder.setSingleChat(jsonObject.getBoolean("isUnique"));
            }

            if (jsonObject.hasKey("agentIds")) {
                chatBuilder.setAgentIds((List<String>) GsonUtils.getObjectFromJson(jsonObject.getString("agentIds"), List.class));
            }

            if (jsonObject.hasKey("botIds")) {
                chatBuilder.setBotIds((List<String>) GsonUtils.getObjectFromJson(jsonObject.getString("botIds"), List.class));
            }

            if (jsonObject.hasKey("groupName")) {
                chatBuilder.setChatName(jsonObject.getString("groupName"));
            }

            if (jsonObject.hasKey("deviceToken")) {
                chatBuilder.setDeviceToken(jsonObject.getString("deviceToken"));
            }

            if (jsonObject.hasKey("metadata")) {
                chatBuilder.setMetadata((Map<String, String>) GsonUtils.getObjectFromJson(jsonObject.getString("metadata"), Map.class));
            }

            if (jsonObject.hasKey("clientConversationId")) {
                chatBuilder.setClientConversationId(jsonObject.getString("clientConversationId"));
            }

            if (jsonObject.hasKey("createOnly") && jsonObject.getBoolean("createOnly")) {
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

    @ReactMethod
    public void launchPaticularConversation(final String conversationId, final boolean skipConversationList,final Callback callback) {
        final Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            callback.invoke("Error", "Activity does not exist.");
            return;
        }
        try {
            KmConversationHelper.openConversation(currentActivity,skipConversationList,  Integer.parseInt(conversationId), new KmCallback() {
                @Override
                public void onSuccess(Object message) {
                    callback.invoke("Success", message.toString());
                }

                @Override
                public void onFailure(Object error) {
                    callback.invoke("Error", error.toString());
                }
            });
        }catch (KmException k) {
            callback.invoke("Error", k.toString());
        }

    }

    @ReactMethod
    public void isLoggedIn(final Callback callback) {
        final Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            callback.invoke("False");
        } else {
            if(Kommunicate.isLoggedIn(currentActivity))
                callback.invoke("True");
            else
                callback.invoke("False");

        }
    }

    @ReactMethod
    public void getLoggedInUser(final Callback callback) {
        final Activity activity = getCurrentActivity();
        if(activity == null) {
            callback.invoke("Error", "Activity does not exist.");
            return;
        }

        KMUser kmUser = KMUser.getLoggedInUser(activity);

        callback.invoke("Success", GsonUtils.getJsonFromObject(kmUser, KMUser.class));
    }

    @ReactMethod
    public void logout(final Callback callback) {
        final Activity currentActivity = getCurrentActivity();
        if(currentActivity == null) {
            callback.invoke("Error");
            return;
        }
        Kommunicate.logout(currentActivity, new KMLogoutHandler() {
            @Override
            public void onSuccess(Context context) {
                callback.invoke("Success");
            }

            @Override
            public void onFailure(Exception exception) {
                callback.invoke("Error");
            }
        });
    }
}