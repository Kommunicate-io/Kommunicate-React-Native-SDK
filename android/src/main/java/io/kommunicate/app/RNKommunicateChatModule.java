package io.kommunicate.app;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.os.AsyncTask;



import com.applozic.mobicomkit.api.account.register.RegistrationResponse;
import com.applozic.mobicomkit.api.account.user.AlUserUpdateTask;
import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.applozic.mobicomkit.channel.service.ChannelService;
import com.applozic.mobicomkit.listners.AlCallback;
import com.applozic.mobicomkit.uiwidgets.kommunicate.KmPrefSettings;
import com.applozic.mobicommons.json.GsonUtils;
import com.applozic.mobicommons.people.channel.Channel;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;
import com.applozic.mobicommons.file.FileUtils;
import com.applozic.mobicomkit.api.conversation.database.MessageDatabaseService;


import io.kommunicate.KmConversationHelper;
import io.kommunicate.KmException;
import io.kommunicate.KmSettings;
import io.kommunicate.Kommunicate;
import io.kommunicate.callbacks.KMLoginHandler;
import io.kommunicate.callbacks.KMLogoutHandler;
import io.kommunicate.callbacks.KmCallback;
import io.kommunicate.callbacks.KmPushNotificationHandler;
import io.kommunicate.users.KMUser;
import io.kommunicate.KmConversationBuilder;
import com.applozic.mobicomkit.uiwidgets.kommunicate.settings.KmSpeechToTextSetting;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import android.os.AsyncTask;
import io.kommunicate.callbacks.KmGetConversationInfoCallback;
import io.kommunicate.async.KmConversationInfoTask;



import com.applozic.mobicommons.people.channel.Channel;
import com.applozic.mobicommons.people.contact.Contact;
import com.applozic.mobicomkit.contact.AppContactService;
import com.applozic.mobicomkit.uiwidgets.conversation.fragment.MobiComConversationFragment;
import com.applozic.mobicomkit.api.conversation.AlTotalUnreadCountTask;
import io.kommunicate.preference.KmConversationInfoSetting;
import com.applozic.mobicomkit.broadcast.AlEventManager;
import com.applozic.mobicomkit.api.conversation.MessageBuilder;
import io.kommunicate.async.KmConversationInfoTask;
import io.kommunicate.callbacks.KmGetConversationInfoCallback;
import io.kommunicate.services.KmChannelService;



public class RNKommunicateChatModule extends ReactContextBaseJavaModule {

    private static final String SUCCESS = "Success";
    private static final String ERROR = "Error";
    private static final String CONVERSATION_ID = "conversationId";
    private static final String CLIENT_CONVERSATION_ID = "clientConversationId";
    private static final String CONVERSATION_ASSIGNEE = "conversationAssignee";
    private static final String TEAM_ID = "teamId";
    private static final String CONVERSATION_INFO = "conversationInfo";
    private static final String LANGUAGES = "languages";
    private static final String KM_USER = "kmUser";
    private KmEventListener kmEventListener;
    private ReactApplicationContext reactContext;

