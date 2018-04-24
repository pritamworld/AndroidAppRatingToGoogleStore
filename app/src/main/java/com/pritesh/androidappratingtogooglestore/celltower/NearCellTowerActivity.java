package com.pritesh.androidappratingtogooglestore.celltower;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.pritesh.androidappratingtogooglestore.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//https://www.sitepoint.com/requesting-runtime-permissions-in-android-m-and-n/
//https://stackoverflow.com/questions/13294545/retrieve-cell-towers-information
//https://www.mylnikov.org/archives/1059
//https://www.wilsonamplifiers.com/blog/finding-cell-tower-locations-the-complete-guide/
//http://www.tested.com/tech/android/557-how-to-measure-cell-signal-strength-on-android-phones/
//https://powerfulsignal.com/cell-signal-strength/
//https://www.wilsonamplifiers.com/blog/how-to-read-cell-phone-signal-strength-the-right-way/
//https://www.cellmapper.net/map
//http://www.instructables.com/id/Track-your-location-without-using-GPS-using-LAC-a/
//https://api.mylnikov.org/geolocation/cell?v=1.1&data=open&mcc=268&mnc=06&lac=8280&cellid=5616
//https://developer.android.com/guide/components/intents-common.html

public class NearCellTowerActivity extends AppCompatActivity
{

    static final Integer LOCATION = 0x1;
    static final Integer CALL = 0x2;
    static final Integer WRITE_EXST = 0x3;
    static final Integer READ_EXST = 0x4;
    static final Integer CAMERA = 0x5;
    static final Integer ACCOUNTS = 0x6;
    static final Integer GPS_SETTINGS = 0x7;

    GoogleApiClient client;
    LocationRequest mLocationRequest;
    PendingResult<LocationSettingsResult> result;


    public static JSONArray getCellInfo(Context ctx)
    {
        TelephonyManager tel = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);

        JSONArray cellList = new JSONArray();

        // Type of the network
        int phoneTypeInt = tel.getPhoneType();
        String phoneType = null;
        phoneType = phoneTypeInt == TelephonyManager.PHONE_TYPE_GSM ? "gsm" : phoneType;
        phoneType = phoneTypeInt == TelephonyManager.PHONE_TYPE_CDMA ? "cdma" : phoneType;

