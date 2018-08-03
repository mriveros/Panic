package com.panic.root.panic;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.view.View;
import android.Manifest;

public class MainActivity extends AppCompatActivity {
    private LocationManager locManager;
    private LocationListener locListener;
    private String IMEI, Latitud, Longitud, Precision, NUMERO;
    Location currentLocation;
    double currentLatitude, currentLongitude;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_STATE}, 225);

        }
        TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        IMEI = mngr.getDeviceId();
        //obtener el numero de teléfono
        NUMERO = mngr.getSimSerialNumber();

        comenzarLocalizacion();


    }




    private void comenzarLocalizacion() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Requiere permisos para Android 6.0
            Log.e("Location", "No se tienen permisos necesarios!, se requieren.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 225);
            return;
        }else{
            Log.i("Location", "Permisos necesarios OK!.");

        }
        //Obtenemos una referencia al LocationManager
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Obtenemos la última posición conocida

        Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        //Mostramos la última posición conocida
        mostrarPosicion(loc);


        //Nos registramos para recibir actualizaciones de la posición
        locListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                mostrarPosicion(location);

            }

            public void onProviderDisabled(String provider) {
                Intent i = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);

                //lblEstado.setText("Provider OFF");
            }

            public void onProviderEnabled(String provider) {
                //lblEstado.setText("Provider ON ");
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.i("", "Provider Status: " + status);
                //lblEstado.setText("Provider Status: " + status);
            }

        };


        locManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 30000, 0, locListener);

    }

    private void mostrarPosicion(Location loc) {
        if (loc == null) {

            //lblLatitud.setText("Latitud: (sin_datos)");
            //lblLongitud.setText("Longitud: (sin_datos)");
            //lblPrecision.setText("Precision: (sin_datos)");

        } else {
            Log.i("", String.valueOf(loc.getLatitude() + " - " + String.valueOf(loc.getLongitude())));
            //aca debemos calcular todos los elementos que debemos pasar al siguiente activity
            Latitud = String.valueOf(loc.getLatitude());
            Longitud = String.valueOf(loc.getLongitude());
            Precision = String.valueOf(loc.getAccuracy());
            Toast.makeText(getBaseContext(), Latitud + ' ' + Longitud + ' ' + Precision, Toast.LENGTH_SHORT).show();

        }

    }

    public void enviarPeligro(View button) {
        if (Latitud == null || Longitud == null) {
            Toast.makeText(getBaseContext(), "Buscando Posición..Intente de Nuevo", Toast.LENGTH_SHORT).show();
            comenzarLocalizacion();
        } else {
            //Cargar la posicion y pasarlo al siguiente activity
            Intent inten = new Intent(MainActivity.this, EnvioPanicActivity.class);
            inten.putExtra("latitud", Latitud);
            inten.putExtra("longitud", Longitud);
            inten.putExtra("precision", Precision);
            inten.putExtra("imei", IMEI);
            startActivity(inten);
        }


    }

    public void enviarSalud(View button) {

        if (Latitud == null || Longitud == null) {
            Toast.makeText(getBaseContext(), "Buscando Posición..Intente de Nuevo", Toast.LENGTH_SHORT).show();

        } else {
            //Cargar la posicion y pasarlo al siguiente activity
            Intent inten = new Intent(MainActivity.this, EnvioSaludActivity.class);
            inten.putExtra("latitud", Latitud);
            inten.putExtra("longitud", Longitud);
            inten.putExtra("precision", Precision);
            inten.putExtra("imei", IMEI);
            startActivity(inten);
        }
    }




    void updateLocation(Location location) {
        currentLocation = location;
        currentLatitude = currentLocation.getLatitude();
        currentLongitude = currentLocation.getLongitude();
        Toast.makeText(getBaseContext(), currentLatitude + ' ' + currentLongitude + ' ' + Precision, Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_principal, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent inten = new Intent(MainActivity.this, Customer.class);
            startActivity(inten);
            Toast.makeText(getBaseContext(), "Configuraciones..", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String getPhoneNumber() {
        TelephonyManager mTelephonyManager;
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.READ_SMS}, 225);

        }
        return mTelephonyManager.getLine1Number();
    }

}
