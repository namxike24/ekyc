package ai.ftech.fekyc.publish;

import ai.ftech.fekyc.domain.APIException;

public interface IFTechEkycCallback<DATA> {
    default void onSuccess(DATA info) {
    }

    default void onFail(APIException error) {
    }

    default void onCancel() {
    }
}
