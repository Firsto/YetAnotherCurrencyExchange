package studio.inprogress.yace.app.model.api

import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable
import studio.inprogress.yace.app.model.api.response.CurrencyResponse

interface CurrenciesApi {

    @GET("/latest")
    fun getCurrencies(@Query("base") base: String?): Observable<CurrencyResponse>

}
