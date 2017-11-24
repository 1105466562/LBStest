package com.example.lbstest;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Mainview extends AppCompatActivity {

    public LocationClient mLocationClient;
    private TextView positionText;
    private MapView mapView;
    private BaiduMap baiduMap;
    private int isFirstLocte = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        super.onCreate(savedInstanceState);

        ActionBar actionbar=getSupportActionBar();//取消标题栏
        if (actionbar!=null){
            actionbar.hide();
        }

        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListner());
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_mainview);//初始化要在这条语句之前
        positionText = (TextView) findViewById(R.id.position_text_view);
        mapView = (MapView) findViewById(R.id.bmapView);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        UiSettings mUiSettings = baiduMap.getUiSettings();//
        mUiSettings.setCompassEnabled(false);//
        mapView. showScaleControl(false);//比例尺
        baiduMap.showMapPoi(false);//底图标注



        List<String> permissionList = new ArrayList<>();//申请权限
        if (ContextCompat.checkSelfPermission(Mainview.this, android.Manifest.
                permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(Mainview.this, android.Manifest.
                permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(Mainview.this, android.Manifest.
                permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(Mainview.this, permissions, 1);
        } else {
            requestLocation();
        }



    }

    private void navigateTo(BDLocation location){
        if (isFirstLocte==0){//只有第一次用才移动位置
            LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());//获取经纬度
            MapStatusUpdate update_navi = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.setMapStatus(update_navi);
            isFirstLocte = 1;
        }
    }
    private void zoomTo(BDLocation location){
        if (isFirstLocte==1){//只有第一次用才设置缩放
            MapStatusUpdate update_zoom = MapStatusUpdateFactory.zoomTo(25f);//浮点数
            baiduMap.animateMapStatus(update_zoom);//设定缩放级别
            isFirstLocte = -1;
        }
    }

    private void requestLocation(){
        initLocation();//更新
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(3000);//3秒更新一次位置
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        mLocationClient.setLocOption(option);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mapView.onResume();
    }
    @Override
    protected void onPause(){
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mLocationClient.stop();//活动结束时，关闭定位
        mapView.onDestroy();//活动结束，关闭地图
        baiduMap.setMyLocationEnabled(false);//关闭图标
    }

    protected void setMarker(double latitude,double longitude){
        LatLng point = new LatLng(latitude,longitude);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));

//在地图上添加Marker，并显示

        baiduMap.addOverlay(option);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if (grantResults.length>0){
                    for (int result:grantResults){
                        if (result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"必须同意所有权限才能使用本程序",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                }else {
                    Toast.makeText(this,"发生未知错误",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
    public class MyLocationListner implements BDLocationListener{
        @Override//获取位置
        public void onReceiveLocation(BDLocation location){
            StringBuilder currentPosition = new StringBuilder();
            currentPosition.append("纬度：").append(location.getLatitude()).append("\n");
            currentPosition.append("经度：").append(location.getLongitude()).append("\n");
            currentPosition.append("国家：").append(location.getCountry()).append("\n");
            currentPosition.append("省：").append(location.getProvince()).append("\n");
            currentPosition.append("市：").append(location.getCity()).append("\n");
            currentPosition.append("区：").append(location.getDistrict()).append("\n");
            currentPosition.append("街道：").append(location.getStreet()).append("\n");
            currentPosition.append("定位方式");
            if (location.getLocType()==BDLocation.TypeGpsLocation){
                currentPosition.append("GPS");
            }else if (location.getLocType()==BDLocation.TypeNetWorkLocation){
                currentPosition.append("网络");
            }
            positionText.setText(currentPosition);


            if (location.getLocType() == BDLocation.TypeGpsLocation||
                    location.getLocType() == BDLocation.TypeNetWorkLocation){
                zoomTo(location);
                navigateTo(location);
            }

            MyLocationData.Builder locationBuilder = new MyLocationData.Builder();//动态标记当前位置
            locationBuilder.latitude(location.getLatitude());
            locationBuilder.longitude(location.getLongitude());
            MyLocationData locationData = locationBuilder.build();
            baiduMap.setMyLocationData(locationData);

            setMarker(location.getLatitude(),location.getLongitude());

        }

    }


}

