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
import ai.ftech.fekyc.domain.model.ekyc.PHOTO_INFORMATION;
import ai.ftech.fekyc.domain.model.ekyc.PHOTO_TYPE;
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
    private PHOTO_INFORMATION informationUpload = PHOTO_INFORMATION.BACK;
    private PHOTO_TYPE typeUpload = PHOTO_TYPE.SSN;

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

        List<String> listOption = List.of("SSN", "DRIVER_LICENSE", "PASSPORT", "FACE");
        spTypePicture.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        typeUpload = PHOTO_TYPE.SSN;
                        break;
                    case 1:
                        typeUpload = PHOTO_TYPE.DRIVER_LICENSE;
                            break;
                    case 2:
                        typeUpload = PHOTO_TYPE.PASSPORT;
                        break;
                    case 3:
                        informationUpload = PHOTO_INFORMATION.FACE;
                        break;
                    default:
                        typeUpload = PHOTO_TYPE.SSN;
                        informationUpload = PHOTO_INFORMATION.BACK;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spTypePicture.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listOption));

        btnTakePicture.setOnClickListener(v -> {
            takePhotoUtils.showDialogSelectImage(null, null, null).setListener(takePhotoCallback);
        });
        btnUpload.setOnClickListener(v -> {
            btnUpload.setVisibility(View.GONE);
            ivPreviewPicture.setImageBitmap(null);
            pbLoading.setVisibility(View.VISIBLE);
            FTechEkycManager.uploadPhoto(currentPath, typeUpload, informationUpload, new IFTechEkycCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean info) {
                    Log.d("DucPT", "onSuccess UploadPhoto: "+info.toString());
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
