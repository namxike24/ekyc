package ai.ftech.ekyc.publish;

public interface IFTechEkycCallback<DATA> {
    default void onSuccess(DATA info) {}
    default void onFail(Throwable throwable) {}
    default void onCancel() {}
}
