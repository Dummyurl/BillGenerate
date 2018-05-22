package app.zingo.com.billgenerate.Model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ZingoHotels.com on 03-11-2017.
 */

public class Util {

    private static Retrofit retrofit = null;
    private static final int PERMISSION_RESULT = 1;

    public static Retrofit getClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100,TimeUnit.SECONDS).build();
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://zingotesting.azurewebsites.net/api/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getToken(Context context)
    {
        String email = PreferenceHandler.getInstance(context).getUserName();
        String password = PreferenceHandler.getInstance(context).getPhoneNumber();
        String value = email+":"+password;
        //String st = ByteString.encodeUtf8(value).base64();
        String s = Base64.encodeToString(value.getBytes(),Base64.NO_WRAP);
        System.out.println("Auth=="+"Basic "+s);
        return "Basic "+s;
    }


    public static boolean checkPermissionOfCamera(final Context context, final String permission,String msg)
    {
        if(ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,permission))
            {
                //ActivityCompat.requestPermissions((Activity) context,new String[]{permission},PERMISSION_RESULT);
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("Permission necessary");
                alertBuilder.setMessage(msg);
                alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, PERMISSION_RESULT);
                        System.out.println("true");

                    }
                });
                alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("false");
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
                return false;

            }
            else
            {
                ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, PERMISSION_RESULT);
                return true;
            }
        }
        else
        {
            return true;
        }


    }
}
