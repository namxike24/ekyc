package ai.ftech.demo;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ai.ftech.fekyc.data.repo.converter.FaceMatchingDataConvertToSubmitRequest;
import ai.ftech.fekyc.data.source.remote.model.ekyc.submit.NewSubmitInfoRequest;
import ai.ftech.fekyc.data.source.remote.model.ekyc.transaction.TransactionData;
import ai.ftech.fekyc.domain.APIException;
import ai.ftech.fekyc.domain.model.facematching.FaceMatchingData;
import ai.ftech.fekyc.publish.FTechEkycManager;
import ai.ftech.fekyc.publish.IFTechEkycCallback;

public class JavaActivity extends AppCompatActivity {
    private TextView tvState;
    private Button btnEkyc;
    private Button btnCreateTransaction;
    private Button btnSubmitInfo;
    private Button btnUploadPhoto;
    private Button btnFaceMatching;

    private NewSubmitInfoRequest submitInfoRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity);
        tvState = findViewById(R.id.tvDemoState);
        btnEkyc = findViewById(R.id.btnDemoEkyc);
        btnCreateTransaction = findViewById(R.id.btnDemoCreateTransaction);
        btnSubmitInfo = findViewById(R.id.btnSubmitInfo);
        btnUploadPhoto = findViewById(R.id.btnUploadPhoto);
        btnFaceMatching = findViewById(R.id.btnFaceMatching);

        FTechEkycManager.register(this);

        tvState.setOnClickListener(v -> {
            tvState.setText("");
        });
        btnEkyc.setOnClickListener(v -> {
//            Random rd = new Random();
//            String transId = "" + rd.nextInt(100000);
//            FTechEkycManager.startEkyc("licenceftechekyc", "ftechekycapp", transId, new IFTechEkycCallback<FTechEkycInfo>() {
//                @Override
//                public void onSuccess(FTechEkycInfo info) {
//                    Log.d("anhnd", "onSuccess() called with: info = [" + info + "]");
//                    tvState.setText(info.getMessage());
//                }
//
//                @Override
//                public void onFail() {
//                }
//
//                @Override
//                public void onCancel() {
//
//                }
//            });
//            FTechEkycManager.init(this);
            FTechEkycManager.registerEkyc();
        });

        btnCreateTransaction.setOnClickListener(v -> createTransaction());

        btnSubmitInfo.setOnClickListener(v -> {
            executeSubmitInfo();
        });

        btnUploadPhoto.setOnClickListener(v -> {
            TakePhotoActivity.startTakePhotoScreen(this);
        });

        btnFaceMatching.setOnClickListener(v -> {
            executeFaceMatching();
        });
    }

    private void createTransaction() {
        FTechEkycManager.createTransaction(new IFTechEkycCallback<TransactionData>() {
            @Override
            public void onSuccess(TransactionData info) {
                IFTechEkycCallback.super.onSuccess(info);
                FTechEkycManager.setTransactionId(info.getTransactionId());
            }
        });
    }

    private void executeFaceMatching() {
        FTechEkycManager.faceMatching(new IFTechEkycCallback<FaceMatchingData>() {
            @Override
            public void onSuccess(FaceMatchingData data) {
                submitInfoRequest = new FaceMatchingDataConvertToSubmitRequest().convert(data);
                Log.d("DucPT", "SubmitReq: " + submitInfoRequest);
            }

            @Override
            public void onFail(APIException error) {
                IFTechEkycCallback.super.onFail(error);
                Toast.makeText(JavaActivity.this, "ErrorFaceMatching: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
            }
        });
    }

    private void executeSubmitInfo() {
        if (submitInfoRequest != null) {
            FTechEkycManager.submitInfo(submitInfoRequest, new IFTechEkycCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean info) {
                    submitInfoRequest = null;
                    clearTransaction();
                    Toast.makeText(JavaActivity.this, "Success Submit Info", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFail(APIException error) {
                    IFTechEkycCallback.super.onFail(error);
                    Toast.makeText(JavaActivity.this, "ErrorSubmitInfo: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel() {
                }
            });
        } else {
            Toast.makeText(this, "Submit request is null", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearTransaction() {
        FTechEkycManager.setTransactionId("");
        FTechEkycManager.setTransactionFront("");
        FTechEkycManager.setTransactionBack("");
        FTechEkycManager.setTransactionFace("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        FTechEkycManager.notifyActive(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FTechEkycManager.notifyInactive(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FTechEkycManager.unregister(this);
    }
}
