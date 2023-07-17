package ai.ftech.demo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ai.ftech.fekyc.data.source.remote.model.ekyc.init.sdk.RegisterEkycData;
import ai.ftech.fekyc.data.source.remote.model.ekyc.transaction.TransactionData;
import ai.ftech.fekyc.domain.APIException;
import ai.ftech.fekyc.domain.model.facematching.FaceMatchingData;
import ai.ftech.fekyc.presentation.AppPreferences;
import ai.ftech.fekyc.presentation.picture.take.TakePictureActivity;
import ai.ftech.fekyc.publish.FTechEkycManager;
import ai.ftech.fekyc.publish.IFTechEkycCallback;

public class JavaActivity extends AppCompatActivity {
    private TextView tvState;
    private Button btnCreateTransaction;
    private Button btnSubmitInfo;
    private Button btnUploadPhoto;
    private Button btnFaceMatching;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity);
        tvState = findViewById(R.id.tvDemoState);
        btnCreateTransaction = findViewById(R.id.btnDemoCreateTransaction);
        btnSubmitInfo = findViewById(R.id.btnSubmitInfo);
        btnUploadPhoto = findViewById(R.id.btnUploadPhoto);
        btnFaceMatching = findViewById(R.id.btnFaceMatching);

        FTechEkycManager.register(this);

        tvState.setOnClickListener(v -> {
            tvState.setText("");
        });
        FTechEkycManager.registerEkyc(new IFTechEkycCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean info) {
                IFTechEkycCallback.super.onSuccess(info);
                Toast.makeText(JavaActivity.this, "Register succeeded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(APIException error) {
                IFTechEkycCallback.super.onFail(error);
            }

            @Override
            public void onCancel() {
                IFTechEkycCallback.super.onCancel();
            }
        });


        btnCreateTransaction.setOnClickListener(v -> createTransaction());

        btnSubmitInfo.setOnClickListener(v -> {
            executeSubmitInfo();
        });

        btnUploadPhoto.setOnClickListener(v -> {
            launchCaptureScreen();
        });

        btnFaceMatching.setOnClickListener(v -> {
            executeFaceMatching();
        });
    }

    private void launchCaptureScreen() {
        startActivity(new Intent(this, TakePictureActivity.class));
    }

    private void createTransaction() {
        FTechEkycManager.createTransaction(new IFTechEkycCallback<TransactionData>() {
            @Override
            public void onSuccess(TransactionData info) {
                IFTechEkycCallback.super.onSuccess(info);
                Toast.makeText(JavaActivity.this, "Transaction created!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(APIException error) {
                IFTechEkycCallback.super.onFail(error);
                Toast.makeText(JavaActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                IFTechEkycCallback.super.onCancel();
            }
        });
    }

    private void executeFaceMatching() {
            FTechEkycManager.faceMatching(new IFTechEkycCallback<FaceMatchingData>() {
                @Override
                public void onSuccess(FaceMatchingData data) {
                    Toast.makeText(JavaActivity.this, "Matching succeeded!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFail(APIException error) {
                    IFTechEkycCallback.super.onFail(error);
                    Toast.makeText(JavaActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel() {
                }
            });
    }

    private boolean hasTransactionId() {
        return !FTechEkycManager.INSTANCE.getTransactionId().isEmpty();
    }

    private void executeSubmitInfo() {
            FTechEkycManager.submitInfo(new IFTechEkycCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean info) {
                    Toast.makeText(JavaActivity.this, "Submit Info Succeeded!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFail(APIException error) {
                    IFTechEkycCallback.super.onFail(error);
                    Toast.makeText(JavaActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel() {
                }
            });
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
