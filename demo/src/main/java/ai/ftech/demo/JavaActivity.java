package ai.ftech.demo;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import ai.ftech.fekyc.publish.FTechEkycInfo;
import ai.ftech.fekyc.publish.FTechEkycManager;
import ai.ftech.fekyc.publish.IFTechEkycCallback;

public class JavaActivity extends AppCompatActivity {
    private TextView tvState;
    private Button btnEkyc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity);
        tvState = findViewById(R.id.tvDemoState);
        btnEkyc = findViewById(R.id.btnDemoEkyc);

        FTechEkycManager.register(this);

        tvState.setOnClickListener(v -> {
            tvState.setText("");
        });



        btnEkyc.setOnClickListener(v -> {
            Random rd = new Random();
            String transId = "" + rd.nextInt(100000);
            FTechEkycManager.startEkyc("licenceftechekyc", "ftechekycapp", transId, new IFTechEkycCallback<FTechEkycInfo>() {
                @Override
                public void onSuccess(FTechEkycInfo info) {
                    Log.d("anhnd", "onSuccess() called with: info = [" + info + "]");
                    tvState.setText(info.getMessage());
                }

                @Override
                public void onFail() {
                }

                @Override
                public void onCancel() {

                }
            });
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
