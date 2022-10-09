package com.example.firsttask;

import android.graphics.Bitmap;
import android.media.Image;

public class Cars {
    int idCar;
    String nameCar;
    String colorCar;
    String priceCar;
    Bitmap photoCar;

    Cars(int idCar, String nameCar, String colorCar, String priceCar, Bitmap photoCar){
        this.idCar = idCar;
        this.nameCar = nameCar;
        this.colorCar = colorCar;
        this.priceCar = priceCar;
        this.photoCar = photoCar;
    }
}
