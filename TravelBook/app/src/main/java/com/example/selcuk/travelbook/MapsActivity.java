package com.example.selcuk.travelbook;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    static SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //setOnMapLongClick metodunun içindekileri çalıştırmak için kullanılır
        mMap.setOnMapLongClickListener(this);

        Intent intent = getIntent();
        String info = intent.getStringExtra("info");
        //eğer MainActivity'den gönderilen info bilgisi 'new' ise yani yeni bir adres eklenecek ise aşağıdaki ekleme adımlarını gerçekleştiriyoruz.
        if (info.matches("new")) {

            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    SharedPreferences sharedPreferences = MapsActivity.this.getSharedPreferences("com.example.selcuk.travelbook", MODE_PRIVATE);
                    //aşağıdaki kod bloğu kullanıcının uygulamayı ilk kez yüklediğinde yer bilgisi almak için kullanılır
                    boolean firstTimeCheck = sharedPreferences.getBoolean("notFirstTime", false);

                    if (!firstTimeCheck) {
                        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 20));
                        System.out.println("location : " + location);
                        sharedPreferences.edit().putBoolean("notFirstTime", true).apply();
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            if (Build.VERSION.SDK_INT >= 23) {
                //kullanıcının izni yoksa
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //izin ver
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                } else {
                    //izin varsa gps sinyalini al
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                    mMap.clear();
                    //son bilinen location'a yönlendir
                    Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (lastLocation != null) {
                        //latitude ve longitude bilgilerine çevirip mMap ile göster.
                        LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 15));
                    }
                }
            } else {
                //izin varsa gps sinyalini al
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                //son bilinen location'a yönlendir
                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastLocation != null) {
                    //latitude ve longitude bilgilerine çevirip mMap ile göster.
                    LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 15));
                }
            }
        }else{
            mMap.clear();
            //MainActivity'den gelen positiona göre MainActivity'den gönderilen location bilgilerini alıyoruz.
            int position=intent.getIntExtra("position",0);
            LatLng location=new LatLng(MainActivity.locations.get(position).latitude,MainActivity.locations.get(position).longitude);
            String placeName=MainActivity.names.get(position);

            mMap.addMarker(new MarkerOptions().title(placeName).position(location));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,18));
        }
    }
    // eğer izin verilmişse bu kod bloğunu çalıştır
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length>0){
            //izin var mı kontrolü
            if(requestCode==1){
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                    //izin varsa gps sinyalini al
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                    Intent intent=getIntent();
                    String info=intent.getStringExtra("info");
                    //eğer ilk defa bir adres eklenecek ise
                    if(info.matches("new")){
                        //son bilinen location'a yönlendir
                        Location lastLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if(lastLocation!=null){
                            LatLng lastUserLocation=new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation,15));
                        }

                    }else{
                        mMap.clear();
                        //MainActivity'den gelen positiona göre MainActivity'den gönderilen location bilgilerini alıyoruz.
                        int position=intent.getIntExtra("position",0);
                        LatLng location=new LatLng(MainActivity.locations.get(position).latitude,MainActivity.locations.get(position).longitude);
                        String placeName=MainActivity.names.get(position);

                        mMap.addMarker(new MarkerOptions().title(placeName).position(location));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,20));
                    }

                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        //tıklanılan yerin koordinatları ile o yere ulaşabilmek için geocoder nesnesi türetiyoruz.
        Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
        String address="";
        try {
            //tıklanılan yerin koordinatlarını geocoder nesnesi ile alıp addresList'e atıyoruz.
            List<Address> addressList=geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            //eğer addressList içerisinde bir adres var ise bu adresin cadde ismi ile sokak ismini alıyoruz.
            if(addressList!=null&&addressList.size()>0){
                if(addressList.get(0).getThoroughfare()!=null){
                    address+=addressList.get(0).getThoroughfare();

                    if(addressList.get(0).getSubThoroughfare()!=null){
                        address+=addressList.get(0).getSubThoroughfare();
                    }
                }
            }else{
                address="New Place";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //veritabanına eklediğimiz bilgiler aynı zamanda listview'a da uygulamadan çıkılmadan yansıtılmasını sağlayan kod.
        MainActivity.names.add(address);
        MainActivity.locations.add(latLng);
        // yeni eklenenen bilgileri listview'da güncelle
        MainActivity.arrayAdapter.notifyDataSetChanged();

        mMap.addMarker(new MarkerOptions().title(address).position(latLng));
        Toast.makeText(getApplicationContext(),"New Place Created!..",Toast.LENGTH_SHORT).show();

        try{
            //onMapLongClick metodundan gelen latLng nesnesinden latitude ve longitude bilgilerini alıyoruz
            Double l1=latLng.latitude;
            Double l2=latLng.longitude;

            //veritabanına yazdırabilmek için stringe çevrilmesi işlemi
            String coord1=l1.toString();
            String coord2=l2.toString();

            //SQLite database tanımlamalarını yapıyoruz
            database=this.openOrCreateDatabase("Places",MODE_PRIVATE,null);
            database.execSQL("create table if not exists places(name varchar,latitude varchar,longitude varchar)");

            //SQLite database veri girişlerini yapıyoruz
            String toCompile="insert into places (name,latitude,longitude) values (?,?,?)";
            SQLiteStatement sqLiteStatement=database.compileStatement(toCompile);
            sqLiteStatement.bindString(1,address);
            sqLiteStatement.bindString(2,coord1);
            sqLiteStatement.bindString(3,coord2);
            sqLiteStatement.execute();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
