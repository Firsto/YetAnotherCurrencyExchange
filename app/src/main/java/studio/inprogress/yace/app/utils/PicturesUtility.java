package studio.inprogress.yace.app.utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

public class PicturesUtility {
    public static Drawable getCountryImage(Context context, String currency) {

        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), BitmapFactory.decodeResource(context.getResources(), context.getResources().getIdentifier(
                "flag_" + currency.toLowerCase(), "drawable", context.getPackageName()), null));

        roundedBitmapDrawable.setCircular(true);
        return roundedBitmapDrawable;
    }
}
