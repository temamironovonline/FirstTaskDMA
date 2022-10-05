package com.example.firsttask;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ReadCars extends AppCompatActivity {
    private static String currentIdCar;
    private static String currentNameCar;
    private static String currentColorCar;
    private static String currentPriceCar;
    Connection connection;


    public static void getId(String[] array) {
        array[0] = currentIdCar;
        array[1] = currentNameCar;
        array[2] = currentColorCar;
        array[3] = currentPriceCar;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readcars2);
        getListFromSQL();
    }

    public void getListFromSQL(){
        String query = String.format("select * from cars");
        Statement statement = null;
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connection = connectionHelper.connectionClass();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery(query);
            ArrayList<Cars> dataCars = new ArrayList<Cars>();
            while (resultSet.next())
            {
                dataCars.add(new Cars(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4)));
            }
            CarsAdapter carsAdapter = new CarsAdapter(this, dataCars);
            ListView lv = (ListView) findViewById(R.id.lvMain);
            lv.setAdapter(carsAdapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        resultSet.beforeFirst();
                        while(resultSet.next())
                        {
                            if (resultSet.getRow() == position+1)
                            {
                                currentIdCar = resultSet.getString(1);
                                currentNameCar = resultSet.getString(2);
                                currentColorCar = resultSet.getString(3);
                                currentPriceCar = resultSet.getString(4);
                                break;
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(ReadCars.this, updateDeleteCars.class);
                    startActivity(intent);
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}