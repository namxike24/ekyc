package ai.ftech.ekyc.domain.event;

import java.io.Serializable;

import ai.ftech.dev.base.parcelable.IParcelable;
import ai.ftech.ekyc.domain.APIException;

public class EkycEvent implements IFbaseEvent, Serializable {
    private String message;
    private APIException exception;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public APIException getException() {
        return exception;
    }

    public void setException(APIException exception) {
        this.exception = exception;
    }
}
