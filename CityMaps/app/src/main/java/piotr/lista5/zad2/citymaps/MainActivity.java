package piotr.lista5.zad2.citymaps;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;

import static com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;

public class MainActivity extends Activity implements OnMapReadyCallback, OnMapLoadedCallback {

    private GoogleMap mapa;
    LinkedList<MyElement> list_of_city;
    LinkedList<MyElement> list_of_selected_city;
    ArrayList<String> names;
    ArrayAdapter<String> myAdapter;
    Spinner mySpinner;
    private Polyline line;
    private Marker markers;
    MapFragment mf;
    int latest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mf = (MapFragment)
        getFragmentManager().findFragmentById(R.id.mapaGoogle);
        mf.getMapAsync(this);

        mapa = mf.getMap();

        list_of_city = new LinkedList<MyElement>();
        list_of_selected_city = new LinkedList<MyElement>();

        try
        {
            String line = "";
            String committed[]; //rozdzielone
            String coordinates[]; //wspolrzedne geo
            MyElement addedElement; //dodawany
            names = new ArrayList<String>();
            BufferedReader br = new BufferedReader(new InputStreamReader(getResources()
                    .openRawResource(R.raw.miasta), "UTF-8"));

            try
            {
                // wyciagnij wiersz tekstowy z miasta.txt
                while ((line = br.readLine()) != null)
                {
                    // podziel wyciagniety wiersz na stringi wedle symbolu *
                    committed = line.split(" \\* ");

                    // podziel wsp geo wedle spacji
                    coordinates = committed[1].split(" ");

                    // zamiana stringow ze wsp geo na liczby typu double
                    double coord1 = Double.parseDouble(coordinates[0]);
                    double coord2 = Double.parseDouble(coordinates[1]);

                    addedElement = new MyElement(committed[0], coord1, coord2);

                    // dodaj etykiete do Spinnera
                    names.add(committed[0]);

                    // dodaj nowe miasto do listy i jego wspolrzedne
                    list_of_city.add(addedElement);
                }

                br.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        mySpinner = (Spinner)findViewById(R.id.myspinner);

        // utworz adapter do listy zawierajacej etykiety miast
        myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, names);

        mySpinner.setAdapter(myAdapter); // dodaj adapter do Spinnera

        // przylaczenie listenera do Spinnera
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            // w momencie wybrania ktoregos miasta
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                MyElement addedElement = new MyElement(list_of_city.get(position).city_name, list_of_city.get(position).geoLength,
                        list_of_city.get(position).geoWidth);
                list_of_selected_city.add(addedElement);

                latest = position;
                markers = mapa.addMarker(new MarkerOptions()
                        .position(new LatLng(list_of_city.get(position).geoLength, list_of_city.get(position).geoWidth))
                        .title(list_of_city.get(position).city_name));

                // rysowanie wybranej drogi
                if (list_of_selected_city.size() > 1)
                {
                    // rysowanie wybranej drogi
                    PolylineOptions points = new PolylineOptions().width(6).color(Color.BLACK).geodesic(true);

                    mapa.clear();

                    for (int i = 0; i < list_of_selected_city.size(); ++i)
                    {
                        LatLng temp = new LatLng(list_of_selected_city.get(i).geoLength, list_of_selected_city.get(i).geoWidth);
                        points.add(temp);

                        markers = mapa.addMarker(new MarkerOptions()
                                .position(new LatLng(list_of_selected_city.get(i).geoLength, list_of_selected_city.get(i).geoWidth))
                                .title(list_of_selected_city.get(i).city_name));
                    }

                    line = mapa.addPolyline(points);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {}
        });
    }

    public void deleteLine(View view)
    {
        if (list_of_selected_city.size() > 0)
        {
            list_of_selected_city.clear();
            mapa.clear();
        }
    }

    @Override
    public void onMapLoaded() {
        mapa.setOnMapLoadedCallback(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {}
}