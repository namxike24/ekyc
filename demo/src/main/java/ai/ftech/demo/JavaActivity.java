package ai.ftech.demo;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

import ai.ftech.fekyc.data.source.remote.model.ekyc.transaction.TransactionData;
import ai.ftech.fekyc.domain.APIException;
import ai.ftech.fekyc.domain.model.ekyc.EkycFormInfo;
import ai.ftech.fekyc.domain.model.facematching.FaceMatchingData;
import ai.ftech.fekyc.domain.model.transaction.TransactionProcessData;
import ai.ftech.fekyc.presentation.picture.take.TakePictureActivity;
import ai.ftech.fekyc.publish.FTechEkycInfo;
import ai.ftech.fekyc.publish.FTechEkycManager;
import ai.ftech.fekyc.publish.IFTechEkycCallback;

public class JavaActivity extends AppCompatActivity {
    private TextView tvState;
    private ProgressBar pbLoading;
    private Button btnStartEkyc;

    private String appId;
    private String licenseKey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity);
        tvState = findViewById(R.id.tvDemoState);
        pbLoading = findViewById(R.id.pbLoading);
        btnStartEkyc = findViewById(R.id.btnStartEkyc);

        FTechEkycManager.register(this);

        tvState.setOnClickListener(v -> {
            tvState.setText("");
        });

        btnStartEkyc.setOnClickListener(v -> {
            toggleStateStartEkyc();
            createTransaction();
        });

        try {
            ApplicationInfo applicationInfo = getApplicationContext().getPackageManager().getApplicationInfo(
                    getApplicationContext().getPackageName(),
                    PackageManager.GET_META_DATA
            );
            Bundle bundle = applicationInfo.metaData;
            appId = bundle.getString("ekycId");
            licenseKey = bundle.getString("licenseKey");
            registerEkyc();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.e("JavaActivity", e.getMessage());
        }
    }

    private void registerEkyc() {
        FTechEkycManager.registerEkyc(appId, licenseKey, new IFTechEkycCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean info) {
                IFTechEkycCallback.super.onSuccess(info);
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

    private void createTransaction() {
        FTechEkycManager.createTransaction(new IFTechEkycCallback<TransactionData>() {
            @Override
            public void onSuccess(TransactionData info) {
                IFTechEkycCallback.super.onSuccess(info);
                startEkyc();
                toggleStateStartEkyc();
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

    private void startEkyc() {
        FTechEkycManager.startEkyc(licenseKey, appId, new IFTechEkycCallback<FTechEkycInfo>() {
            @Override
            public void onSuccess(FTechEkycInfo info) {
                IFTechEkycCallback.super.onSuccess(info);
                Toast.makeText(JavaActivity.this, info.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void toggleStateStartEkyc() {
        if (pbLoading.getVisibility() == View.VISIBLE) {
            pbLoading.setVisibility(View.GONE);
            btnStartEkyc.setVisibility(View.VISIBLE);
        } else {
            pbLoading.setVisibility(View.VISIBLE);
            btnStartEkyc.setVisibility(View.GONE);
        }
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
