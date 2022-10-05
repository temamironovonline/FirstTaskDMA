package com.example.firsttask;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class updateDeleteCars extends AppCompatActivity {
    String[] array = new String[4];
    Connection connection;
    TextView carNameUpdate;
    TextView carColorUpdate;
    TextView carPriceUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatedeletecars);
        carNameUpdate = findViewById(R.id.changeCarName);
        carColorUpdate = findViewById(R.id.changeCarColor);
        carPriceUpdate = findViewById(R.id.changeCarPrice);
        ReadCars.getId(array);
        carNameUpdate.setText(array[1]);
        carColorUpdate.setText(array[2]);
        carPriceUpdate.setText(array[3]);
        updateDateSQL();
    }

    public void updateDateSQL() {
        try{
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connection = connectionHelper.connectionClass();
            Button updateCarInfo = findViewById(R.id.updateCarInfo);
            Button deleteCarInfo = findViewById(R.id.deleteCarInfo);
            updateCarInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String query = String.format("update cars set NAME_CAR = '%s', COLOR_CAR = '%s', CAR_PRICE = '%s' where CODE_CAR = '%s'", carNameUpdate.getText().toString(), carColorUpdate.getText().toString(), carPriceUpdate.getText().toString(), array[0]);
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
        catch (Exception e)
        {
            Log.e("Error: ", e.getMessage());
        }

    }
}
