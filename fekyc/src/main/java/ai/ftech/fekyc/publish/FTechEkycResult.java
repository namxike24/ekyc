package ai.ftech.fekyc.publish;

import androidx.annotation.NonNull;

import ai.ftech.fekyc.domain.APIException;

public final class FTechEkycResult<T> {
    private T data;
    private FTECH_EKYC_RESULT_TYPE type = FTECH_EKYC_RESULT_TYPE.CANCEL;
    private APIException error;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public APIException getError() {
        return error;
    }

    public void setError(APIException error) {
        this.error = error;
    }

    @NonNull
    public FTECH_EKYC_RESULT_TYPE getType() {
        return type;
    }

    public void setType(@NonNull FTECH_EKYC_RESULT_TYPE type) {
        this.type = type;
    }
}
