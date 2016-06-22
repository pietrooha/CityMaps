package piotr.lista5.zad2.citymaps;

/**
 * Created by piotr on 26.04.15.
 */
public class MyElement {
    public String city_name;
    public double geoLength; // dl geo
    public double geoWidth; // szer geo

    public MyElement() // zeruje pola
    {
        city_name="";
        geoLength = geoWidth = 0;
    }

    // ustawia odpowiednio pola
    public MyElement(String name, double length, double width)
    {
        city_name = name;
        this.geoLength = length;
        this.geoWidth = width;
    }
}