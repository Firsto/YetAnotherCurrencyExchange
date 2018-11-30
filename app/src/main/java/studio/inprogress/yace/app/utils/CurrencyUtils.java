package studio.inprogress.yace.app.utils;

import com.neovisionaries.i18n.CurrencyCode;

import java.util.*;

public class CurrencyUtils {
    public static SortedMap<Currency, Locale> currencyLocaleMap;

    static {
        currencyLocaleMap = new TreeMap<Currency, Locale>(new Comparator<Currency>() {
            public int compare(Currency c1, Currency c2) {
                return c1.getCurrencyCode().compareTo(c2.getCurrencyCode());
            }
        });
        for (Locale locale : Locale.getAvailableLocales()) {
            try {
                Currency currency = Currency.getInstance(locale);

                currencyLocaleMap.put(currency, locale);
            } catch (Exception e) {
            }
        }
    }


    public static String getCurrencySymbol(String currencyCode) {
        Currency currency = Currency.getInstance(currencyCode);
        System.out.println(currencyCode + ":-" + currency.getSymbol(currencyLocaleMap.get(currency)));
        return currency.getSymbol(currencyLocaleMap.get(currency));
    }

    public static String getCountryCodeFromCurrencyCode(String currencyCode) {
        try {
            // Get a CurrencyCode instance.
            CurrencyCode currency = CurrencyCode.getByCode(currencyCode);
            if (currency == null) {
                // The code is invalid.
                return null;
            } else {
                String countryName = currency.getCountryList().get(0).name();

                for (Locale locale : Locale.getAvailableLocales()) {
                    String localeString = locale.toString();
                    if (countryName.equalsIgnoreCase(localeString)) return localeString;
                }

                return "us";
            }
        } catch (Exception e) {
            return "us";
        }
    }
}
