package studio.inprogress.yace.app.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.jwang123.flagkit.FlagKit;

public class PicturesUtility {
    public static Drawable getCountryImage(Context context,  String country) {
        try {
            return FlagKit.drawableWithFlag(context, country);
        } catch (Exception e) {
            e.printStackTrace();
            return FlagKit.drawableWithFlag(context, "us");
        }
    }
}
