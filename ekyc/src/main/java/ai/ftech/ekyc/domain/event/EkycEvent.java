package ai.ftech.ekyc.domain.event;

public class EkycEvent implements IFbaseEvent {
    private String message;
    private Throwable  throwable;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
