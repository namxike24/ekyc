package ai.ftech.ekyc.publish;

import java.io.Serializable;

public final class FTechEkycInfo implements Serializable {
    private int code = 0;
    private String message = "";

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

    @Override
    public String toString() {
        return "FTechEkycInfo{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
