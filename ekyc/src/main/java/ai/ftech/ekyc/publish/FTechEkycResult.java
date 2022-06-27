package ai.ftech.ekyc.publish;

public class FTechEkycResult<T> {
    private T data;
    private RESULT_TYPE type = RESULT_TYPE.CANCEL;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public RESULT_TYPE getType() {
        return type;
    }

    public void setType(RESULT_TYPE type) {
        this.type = type;
    }
}
