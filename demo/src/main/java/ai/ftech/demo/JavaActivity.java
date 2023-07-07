package ai.ftech.demo;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.Random;

import ai.ftech.fekyc.data.source.remote.model.ekyc.init.sdk.InitSDKData;
import ai.ftech.fekyc.data.source.remote.model.ekyc.submit.NewSubmitInfoRequest;
import ai.ftech.fekyc.domain.model.ekyc.EkycInfo;
import ai.ftech.fekyc.domain.model.facematching.FaceMatchingData;
import ai.ftech.fekyc.domain.model.submit.SubmitInfo;
import ai.ftech.fekyc.publish.FTechEkycInfo;
import ai.ftech.fekyc.publish.FTechEkycManager;
import ai.ftech.fekyc.publish.IFTechEkycCallback;

public class JavaActivity extends AppCompatActivity {
    private TextView tvState;
    private Button btnEkyc;
    private Button btnSubmitInfo;
    private Button btnUploadPhoto;
    private Button btnFaceMatching;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity);
        tvState = findViewById(R.id.tvDemoState);
        btnEkyc = findViewById(R.id.btnDemoEkyc);
        btnSubmitInfo = findViewById(R.id.btnSubmitInfo);
        btnUploadPhoto = findViewById(R.id.btnUploadPhoto);
        btnFaceMatching = findViewById(R.id.btnFaceMatching);

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

    private void executeFaceMatching() {
        String idTransaction = "2e5912a0-0ead-4fda-8849-11540b9b68ff";
        String idSessionFront = "4e2e9287-4b03-4bd0-9022-aaad4bc62916";
        String idSessionBack = "5a7939c5-74fe-4bb2-9ef4-a0d2e401b632";
        String idSessionFace = "3a6b002f-0007-4b5c-b09f-6838d7e7265";
        FTechEkycManager.faceMatching(
                idTransaction,
                idSessionFront,
                idSessionBack,
                idSessionFace,
                new IFTechEkycCallback<FaceMatchingData>() {
                    @Override
                    public void onSuccess(FaceMatchingData data) {
                        Log.d("DucPT", "onSuccess FaceMatching: " + data.toString());
                    }

                    @Override
                    public void onFail() {
                        Log.d("DucPT", "onFail FaceMatching");
                    }

                    @Override
                    public void onCancel() {
                    }
                }
        );
    }

    private void executeSubmitInfo() {
        FTechEkycManager.submitInfo(generateMockInfo(), new IFTechEkycCallback<SubmitInfo>() {
            @Override
            public void onSuccess(SubmitInfo info) {
                Log.d("DucPT", "onSuccess SubmitInfo: " + info.toString());
            }

            @Override
            public void onFail() {
                Log.d("DucPT", "onFail SubmitInfo");
            }

            @Override
            public void onCancel() {
            }
        });
    }

    private NewSubmitInfoRequest generateMockInfo() {
        String mockJson = "{\"card_info_submit\": {\n" +
                "            \"id\": \"001093047064\",\n" +
                "            \"birth_day\": \"05/01/1993\",\n" +
                "            \"birth_place\": \"\",\n" +
                "            \"card_type\": \"CĂN CƯỚC CÔNG DÂN\",\n" +
                "            \"gender\": \"Nam\",\n" +
                "            \"issue_date\": \"24/07/2021\",\n" +
                "            \"issue_place\": \"CỤC TRƯỞNG CỤC CẢNH SÁT QUẢN LÝ HÀNH CHÍNH VỀ TRẬT TỰ XÃ HỘI\",\n" +
                "            \"name\": \"ỨNG HOÀNG HIỆP\",\n" +
                "            \"nationality\": \"Việt Nam\",\n" +
                "            \"origin_location\": \"Yên Bắc, Thị Xã Duy Tiên, Hà Nam\",\n" +
                "            \"passport_no\": \"\",\n" +
                "            \"recent_location\": \"8 ngách 9 ngõ 647\\\\nKim Ngưu, Vĩnh Tuy, Hai Bà Trưng, Hà Nội\",\n" +
                "            \"valid_date\": \"05/01/2033\",\n" +
                "            \"feature\": \"Nốt ruồi C:3 cm trên sau mép trái\",\n" +
                "            \"nation\": \"\",\n" +
                "            \"religion\": \"\",\n" +
                "            \"mrz\": \"IDVNM0930470641001093047064<<2\\\\n9301054M3301052VNM<<<<<<<<<<<8\\\\nUNG<<HOANG<HIEP<<<<<<<<<<<<<<<\"\n" +
                "        },\n" +
                "        \"pre_process_id\": \"0c364ac1-e34f-4678-96e5-4f9fbff7fa1c\"\n" +
                "}";
        return new Gson().fromJson(mockJson, NewSubmitInfoRequest.class);
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
