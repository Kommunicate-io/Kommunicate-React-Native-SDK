package io.kommunicate.app;

import com.applozic.mobicomkit.broadcast.AlEventManager;
import io.kommunicate.callbacks.KmPluginEventListener;
import com.applozic.mobicomkit.api.conversation.Message;
import com.facebook.react.bridge.ReactApplicationContext;
import com.applozic.mobicommons.json.GsonUtils;
import org.json.JSONObject;
import org.json.JSONException;
import androidx.annotation.Nullable;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;
import com.google.gson.Gson;




public class KmEventListener implements KmPluginEventListener {
    private ReactApplicationContext reactContext;

   public void register(ReactApplicationContext reactContext) {
        this.reactContext = reactContext;
        AlEventManager.getInstance().registerPluginEventListener(this);
    }

    public void unregister() {
        AlEventManager.getInstance().unregisterPluginEventListener();
    }

    private void sendEvent(String eventName, String value) {
        WritableMap params = Arguments.createMap();
        params.putString("data", value);
        reactContext.
                getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    @Override
    public void onPluginLaunch() {
       sendEvent("onPluginLaunch", "launch");
    }

    @Override
    public void onPluginDismiss() {
        sendEvent("onPluginDismiss", "dismiss");
    }

    @Override
    public void onConversationResolved(Integer conversationId) {
        sendEvent("onConversationResolved", String.valueOf(conversationId));
    }

    @Override
    public void onConversationRestarted(Integer conversationId) {
        sendEvent("onConversationRestarted", String.valueOf(conversationId));
    }

    @Override
    public void onRichMessageButtonClick(Integer conversationId, String actionType, Object action) {
        try  {
            JSONObject messageActionObject = new JSONObject();
            messageActionObject.put("conversationId", conversationId);
            messageActionObject.put("actionType", actionType);
            messageActionObject.put("action", new Gson().toJson(action));
            sendEvent("onRichMessageButtonClick", String.valueOf(messageActionObject));
        } catch(JSONException e) {
            sendEvent("onRichMessageButtonClick",  "error fetching data");
            e.printStackTrace();
        }
    }

    @Override
    public void onStartNewConversation(Integer conversationId) {
        sendEvent("onStartNewConversation", String.valueOf(conversationId));
    }

    @Override
    public void onSubmitRatingClick(Integer conversationId, Integer rating, String feedback) {
        try {
            JSONObject ratingObject = new JSONObject();
            ratingObject.put("conversationId", conversationId);
            ratingObject.put("rating", rating);
            ratingObject.put("feedback", feedback);
            sendEvent("onSubmitRatingClick", String.valueOf(ratingObject));
        } catch(JSONException e) {
            sendEvent("onSubmitRatingClick", "error fetching data");
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageSent(Message message) {
        sendEvent("onMessageSent", GsonUtils.getJsonFromObject(message, Message.class));
    }

    @Override
    public void onMessageReceived(Message message) {
        sendEvent("onMessageReceived", GsonUtils.getJsonFromObject(message, Message.class));
    }

    @Override
    public void onBackButtonClicked(boolean isConversationOpened) {
        sendEvent("onBackButtonClicked", String.valueOf(isConversationOpened));
    }
    @Override
    public void onAttachmentClick(String attachmentType) {
        sendEvent("onAttachmentClick", attachmentType);
    }

    @Override
    public void onFaqClick(String FaqUrl) {
        sendEvent("onFaqClick", FaqUrl);
    }

    @Override
    public void onLocationClick() {
        sendEvent("onLocationClick", "clicked");
    }

    @Override
    public void onNotificationClick(Message message){
        sendEvent("onNotificationClick", GsonUtils.getJsonFromObject(message, Message.class));
    }

    @Override
    public void onVoiceButtonClick(String action){
        sendEvent("onVoiceButtonClick", action);
    }

    @Override
    public void onRatingEmoticonsClick(Integer ratingValue){
        sendEvent("onRatingEmoticonsClick", String.valueOf(ratingValue));
    }

    @Override
    public void onRateConversationClick(){
        sendEvent("onRateConversationClick", "clicked");
    }

}