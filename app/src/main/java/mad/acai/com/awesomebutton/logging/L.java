package mad.acai.com.awesomebutton.logging;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Andy on 22/04/2016.
 */
public class L {

    public static void m(String message) {
        Log.d("ANDY", "" + message);
    }

    public static void t(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
