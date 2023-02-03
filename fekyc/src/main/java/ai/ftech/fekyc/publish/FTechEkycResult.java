package ai.ftech.fekyc.publish;

import androidx.annotation.NonNull;

public final class FTechEkycResult<T> {
    private T data;
    private FTECH_EKYC_RESULT_TYPE type = FTECH_EKYC_RESULT_TYPE.CANCEL;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @NonNull
    public FTECH_EKYC_RESULT_TYPE getType() {
        return type;
    }

    public void setType(@NonNull FTECH_EKYC_RESULT_TYPE type) {
        this.type = type;
    }
}
