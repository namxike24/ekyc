package ai.ftech.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Objects;

import ai.ftech.fekyc.common.widget.toolbar.ToolbarView;
import ai.ftech.fekyc.presentation.picture.take.TakePictureActivity;
import ai.ftech.fekyc.publish.FTechEkycManager;

public class TakePhotoActivity extends AppCompatActivity {

    private String uploadOrientation = "front";

    private TextView tvStateFrontCapture;
    private TextView tvStateBackCapture;
    private TextView tvStateFaceCapture;

    public static void startTakePhotoScreen(Context context) {
        context.startActivity(new Intent(context, TakePhotoActivity.class));
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_photo_activity);
        Objects.requireNonNull(getSupportActionBar()).hide();

        ToolbarView tbvHeader = findViewById(R.id.tbvTakePhoto);
        Spinner spTypePicture = findViewById(R.id.spTypePicture);
        Button btnTakePicture = findViewById(R.id.btnTakePicture);

        tvStateFrontCapture = findViewById(R.id.tvTakePhotoStateFront);
        tvStateBackCapture = findViewById(R.id.tvTakePhotoStateBack);
        tvStateFaceCapture = findViewById(R.id.tvTakePhotoStateFace);

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        FTechEkycManager.notifyActive(this);
        setStatusCapture();
    }

    private void setStatusCapture() {
        if (!FTechEkycManager.INSTANCE.getTransactionFront().isEmpty()){
            tvStateFrontCapture.setSelected(true);
        }
        if (!FTechEkycManager.INSTANCE.getTransactionBack().isEmpty()){
            tvStateBackCapture.setSelected(true);
        }
        if (!FTechEkycManager.INSTANCE.getTransactionFace().isEmpty()){
            tvStateFaceCapture.setSelected(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        FTechEkycManager.notifyInactive(this);
    }
}
