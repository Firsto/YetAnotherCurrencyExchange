package studio.inprogress.yace.app.model

import rx.Single
import studio.inprogress.yace.app.model.api.CurrenciesApi
import studio.inprogress.yace.app.model.api.response.CurrencyResponse

class CurrencyDataManager(private val api: CurrenciesApi) {

    fun getCurrencies(base: String?): Single<CurrencyResponse> {
        return api.getCurrencies(base)
    }
}
