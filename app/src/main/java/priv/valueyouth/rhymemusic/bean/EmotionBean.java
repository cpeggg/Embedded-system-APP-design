package priv.valueyouth.rhymemusic.bean;
import priv.valueyouth.rhymemusic.bean.faceRectangle;
import priv.valueyouth.rhymemusic.bean.emotion;
import priv.valueyouth.rhymemusic.bean.faceAttributes;


/**
 * 反序列化json字符串 from Microsoft Emotion API
 * Created by Idea on 2018/4/19.
 */


public class EmotionBean {
    public float anger;
    public float contempt;
    public float disgust;
    public float fear;
    public float happiness;
    public float neutral;
    public float sadness;
    public float surprise;
    public String getEmotion(){
        float a1= this.anger;
        float a2= this.contempt;
        float a3= this.disgust;
        float a4= this.fear;
        float a5= this.happiness;
        float a6= this.neutral;
        float a7= this.sadness;
        float a8= this.surprise;
        float max=Math.max(a1,Math.max(a2,Math.max(a3,Math.max(a4,Math.max(a5,Math.max(a6,Math.max(a7,a8)))))));

        if (a1==max) return "anger";
        if (a2==max) return "contempt";
        if (a3==max) return "disgust";
        if (a4==max) return "fear";
        if (a5==max) return "happiness";
        if (a6==max) return "neutral";
        if (a7==max) return "sadness";
        if (a8==max) return "surprise";
        return "emotion return ERROR";
    }
}
