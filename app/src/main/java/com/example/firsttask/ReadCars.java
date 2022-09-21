package com.example.firsttask;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ReadCars extends AppCompatActivity {

    Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readcars);
        String query = String.format("select * from cars");
        Statement statement = null;
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connection = connectionHelper.connectionClass();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery(query);
            int columns = resultSet.getMetaData().getColumnCount();
            resultSet.last();
            int rows = resultSet.getRow();
            resultSet.beforeFirst();
            TableLayout tableLayout = findViewById(R.id.tablelayout);

            while (resultSet.next())
            {


                //TextView showCarName = findViewById(R.id.showCarName);
                //TextView showCarColor = findViewById(R.id.showCarColor);
                //TextView showCarPrice = findViewById(R.id.showCarPrice);
                //TableRow row = new TableRow(this);
                TableRow row = (TableRow) LayoutInflater.from(this).inflate(R.layout.activity_readcars, null);

                ((TextView)row.findViewById(R.id.showCarName)).setText(resultSet.getString(2));
                ((TextView)row.findViewById(R.id.showCarColor)).setText(resultSet.getString(3));
                ((TextView)row.findViewById(R.id.showCarPrice)).setText(resultSet.getString(4));

                String SCN = resultSet.getString(2);
                String SCC = resultSet.getString(3);
                String SCP = resultSet.getString(4);
                /*TextView showCarName = new TextView(this);
                    showCarName.setText(SCN);
                    showCarName.setTextSize(20);
                TextView showCarColor = new TextView(this);
                    showCarColor.setText(SCC);
                    showCarColor.setTextSize(20);
                TextView showCarPrice = new TextView(this);
                    showCarPrice.setText(SCP);
                    showCarPrice.setTextSize(20);*/


                /*row.addView(showCarName);
                row.addView(showCarColor);
                row.addView(showCarPrice);*/
                tableLayout.addView(row);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}