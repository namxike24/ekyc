package ai.ftech.ekyc.publish;

public class FTechEkycResult<T> {
    private T data;
    private Throwable throwable;
    private RESULT_TYPE type = RESULT_TYPE.CANCEL;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public RESULT_TYPE getType() {
        return type;
    }

    public void setType(RESULT_TYPE type) {
        this.type = type;
    }
}
