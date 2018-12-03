package studio.inprogress.yace.app.utils;

import android.widget.EditText;
import android.widget.TextView;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;

import java.text.DecimalFormat;

public class Converter {

    @BindingAdapter("android:text")
    public static void setDouble(EditText view, double value) {
        if (Double.isNaN(value)) view.setText("");
        else {
            int cursorPosition = view.getSelectionStart();
            DecimalFormat df = new DecimalFormat("#.##");
            String text = df.format(value);
            view.setText(text);
            view.setSelection(text.length() > cursorPosition ? cursorPosition : text.length());
        }
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static double getDouble(TextView view) {
        String num = view.getText().toString();
        if (num.isEmpty()) return 0.0;
        try {
            return Double.parseDouble(num);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}