package ai.ftech.demo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tuanfadbg.takephotoutils.TakePhotoCallback;
import com.tuanfadbg.takephotoutils.TakePhotoUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ai.ftech.fekyc.common.widget.toolbar.ToolbarView;
import ai.ftech.fekyc.domain.model.capture.CaptureData;
import ai.ftech.fekyc.presentation.picture.take.TakePictureActivity;
import ai.ftech.fekyc.publish.FTechEkycManager;
import ai.ftech.fekyc.publish.IFTechEkycCallback;

public class TakePhotoActivity extends AppCompatActivity {

    private Button btnUpload;
    private Button btnTakePicture;
    private ImageView ivPreviewPicture;
    private ProgressBar pbLoading;

    private TakePhotoUtils takePhotoUtils;
    private final TakePhotoCallback takePhotoCallback = new TakePhotoCallback() {

        @Override
        public void onMultipleSuccess(List<String> imagesEncodedList, ArrayList<Uri> mArrayUri, List<Long> lastModifieds) {

        }

        @Override
        public void onSuccess(String path, Bitmap bitmap, int width, int height, Uri sourceUri, long lastModified) {
            currentPath = path;
            ivPreviewPicture.setImageBitmap(bitmap);
            btnUpload.setVisibility(View.VISIBLE);
            btnTakePicture.setVisibility(View.GONE);
        }

        @Override
        public void onFail() {

        }
    };

    private String currentPath = null;
    private String uploadOrientation = "front";

    public static void startTakePhotoScreen(Context context) {
        context.startActivity(new Intent(context, TakePhotoActivity.class));
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_photo_activity);
        Objects.requireNonNull(getSupportActionBar()).hide();
        takePhotoUtils = new TakePhotoUtils(this, BuildConfig.APPLICATION_ID + ".fileprovider");

        ToolbarView tbvHeader = findViewById(R.id.tbvTakePhoto);
        Spinner spTypePicture = findViewById(R.id.spTypePicture);
        btnUpload = findViewById(R.id.btnUploadPicture);
        btnTakePicture = findViewById(R.id.btnTakePicture);
        ivPreviewPicture = findViewById(R.id.ivPreviewPicture);
        pbLoading = findViewById(R.id.pbLoading);

        tbvHeader.setListener(new ToolbarView.IListener() {
            @Override
            public void onLeftIconClick() {
                finish();
            }

            @Override
            public void onRightTextClick() {
            }

            @Override
            public void onRightIconClick() {
            }
        });

        List<String> listOption = List.of("Front", "Back", "Face");
        spTypePicture.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        uploadOrientation = "front";
                        break;
                    case 1:
                        uploadOrientation = "back";
                        break;
                    case 2:
                        uploadOrientation = null;
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spTypePicture.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listOption));

        btnTakePicture.setOnClickListener(v -> {
            Intent intent = new Intent(this, TakePictureActivity.class);
            intent.putExtra(TakePictureActivity.KEY_ORIENTATION, uploadOrientation);
            startActivity(intent);
        });
        btnUpload.setOnClickListener(v -> {
            String transactionId = "2e5912a0-0ead-4fda-8849-11540b9b68ff";
            btnUpload.setVisibility(View.GONE);
            ivPreviewPicture.setImageBitmap(null);
            pbLoading.setVisibility(View.VISIBLE);
            FTechEkycManager.uploadPhoto(currentPath, uploadOrientation, transactionId, new IFTechEkycCallback<CaptureData>() {
                @Override
                public void onSuccess(CaptureData data) {
                    Log.d("DucPT", "onSuccess UploadPhoto: " + data.toString());
                    btnTakePicture.setVisibility(View.VISIBLE);
                    pbLoading.setVisibility(View.GONE);
                }

                @Override
                public void onFail() {
                    Log.d("DucPT", "onFail UploadPhoto");
                    btnTakePicture.setVisibility(View.VISIBLE);
                    pbLoading.setVisibility(View.GONE);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        takePhotoUtils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        takePhotoUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
