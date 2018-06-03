package priv.valueyouth.rhymemusic.util;

public interface HttpCallbackListener {
    void onFinish (String response);

    void onError (Exception e);
}
