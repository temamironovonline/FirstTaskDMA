package com.example.firsttask;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

public class MainActivity extends AppCompatActivity {

    Connection connection;
    String connectionResult = "";
    ImageView carsImage;
    String encodedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkConnection();
        addImage();
    }
    public void checkConnection() // Проверка подключения к БД
    {
        try{
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connection = connectionHelper.connectionClass();
            if(connection != null)
            {
                setTextToSQL();
            }
            else connectionResult = "Check connection!";
        }

        catch (Exception e)
        {
            Log.e("Error: ", e.getMessage());
        }
    }

    //Отдельный метод для вызова события на кнопку "Добавить запись"
    public void addDataButton(EditText forCarName, EditText forCarColor, EditText forCarPrice, String forCarImage){
        Button addCarButton = findViewById(R.id.addButton);
        addCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String CN = forCarName.getText().toString();
                String CC = forCarColor.getText().toString();
                String CP = forCarPrice.getText().toString();
                String query = String.format("insert into cars(NAME_CAR, COLOR_CAR, CAR_PRICE, PHOTO_CAR) values ('%s', '%s', %s, CONVERT(VARBINARY(MAX), '%s'))", CN, CC, CP, encodedImage);
                Statement statement = null;
                try {
                    statement = connection.createStatement();
                    statement.execute(query);
                    forCarName.setText("");
                    forCarColor.setText("");
                    forCarPrice.setText("");
                    carsImage.setImageResource(R.drawable.empty);
                    encodedImage = null;
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    //Отдельный метод для вызова события на кнопку "Вывести все записи"
    public void readDataButton(){
        Button readCarButton = findViewById(R.id.readButton);
        readCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReadCars.class);
                startActivity(intent);
            }
        });
    }

    //Метод для события при нажатии на изображение, чтобы его изменить
    public void addImage(){

        carsImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImg.launch(intent);
        });
    }

    //Главный метод
    public void setTextToSQL() {
        carsImage = findViewById(R.id.carImage);
        carsImage.setImageResource(R.drawable.empty);
        EditText carName = findViewById(R.id.carName);
        EditText carColor = findViewById(R.id.carColor);
        EditText carPrice = findViewById(R.id.carPrice);
        addDataButton(carName, carColor, carPrice, encodedImage);
        readDataButton();
    }

    private final ActivityResultLauncher<Intent> pickImg = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            if (result.getData() != null) {
                Uri uri = result.getData().getData();
                try {
                    InputStream is = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    carsImage.setImageBitmap(bitmap);
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
}