package com.example.firsttask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    Connection connection;
    String connectionResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTextToSQL();

    }
        public void setTextToSQL()
        {
            EditText carName = findViewById(R.id.carName);
            EditText carColor = findViewById(R.id.carColor);
            EditText carPrice = findViewById(R.id.carPrice);
            Button addCarButton = findViewById(R.id.addButton);
            try{
                ConnectionHelper connectionHelper = new ConnectionHelper();
                connection = connectionHelper.connectionClass();

                if(connection != null)
                {
                    addCarButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String CN = carName.getText().toString();
                            String CC = carColor.getText().toString();
                            String CP = carPrice.getText().toString();
                            String query = String.format("insert into cars(NAME_CAR, COLOR_CAR, CAR_PRICE) values ('%s', '%s', %s)", CN, CC, CP);
                            Statement statement = null;
                            try {
                                statement = connection.createStatement();
                                ResultSet resultSet = statement.executeQuery(query);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
                else connectionResult = "Check connection!";
            }

            catch (Exception e)
            {
                Log.e("Error: ", e.getMessage());
            }

        }


    }