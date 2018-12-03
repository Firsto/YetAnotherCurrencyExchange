package studio.inprogress.yace.app.model

import rx.Observable
import studio.inprogress.yace.app.model.api.CurrenciesApi
import studio.inprogress.yace.app.model.api.response.CurrencyResponse

class CurrencyDataManager(private val api: CurrenciesApi) {

    fun getCurrencies(base: String?): Observable<CurrencyResponse> {
        return api.getCurrencies(base)
    }
}