    public RNKommunicateChatModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNKommunicateChat";
    }
    @ReactMethod
    public void addListener(String eventName) {
            kmEventListener = new KmEventListener();
            kmEventListener.register(reactContext);
    }

    @ReactMethod
    public void removeListeners(Integer count) {
        kmEventListener.unregister();
    }

    @ReactMethod
    public void loginUser(final ReadableMap config, final Callback callback) {
        final Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            callback.invoke(ERROR, "Activity doesn't exist");
            return;
        }

        KMUser user = (KMUser) GsonUtils.getObjectFromJson(GsonUtils.getJsonFromObject(config.toHashMap(), HashMap.class), KMUser.class);

        if (user != null && !TextUtils.isEmpty(user.getApplicationId())) {
            Kommunicate.init(currentActivity, user.getApplicationId());
        }
        
        Kommunicate.login(currentActivity, user, new KMLoginHandler() {
            @Override
            public void onSuccess(RegistrationResponse registrationResponse, Context context) {
                callback.invoke(SUCCESS, GsonUtils.getJsonFromObject(registrationResponse, RegistrationResponse.class));
            }

            @Override
            public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
                callback.invoke(ERROR, registrationResponse != null ? GsonUtils.getJsonFromObject(registrationResponse, RegistrationResponse.class) : exception != null ? exception.getMessage() : null);
            }
        });
    }

    @ReactMethod
    public void loginAsVisitor(final String applicationId, final Callback callback) {
        final Activity activity = getCurrentActivity();
        if (activity == null) {
            callback.invoke(ERROR, "Activity doesn't exist");
            return;
        }

        if (applicationId != null && !TextUtils.isEmpty(applicationId)) {
            Kommunicate.init(activity, applicationId);
        }

        Kommunicate.loginAsVisitor(activity, new KMLoginHandler() {
            @Override
            public void onSuccess(RegistrationResponse registrationResponse, Context context) {
                callback.invoke(SUCCESS, GsonUtils.getJsonFromObject(registrationResponse, RegistrationResponse.class));
            }

            @Override
            public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
                callback.invoke(ERROR, registrationResponse != null ? GsonUtils.getJsonFromObject(registrationResponse, RegistrationResponse.class) : exception != null ? exception.getMessage() : null);
            }
        });
    }

    @ReactMethod
    public void registerPushNotification(final Callback callback) {
        final Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            callback.invoke(ERROR, "Activity doesn't exist");
            return;
        }

        Kommunicate.registerForPushNotification(currentActivity, new KmPushNotificationHandler() {
            @Override
            public void onSuccess(RegistrationResponse registrationResponse) {
                callback.invoke(SUCCESS, GsonUtils.getJsonFromObject(registrationResponse, RegistrationResponse.class));
            }

            @Override
            public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
                callback.invoke(ERROR, registrationResponse != null ? GsonUtils.getJsonFromObject(registrationResponse, RegistrationResponse.class) : exception != null ? exception.getMessage() : null);
            }
        });
    }

    @ReactMethod
    public void updateUserDetails(final ReadableMap config, final Callback callback) {
        final Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            callback.invoke(ERROR, "Activity doesn't exist");
            return;
        }

        if (KMUser.isLoggedIn(currentActivity)) {
            KMUser user = (KMUser) GsonUtils.getObjectFromJson(GsonUtils.getJsonFromObject(config.toHashMap(), HashMap.class), KMUser.class);
            new AlUserUpdateTask(currentActivity, user, new AlCallback() {
                @Override
                public void onSuccess(Object message) {
                    callback.invoke(SUCCESS, "User details updated");
                }

                @Override
                public void onError(Object error) {
                    callback.invoke(ERROR, "Unable to update user details");
                }
            }).execute();
        } else {
            callback.invoke(ERROR, "User not authorised. This usually happens when calling the function before conversationBuilder or loginUser. Make sure you call either of the two functions before updating the user details");
        }
    }

    @ReactMethod
    public void updatePushToken(String token, final Callback callback) {
        final Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            callback.invoke(ERROR, "Activity doesn't exist");
            return;
        }
        if (MobiComUserPreference.getInstance(currentActivity).isRegistered()) {
            try {
                Kommunicate.registerForPushNotification(currentActivity, token, new KmPushNotificationHandler() {
                    @Override
                    public void onSuccess(RegistrationResponse registrationResponse) {
                        callback.invoke(SUCCESS, GsonUtils.getJsonFromObject(registrationResponse, RegistrationResponse.class));
                    }

                    @Override
                    public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
                        callback.invoke(ERROR, registrationResponse != null ? GsonUtils.getJsonFromObject(registrationResponse, RegistrationResponse.class) : exception != null ? exception.getMessage() : null);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @ReactMethod
    public void openConversation(final Callback callback) {
        final Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            callback.invoke(ERROR, "Activity doesn't exist");
            return;
        }

        Kommunicate.openConversation(currentActivity, new KmCallback() {
            @Override
            public void onSuccess(Object message) {
                callback.invoke(SUCCESS, message.toString());
            }

            @Override
            public void onFailure(Object error) {
                callback.invoke(ERROR, error.toString());
            }
        });
    }

    @ReactMethod
    public void buildConversation(final ReadableMap jsonObject, final Callback callback) {
        final Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            callback.invoke(ERROR, "Activity doesn't exist");
            return;
        }

        try {
            KMUser user = null;
            Map<String, String> conversationInfo = null;
            Map<String, Object> dataMap = jsonObject.toHashMap();

            if (jsonObject.hasKey(KM_USER)) {
                user = (KMUser) GsonUtils.getObjectFromJson(jsonObject.getString(KM_USER), KMUser.class);
                dataMap.remove(KM_USER);
            }

            if (jsonObject.hasKey(CONVERSATION_INFO)) {
                conversationInfo = (Map<String, String>) GsonUtils.getObjectFromJson(jsonObject.getString(CONVERSATION_INFO), Map.class);
                dataMap.remove(CONVERSATION_INFO);
            }
            KmConversationBuilder conversationBuilder = (KmConversationBuilder) GsonUtils.getObjectFromJson(GsonUtils.getJsonFromObject(dataMap, HashMap.class), KmConversationBuilder.class);
            conversationBuilder.setContext(currentActivity);

            if (!jsonObject.hasKey("isSingleConversation")) {
                conversationBuilder.setSingleConversation(true);
            }
            if (!jsonObject.hasKey("skipConversationList")) {
                conversationBuilder.setSkipConversationList(true);
            }
            if (user != null) {
                conversationBuilder.setKmUser(user);
            }
            if (conversationInfo != null) {
                conversationBuilder.setConversationInfo(conversationInfo);
            }

            if (jsonObject.hasKey("createOnly") && jsonObject.getBoolean("createOnly")) {
                conversationBuilder.createConversation(new KmCallback() {
                    @Override
                    public void onSuccess(Object message) {
                        Channel channel = ChannelService.getInstance(currentActivity).getChannelByChannelKey((Integer) message);
                        callback.invoke(SUCCESS, channel != null && !TextUtils.isEmpty(channel.getClientGroupId()) ? channel.getClientGroupId() : (String) message);
                    }

                    @Override
                    public void onFailure(Object error) {
                        callback.invoke(ERROR, error != null ? error.toString() : "Unknown error occurred");
                    }
                });
            } else {
                conversationBuilder.launchConversation(new KmCallback() {
                    @Override
                    public void onSuccess(Object message) {
                        callback.invoke(SUCCESS, message != null ? ChannelService.getInstance(currentActivity).getChannelByChannelKey((Integer) message).getClientGroupId()  : "Success");
                    }

                    @Override
                    public void onFailure(Object error) {
                        callback.invoke(ERROR, error != null ? error.toString() : "Unknown error occurred");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            callback.invoke(ERROR, e.getMessage());
        }
    }
    

    @ReactMethod
    public void openParticularConversation(final String conversationId, final boolean skipConversationList, final Callback callback) {
        final Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            callback.invoke(ERROR, "Activity does not exist.");
            return;
        }
        if (TextUtils.isEmpty(conversationId)) {
            callback.invoke(ERROR, "Invalid or empty clientConversationId.");
            return;
        }

        new KmConversationInfoTask(currentActivity, conversationId, new KmGetConversationInfoCallback() {
            @Override
            public void onSuccess(Channel channel, Context context) {
                if (channel != null) {
                    try {
                        KmConversationHelper.openConversation(context, true, channel.getKey(), new KmCallback() {
                            @Override
                            public void onSuccess(Object message) {
                                callback.invoke(SUCCESS,message.toString());
                            }

                            @Override
                            public void onFailure(Object error) {
                                callback.invoke(ERROR, error.toString());
                            }
                        });
                    } catch (KmException k) {
                        callback.invoke(ERROR, k.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Exception e, Context context) {
                new KmConversationInfoTask(context, Integer.valueOf(conversationId), new KmGetConversationInfoCallback() {
                    @Override
                    public void onSuccess(Channel channel, Context context) {
                        if (channel != null) {

                                Kommunicate.openConversation(context, channel.getKey(), new KmCallback() {
                                    @Override
                                    public void onSuccess(Object message) {
                                        callback.invoke(SUCCESS,message.toString());
                                    }

                                    @Override
                                    public void onFailure(Object error) {
                                        callback.invoke(ERROR, error.toString());
                                    }
                                });

                        }
                    }

                    @Override
                    public void onFailure(Exception e, Context context) {
                        callback.invoke(ERROR, e != null ? e.getMessage() : "Invalid conversationId");
                    }
                }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    } 


    

    @ReactMethod
    public void isLoggedIn(final Callback callback) {
        final Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            callback.invoke("False");
        } else {
            if (Kommunicate.isLoggedIn(currentActivity))
                callback.invoke("True");
            else
                callback.invoke("False");

        }
    }

    @ReactMethod
    public void updateChatContext(ReadableMap chatContext, Callback callback) {
        final Activity currentActivity = getCurrentActivity();
        try {
            if (Kommunicate.isLoggedIn(currentActivity)) {
                Kommunicate.updateChatContext(currentActivity, getStringMap(chatContext.toHashMap()));
                callback.invoke(SUCCESS, "Updated chat context");
            } else {
                callback.invoke(ERROR, "User not authorised. This usually happens when calling the function before conversationBuilder or loginUser. Make sure you call either of the two functions before updating the chatContext");
            }
        } catch (Exception e) {
            callback.invoke(ERROR, e.toString());
        }
    }

    private Map<String, String> getStringMap(HashMap<String, Object> objectMap) {
        if (objectMap == null) {
            return null;
        }
        Map<String, String> newMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
            newMap.put(entry.getKey(), entry.getValue() instanceof String ? (String) entry.getValue() : entry.getValue().toString());
        }
        return newMap;
    }

    @ReactMethod
    public void logout(final Callback callback) {
        final Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            callback.invoke(ERROR);
            return;
        }
        Kommunicate.logout(currentActivity, new KMLogoutHandler() {
            @Override
            public void onSuccess(Context context) {
                callback.invoke(SUCCESS);
            }

            @Override
            public void onFailure(Exception exception) {
                callback.invoke(ERROR);
            }
        });
    }

    @ReactMethod
    public void updateConversationAssignee(ReadableMap assigneeObject, final Callback callback) {
        final Activity currentActivity = getCurrentActivity();
        try {
            if (Kommunicate.isLoggedIn(currentActivity)) {
                KmInfoProcessor infoProcessor = new KmInfoProcessor(assigneeObject, callback);
                KmSettings.updateConversationAssignee(currentActivity, infoProcessor.getConversationId(), infoProcessor.getClientConversationId(), infoProcessor.getConversationAssignee(), new KmCallback() {
                    @Override
                    public void onSuccess(Object o) {
                        callback.invoke(SUCCESS, o);
                    }

                    @Override
                    public void onFailure(Object o) {
                        callback.invoke(ERROR, o);
                    }
                });
            } else {
                callback.invoke(ERROR, "User not authorised. This usually happens when calling the function before conversationBuilder or loginUser.");
            }
        } catch (Exception e) {
            callback.invoke(ERROR, e.toString());
        }
    }

    @ReactMethod
    public void updateTeamId(final ReadableMap teamObject, final Callback callback) {
        final Activity currentActivity = getCurrentActivity();
        try {
            if (Kommunicate.isLoggedIn(currentActivity)) {
                final KmInfoProcessor infoProcessor = new KmInfoProcessor(teamObject, callback);
                KmSettings.updateTeamId(currentActivity, infoProcessor.getConversationId(), infoProcessor.getClientConversationId(), infoProcessor.getTeamId(), new KmCallback() {
                    @Override
                    public void onSuccess(Object o) {
                        if (!(o instanceof Channel)) {
                            callback.invoke(SUCCESS, o);
                        }
                    }

                    @Override
                    public void onFailure(Object o) {
                        callback.invoke(ERROR, o);
                    }
                });
            } else {
                callback.invoke(ERROR, "User not authorised. This usually happens when calling the function before conversationBuilder or loginUser.");
            }
        } catch (Exception e) {
            callback.invoke(ERROR, e.toString());
        }
    }

    @ReactMethod
    public void updateConversationInfo(ReadableMap conversationInfoObject, final Callback callback) {
        final Activity currentActivity = getCurrentActivity();
        try {
            if (Kommunicate.isLoggedIn(currentActivity)) {
                final KmInfoProcessor infoProcessor = new KmInfoProcessor(conversationInfoObject, callback);
                if (infoProcessor.getConversationInfo() == null) {
                    callback.invoke(ERROR, "conversationInfo cannot be null");
                    return;
                }
                KmSettings.updateConversationInfo(currentActivity, infoProcessor.getConversationId(), infoProcessor.getClientConversationId(), getStringMap(infoProcessor.getConversationInfo().toHashMap()), new KmCallback() {
                    @Override
                    public void onSuccess(Object o) {
                        if (!(o instanceof Channel)) {
                            callback.invoke(SUCCESS, o);
                        }
                    }

                    @Override
                    public void onFailure(Object o) {
                        callback.invoke(ERROR, o);
                    }
                });
            } else {
                callback.invoke(ERROR, "User not authorised. This usually happens when calling the function before conversationBuilder or loginUser.");
            }
        } catch (Exception e) {
            callback.invoke(ERROR, e.toString());
        }
    }

    @ReactMethod
    public void closeConversationScreen() {
        final Activity currentActivity = getCurrentActivity();
        if(currentActivity != null) {
            Kommunicate.closeConversationScreen(currentActivity);
        }
    }

    @ReactMethod
    public void createSettings(final String setting) {
        final Activity currentActivity = getCurrentActivity();
        FileUtils.writeSettingsToFile(currentActivity, setting);
    }

    @ReactMethod
    public void updateDefaultSetting(final ReadableMap settingMap, final Callback callback) {
        final Activity currentActivity = getCurrentActivity();
            try {
                KmSettings.clearDefaultSettings();
                if (settingMap.hasKey("defaultAgentIds")) {
                    List<String> agentList = new ArrayList<String>();
                    for(int i = 0; i < settingMap.getArray("defaultAgentIds").size(); i++){
                        agentList.add(settingMap.getArray("defaultAgentIds").getString(i));
                    }
                    KmSettings.setDefaultAgentIds(agentList);
                }
                if (settingMap.hasKey("defaultBotIds")) {
                    List<String> botList = new ArrayList<String>();
                    for(int i = 0; i < settingMap.getArray("defaultBotIds").size(); i++){
                        botList.add(settingMap.getArray("defaultBotIds").getString(i));
                    }
                    KmSettings.setDefaultBotIds(botList);
                }
                if (settingMap.hasKey("defaultAssignee")) {
                    KmSettings.setDefaultAssignee(settingMap.getString("defaultAssignee"));
                }
                if (settingMap.hasKey("teamId")) {
                    KmSettings.setDefaultTeamId(settingMap.getString("teamId"));
                }
                if (settingMap.hasKey("skipRouting")) {
                    KmSettings.setSkipRouting(settingMap.getBoolean("skipRouting"));
                }
                    callback.invoke(SUCCESS, "Successfully set default settings");
            } catch(Exception e) {
                callback.invoke(ERROR, e.toString());
            }
    }

    @ReactMethod
    public void fetchUnreadCount(final Callback callback) {
        final Activity currentActivity = getCurrentActivity();
        try {
            callback.invoke(SUCCESS, String.valueOf(new MessageDatabaseService(currentActivity).getTotalUnreadCount()));
        } catch (Exception e) {
            callback.invoke(ERROR, e.toString());
        }
    }

    @ReactMethod
    public void fetchConversationInformation(final ReadableMap data,final Callback callback) {
        final Activity currentActivity = getCurrentActivity();
        if (data.hasKey("conversationID") && !TextUtils.isEmpty(data.getString("conversationID"))) {
            String conversationID = data.getString("conversationID") ;
            new KmConversationInfoTask(currentActivity, Integer.valueOf(conversationID), new KmGetConversationInfoCallback() {
                @Override
                public void onSuccess(Channel channel, Context context) {
                    if (channel != null) {
                        callback.invoke(SUCCESS, GsonUtils.getJsonFromObject(channel, Channel.class));
                    } else {
                        callback.invoke(ERROR,"Conversation Not Found");
                    }
                }
                @Override
                public void onFailure(Exception e, Context context) {
                    callback.invoke(ERROR,e != null ? e.getMessage() : "Invalid ConversationID");
                }  
            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } else if (data.hasKey("clientConversationID") && !TextUtils.isEmpty(data.getString("clientConversationID"))) {
            String clientConversationID = data.getString("clientConversationID");
            new KmConversationInfoTask(currentActivity, clientConversationID, new KmGetConversationInfoCallback() {
                @Override
                public void onSuccess(Channel channel, Context context) {
                    if (channel != null) {
                        callback.invoke(SUCCESS, GsonUtils.getJsonFromObject(channel, Channel.class));
                    } else {
                        callback.invoke(ERROR, "Conversation Not Found", null);
                    }
                }
                @Override
                public void onFailure(Exception e, Context context) {
                    callback.invoke(ERROR, e != null ? e.getMessage() : "Invalid clientChannelID");
                }  
            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            callback.invoke(ERROR, "Object doesn't contain 'clientChannelID' or 'conversationID'");
        }
    }

    @ReactMethod
    public void fetchConversationAssigneeInfo(final ReadableMap data,final Callback callback) {
        final Activity currentActivity = getCurrentActivity();
        if (data.hasKey("conversationID") && !TextUtils.isEmpty(data.getString("conversationID"))) {
            String conversationID = data.getString("conversationID") ;
            new KmConversationInfoTask(currentActivity, Integer.valueOf(conversationID), new KmGetConversationInfoCallback() {
                @Override
                public void onSuccess(Channel channel, Context context) {
                    if (channel != null) {
                        Contact assignee = new AppContactService(currentActivity).getContactById(channel.getConversationAssignee());
                        callback.invoke(SUCCESS, GsonUtils.getJsonFromObject(assignee, Contact.class));
                    } else {
                        callback.invoke(ERROR,"Conversation Not Found");
                    }
                }
                @Override
                public void onFailure(Exception e, Context context) {
                    callback.invoke(ERROR,e != null ? e.getMessage() : "Invalid ConversationID");
                }  
            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } else if (data.hasKey("clientConversationID") && !TextUtils.isEmpty(data.getString("clientConversationID"))) {
            String clientConversationID = data.getString("clientConversationID");
            new KmConversationInfoTask(currentActivity, clientConversationID, new KmGetConversationInfoCallback() {
                @Override
                public void onSuccess(Channel channel, Context context) {
                    if (channel != null) {
                        Contact assignee = new AppContactService(currentActivity).getContactById(channel.getConversationAssignee());
                        callback.invoke(SUCCESS, GsonUtils.getJsonFromObject(assignee, Contact.class));
                    } else {
                        callback.invoke(ERROR, "Conversation Not Found", null);
                    }
                }
                @Override
                public void onFailure(Exception e, Context context) {
                    callback.invoke(ERROR, e != null ? e.getMessage() : "Invalid clientChannelID");
                }  
            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            callback.invoke(ERROR, "Object doesn't contain 'clientChannelID' or 'conversationID'");
        }
    }

    static class KmInfoProcessor {
        private String clientConversationId;
        private Integer conversationId;
        private ReadableMap conversationInfo;
        private String teamId;
        private String conversationAssignee;

        public KmInfoProcessor(ReadableMap map, Callback callback) {
            if (map.hasKey(CLIENT_CONVERSATION_ID)) {
                clientConversationId = map.getString(CLIENT_CONVERSATION_ID);
            }
            if (map.hasKey(CONVERSATION_ID)) {
                conversationId = map.getInt(CONVERSATION_ID);
            }
            if (clientConversationId == null && conversationId == null) {
                callback.invoke(ERROR, "Either conversationId or clientConversationId is required");
                return;
            }
            if (map.hasKey(CONVERSATION_ASSIGNEE)) {
                conversationAssignee = map.getString(CONVERSATION_ASSIGNEE);
            }
            if (map.hasKey(TEAM_ID)) {
                teamId = map.getString(TEAM_ID);
            }
            if (map.hasKey(CONVERSATION_INFO)) {
                conversationInfo = map.getMap(CONVERSATION_INFO);
            }
        }

        public String getClientConversationId() {
            return clientConversationId;
        }

        public Integer getConversationId() {
            return conversationId;
        }

        public ReadableMap getConversationInfo() {
            return conversationInfo;
        }

        public String getTeamId() {
            return teamId;
        }

        public String getConversationAssignee() {
            return conversationAssignee;
        }
    }
}