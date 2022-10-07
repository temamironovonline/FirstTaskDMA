package com.example.firsttask;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;


public class ReadCars extends AppCompatActivity {
    private static String currentIdCar;
    private static String currentNameCar;
    private static String currentColorCar;
    private static String currentPriceCar;

    String query = "select * from cars"; // Переменная, по которой осуществляются все запросы
    EditText searchDataChangeName; // Объект, предназначенный для поиска данных в БД по названию автомобиля
    ListView mainListData; // Основной список, в котором выводятся данные из БД
    ArrayList<String> SortColor; // Список для хранения цветов автомобилей из БД
    String[] arraySpinnerPrice; // Массив для хранения методов сортировке по цене
    int currentPriceSort;
    String currentColorSort = "";

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
        forTextChanged();
        getListFromSQL();
        forSpinnerPriceChanged(sortingDataByPrice());
        forSpinnerColorChanged(sortingDataByColor(SortColor));

    }


    public void forSpinnerColorChanged(Spinner forSortByColor){
        forSortByColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                currentColorSort = String.valueOf(parentView.getItemAtPosition(position));
                if (currentColorSort.equals("Нет")) currentColorSort = "";
                if (currentPriceSort == 0)
                    query = String.format("select * from cars where NAME_CAR like '%s%%' and COLOR_CAR like '%s%%'", searchDataChangeName.getText().toString(), currentColorSort);
                else if (currentPriceSort == 1)
                    query = String.format("select * from cars where NAME_CAR like '%s%%' and COLOR_CAR like '%s%%' Order by CAR_PRICE asc", searchDataChangeName.getText().toString(), currentColorSort);
                else query = String.format("select * from cars where NAME_CAR like '%s%%' and COLOR_CAR like '%s%%' Order by CAR_PRICE desc", searchDataChangeName.getText().toString(), currentColorSort);
                getListFromSQL();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
    }

    public void forSpinnerPriceChanged(Spinner forSortByPrice){
        forSortByPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                currentPriceSort = parentView.getSelectedItemPosition();
                if (currentColorSort.equals("Нет")) currentColorSort = "";
                if (currentPriceSort == 0)
                    query = String.format("select * from cars where NAME_CAR like '%s%%' and COLOR_CAR like '%s%%'", searchDataChangeName.getText().toString(), currentColorSort);
                else if (currentPriceSort == 1)
                    query = String.format("select * from cars where NAME_CAR like '%s%%' and COLOR_CAR like '%s%%' Order by CAR_PRICE asc", searchDataChangeName.getText().toString(), currentColorSort);
                else query = String.format("select * from cars where NAME_CAR like '%s%%' and COLOR_CAR like '%s%%' Order by CAR_PRICE desc", searchDataChangeName.getText().toString(), currentColorSort);
                getListFromSQL();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
    }

    public void forTextChanged()
    {
        searchDataChangeName = findViewById(R.id.search);
        searchDataChangeName.addTextChangedListener(new TextWatcher(){
                @Override
                public void afterTextChanged(Editable s) {
                    if (currentColorSort.equals("Нет")) currentColorSort = "";
                    if (currentPriceSort == 0)
                        query = String.format("select * from cars where NAME_CAR like '%s%%' and COLOR_CAR like '%s%%'", searchDataChangeName.getText().toString(), currentColorSort);
                    else if (currentPriceSort == 1)
                        query = String.format("select * from cars where NAME_CAR like '%s%%' and COLOR_CAR like '%s%%' Order by CAR_PRICE asc", searchDataChangeName.getText().toString(), currentColorSort);
                    else query = String.format("select * from cars where NAME_CAR like '%s%%' and COLOR_CAR like '%s%%' Order by CAR_PRICE desc", searchDataChangeName.getText().toString(), currentColorSort);
                    getListFromSQL();
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
            });

    }

    public void forGoToListItem(ResultSet resultSet) {
        mainListData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
    }

    public Spinner sortingDataByPrice(){
        arraySpinnerPrice = new String[] {
                "Нет", "По возрастанию", "По убыванию"
        };
        Spinner s = (Spinner) findViewById(R.id.sortingPrice);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinnerPrice);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        return s;
    }

    public Spinner sortingDataByColor(ArrayList<String> forSortingColor){
        Spinner ss = (Spinner) findViewById(R.id.sortingColor);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, forSortingColor);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ss.setAdapter(adapter);

        return ss;
    }


    public void getListFromSQL(){
        Connection connection;
        Statement statement = null;
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connection = connectionHelper.connectionClass();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            SortColor = new ArrayList<String>();
            SortColor.add("Нет");

            ResultSet resultSet = statement.executeQuery(query);
            ArrayList<Cars> dataCars = new ArrayList<Cars>();

            while (resultSet.next()) {
                dataCars.add(new Cars(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4)));
                boolean checkSame = true;
                for (int i = 0; i < SortColor.size(); i++)
                {
                    if (Objects.equals(resultSet.getString(3), SortColor.get(i)))
                    {
                        checkSame = true;
                        break;
                    }
                    else checkSame = false;
                }
                if (!checkSame) SortColor.add(resultSet.getString(3));
            }

            CarsAdapter carsAdapter = new CarsAdapter(this, dataCars);
            mainListData = (ListView) findViewById(R.id.lvMain);
            mainListData.setAdapter(carsAdapter);
            forGoToListItem(resultSet);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}