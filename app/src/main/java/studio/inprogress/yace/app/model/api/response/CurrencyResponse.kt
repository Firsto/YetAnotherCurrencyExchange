package studio.inprogress.yace.app.model.api.response

import androidx.databinding.ObservableField
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CurrencyResponse(
    @SerializedName("base")
    @Expose
    val base: String,

    @SerializedName("date")
    @Expose
    val date: String,

    @SerializedName("rates")
    @Expose
    val rates: HashMap<String, Double>,

    var amount: ObservableField<Double> = ObservableField(11.0),
    val rate: Double
) {
    init {
        amount = ObservableField(1.0)
    }
}