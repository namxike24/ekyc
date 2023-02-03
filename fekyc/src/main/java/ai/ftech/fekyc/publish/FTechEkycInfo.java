package ai.ftech.fekyc.publish;

import java.io.Serializable;

public final class FTechEkycInfo implements Serializable {
    private int code = 0;
    private String message = "";
    private String sessionId = "";

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sectionId) {
        this.sessionId = sectionId;
    }


    @Override
    public String toString() {
        return "FTechEkycInfo{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }

}
