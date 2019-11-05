package com.example.currencyconverter.data;

import androidx.annotation.NonNull;

import com.example.currencyconverter.data.model.CurrencyData;
import com.example.currencyconverter.domain.IConverter;
import com.example.currencyconverter.domain.model.Currency;

import java.util.ArrayList;
import java.util.List;

/**
 * Конвертирует список валют из data сущностей в domain
 *
 * @author Evgeny Chumak
 **/
public class CurrencyConverter
        implements IConverter<List<CurrencyData>, List<Currency>> {

    @NonNull
    @Override
    public List<Currency> convert(@NonNull List<CurrencyData> currencies) {
        List<Currency> result = new ArrayList<>();
        for (CurrencyData currency : currencies) {
            result.add(new Currency(
                    currency.getId(),
                    currency.getCharCode(),
                    currency.getNominal(),
                    currency.getName(),
                    currency.getValue()
            ));
        }
        return result;
    }
}
