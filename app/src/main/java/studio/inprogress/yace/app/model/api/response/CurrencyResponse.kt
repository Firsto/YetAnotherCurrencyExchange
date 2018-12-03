package studio.inprogress.yace.app.model.api.response

import androidx.databinding.Bindable
import androidx.databinding.ObservableDouble
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CurrencyResponse (
    @SerializedName("base")
    @Expose
    val base: String,

    @SerializedName("date")
    @Expose
    val date: String,

    @SerializedName("rates")
    @Expose
    val rates: Map<String, Double>,

    val rate: Double
) {
    @Bindable
    var amount: ObservableDouble = ObservableDouble(1.0)
}