        //from Android M up must use getAllCellInfo
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            List<NeighboringCellInfo> neighCells = tel.getNeighboringCellInfo();
            for(int i = 0; i < neighCells.size(); i++)
            {
                try
                {
                    JSONObject cellObj = new JSONObject();
                    NeighboringCellInfo thisCell = neighCells.get(i);
                    cellObj.put("cellId", thisCell.getCid());
                    cellObj.put("lac", thisCell.getLac());
                    cellObj.put("rssi", thisCell.getRssi());
                    cellList.put(cellObj);
                } catch(Exception e)
                {
                }
            }

        } else
        {
            List<CellInfo> infos = tel.getAllCellInfo();
            for(int i = 0; i < infos.size(); ++i)
            {
                try
                {
                    JSONObject cellObj = new JSONObject();
                    CellInfo info = infos.get(i);
                    if(info instanceof CellInfoGsm)
                    {
                        CellSignalStrengthGsm gsm = ((CellInfoGsm) info).getCellSignalStrength();
                        CellIdentityGsm identityGsm = ((CellInfoGsm) info).getCellIdentity();
                        cellObj.put("cellId", identityGsm.getCid());
                        cellObj.put("lac", identityGsm.getLac());
                        cellObj.put("dbm", gsm.getDbm());
                        cellList.put(cellObj);
                    } else if(info instanceof CellInfoLte)
                    {
                        CellSignalStrengthLte lte = ((CellInfoLte) info).getCellSignalStrength();
                        CellIdentityLte identityLte = ((CellInfoLte) info).getCellIdentity();
                        cellObj.put("cellId", identityLte.getCi());
                        cellObj.put("tac", identityLte.getTac());
                        cellObj.put("dbm", lte.getDbm());
                        cellList.put(cellObj);
                    }
                    else if(info instanceof CellInfoWcdma)
                    {
                        CellSignalStrengthWcdma lte = ((CellInfoWcdma) info).getCellSignalStrength();
                        CellIdentityWcdma identityLte = ((CellInfoWcdma) info).getCellIdentity();
                        cellObj.put("cellId", identityLte.getCid());
                        cellObj.put("lac", identityLte.getLac());
                        cellObj.put("mcc", identityLte.getMcc());
                        cellObj.put("mnc", identityLte.getMnc());
                        cellObj.put("dbm", lte.getDbm());
                        cellObj.put("level", lte.getLevel());
                        cellObj.put("asulevel", lte.getAsuLevel());
                        cellList.put(cellObj);
                    }

                } catch(Exception ex)
                {

                }
            }
        }

        return cellList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_cell_tower);

        client = new GoogleApiClient.Builder(this)
                .addApi(AppIndex.API)
                .addApi(LocationServices.API)
                .build();

        ask(0);
        JSONArray output = getCellInfo(NearCellTowerActivity.this);
        Log.d("OUTPUT : ", output.toString());

        getCellTowerInforLocation();

    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        client.disconnect();
    }


    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(NearCellTowerActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(NearCellTowerActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(NearCellTowerActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(NearCellTowerActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    public void ask(int flag){
        switch (flag){
            case 0:
                askForPermission(Manifest.permission.ACCESS_COARSE_LOCATION,LOCATION);
                break;
            case 1:
                askForPermission(Manifest.permission.CALL_PHONE,CALL);
                break;
            case 2:
                askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,WRITE_EXST);
                break;
            case 3:
                askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE,READ_EXST);
                break;
            case 4:
                askForPermission(Manifest.permission.CAMERA,CAMERA);
                break;
            case 5:
                askForPermission(Manifest.permission.GET_ACCOUNTS,ACCOUNTS);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
            switch (requestCode) {
                //Location
                case 1:
                    //askForGPS();
                    JSONArray output = getCellInfo(NearCellTowerActivity.this);
                    Log.d("OUTPUT : ", output.toString());
                    break;
                //Call
                case 2:
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + "{This is a telephone number}"));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        startActivity(callIntent);
                    }
                    break;
                //Write external Storage
                case 3:
                    break;
                //Read External Storage
                case 4:
                    Intent imageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(imageIntent, 11);
                    break;
                //Camera
                case 5:
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, 12);
                    }
                    break;
                //Accounts
                case 6:
                    AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
                    Account[] list = manager.getAccounts();
                    Toast.makeText(this,""+list[0].name,Toast.LENGTH_SHORT).show();
                    for(int i=0; i<list.length;i++){
                        Log.e("Account "+i,""+list[i].name);
                    }
            }

            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void askForGPS(){
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(30 * 1000);
        mLocationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        result = LocationServices.SettingsApi.checkLocationSettings(client, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(NearCellTowerActivity.this, GPS_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    private void getCellTowerInforLocation()
    {
        //https://api.mylnikov.org/geolocation/cell?v=1.1&data=open&mcc=302&mnc=720&lac=60013&cellid=2906766

        /*
            {
                "result": 200,
                "data": {
                    "lat": 43.6497688362,
                    "range": 1684.422,
                    "lon": -79.38952512778,
                    "time": 1516300422
                }
             }
         */
        /*
            {
                "result": 400,
                "data": {},
                "message": 10,
                "desc": "Bad query parameters",
                "time": 1516304965
             }
         */

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.mylnikov.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CellTowerServices service = retrofit.create(CellTowerServices.class);
        Map<String,String>options = new HashMap<>();
        options.put("v","1.1");
        options.put("data","open");
        options.put("mcc","302");
        options.put("mnc","720");
        options.put("lac","60013");
        options.put("cellid","2906766");
        final Call<CellTowerLocation> cellTowerLocation = service.listCellTowerLocation(options);
        cellTowerLocation.enqueue(new Callback<CellTowerLocation>()
        {
            @Override
            public void onResponse(Call<CellTowerLocation> call, Response<CellTowerLocation> response)
            {
                CellTowerLocation cellTowerLocation = response.body();
                Log.d("TOWER-1", cellTowerLocation.toString());
                if(cellTowerLocation.getResult() == 200)
                {
                    Data data = cellTowerLocation.getData();
                    Log.d("TOWER-2", data.toString());
                    //geo:0,0?q=lat,lng(label)
                    //geo:0,0?q=34.99,-106.61(Treasure)
                    showMap(Uri.parse("geo:0,0?q=" + data.getLat() + "," + data.getLon() + "(My Cell Tower Location)"));
                    //geo:latitude,longitude?z=zoom
                    //geo:47.6,-122.3?z=11
                    //showMap(Uri.parse("geo:" + data.getLat() + "," + data.getLon() + "?z=15"));
                }
            }

            @Override
            public void onFailure(Call<CellTowerLocation> call, Throwable t)
            {
                Log.d("TOWER-E", t.toString());
            }
        });
    }

    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}

//Sample Output
/*

[
   {
      "cellId":2906766,
      "lac":60013,
      "dbm":-77,
      "level":4,
      "asulevel":18
   },
   {
      "cellId":2147483647,
      "lac":2147483647,
      "dbm":-71,
      "level":4,
      "asulevel":21
   },
   {
      "cellId":2147483647,
      "lac":2147483647,
      "dbm":-65,
      "level":4,
      "asulevel":24
   },
   {
      "cellId":2147483647,
      "lac":2147483647,
      "dbm":-69,
      "level":4,
      "asulevel":22
   },
   {
      "cellId":2147483647,
      "lac":2147483647,
      "dbm":-71,
      "level":4,
      "asulevel":21
   },
   {
      "cellId":2147483647,
      "lac":2147483647,
      "dbm":-73,
      "level":4,
      "asulevel":20
   }
]
 */

/*
[
   {
      "cellId":2906766,
      "lac":60013,
      "mcc":302,
      "mnc":720,
      "dbm":-75,
      "level":4,
      "asulevel":19
   },
   {
      "cellId":2147483647,
      "lac":2147483647,
      "mcc":2147483647,
      "mnc":2147483647,
      "dbm":-69,
      "level":4,
      "asulevel":22
   },
   {
      "cellId":2147483647,
      "lac":2147483647,
      "mcc":2147483647,
      "mnc":2147483647,
      "dbm":-71,
      "level":4,
      "asulevel":21
   },
   {
      "cellId":2147483647,
      "lac":2147483647,
      "mcc":2147483647,
      "mnc":2147483647,
      "dbm":-71,
      "level":4,
      "asulevel":21
   },
   {
      "cellId":2147483647,
      "lac":2147483647,
      "mcc":2147483647,
      "mnc":2147483647,
      "dbm":-67,
      "level":4,
      "asulevel":23
   },
   {
      "cellId":2147483647,
      "lac":2147483647,
      "mcc":2147483647,
      "mnc":2147483647,
      "dbm":-71,
      "level":4,
      "asulevel":21
   },
   {
      "cellId":2147483647,
      "lac":2147483647,
      "mcc":2147483647,
      "mnc":2147483647,
      "dbm":-71,
      "level":4,
      "asulevel":21
   }
]
 */
