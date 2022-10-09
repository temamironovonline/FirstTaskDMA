package com.example.firsttask;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

public class updateDeleteCars extends AppCompatActivity {
    String[] array = new String[4];
    Connection connection;
    TextView carNameUpdate;
    TextView carColorUpdate;
    TextView carPriceUpdate;
    ImageView carImage;
    String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatedeletecars);
        carNameUpdate = findViewById(R.id.changeCarName);
        carColorUpdate = findViewById(R.id.changeCarColor);
        carPriceUpdate = findViewById(R.id.changeCarPrice);
        carImage = findViewById(R.id.carImage);
        carImage.setImageBitmap(ReadCars.getId(array));
        carNameUpdate.setText(array[1]);
        carColorUpdate.setText(array[2]);
        carPriceUpdate.setText(array[3]);

        updateDateSQL();
        addImage();
    }

    public void addImage(){
        carImage = findViewById(R.id.carImage);
        carImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImg.launch(intent);
        });
    }

    private final ActivityResultLauncher<Intent> pickImg = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            if (result.getData() != null) {
                Uri uri = result.getData().getData();
                try {
                    InputStream is = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    carImage.setImageBitmap(bitmap);
                    encodedImage = encodeImage(bitmap);
                } catch (Exception e) {

                }
            }
        }
    });

    private String encodeImage(Bitmap bitmap) {
        int prevW = 150;
        int prevH = bitmap.getHeight() * prevW / bitmap.getWidth();
        Bitmap b = Bitmap.createScaledBitmap(bitmap, prevW, prevH, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(bytes);
        }
        return null;
    }

    public void updateDataButton(){
        Button updateCarInfo = findViewById(R.id.updateCarInfo);
        updateCarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = String.format("update cars set NAME_CAR = '%s', COLOR_CAR = '%s', CAR_PRICE = '%s', PHOTO_CAR = CONVERT(VARBINARY(MAX), '%s') where CODE_CAR = '%s'", carNameUpdate.getText().toString(), carColorUpdate.getText().toString(), carPriceUpdate.getText().toString(), encodedImage, array[0]);
                Statement statement = null;
                try {
                    statement = connection.createStatement();
                    statement.execute(query);
                    Intent intent = new Intent(updateDeleteCars.this, ReadCars.class);
                    startActivity(intent);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void deleteDataButton(){
        Button deleteCarInfo = findViewById(R.id.deleteCarInfo);
        deleteCarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = String.format("delete from cars where CODE_CAR = '%s'", array[0]);
                Statement statement = null;
                try {
                    statement = connection.createStatement();
                    statement.execute(query);
                    Intent intent = new Intent(updateDeleteCars.this, ReadCars.class);
                    startActivity(intent);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void updateDateSQL() {
        try{
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connection = connectionHelper.connectionClass();
            updateDataButton();
            deleteDataButton();
        }
        catch (Exception e)
        {
            Log.e("Error: ", e.getMessage());
        }

    }
}
