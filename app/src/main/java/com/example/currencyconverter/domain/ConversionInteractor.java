package com.example.currencyconverter.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.currencyconverter.R;
import com.example.currencyconverter.domain.model.Currency;
import com.example.currencyconverter.presentation.utils.IResourceWrapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

/**
 * Интерактор, отвечающий за операции, связанные с конвертацией валют
 *
 * @author Evgeny Chumak
 **/
public class ConversionInteractor {

    /**
     * Точность при расчётах (знаков после запятой)
     */
    private static final int INTERNAL_SCALE = 5;
    /**
     * Точность при форматировании результата (знаков после запятой)
     */
    private static final int PUBLIC_SCALE = 2;
    private final IResourceWrapper mResourceWrapper;
    private final NumberFormat mNumberFormat = new DecimalFormat("#.##");
    private final NumberFormat mCurrencyFormat = new DecimalFormat("#.#####");

    public ConversionInteractor(@NonNull IResourceWrapper resourceWrapper) {
        mResourceWrapper = resourceWrapper;
    }

    /**
     * Возвращает строку вида "64.24 USD/RUB". Первую валюту использует как базовую (ставит слева), вторую -
     * как котируемую (ставит справа). Соответствующим образом подсчитывает курс конверсии
     *
     * @param currencies            все имеющиеся валюты
     * @param fromCurrencyWithIndex индекс валюты слева (в переданном списке)
     * @param toCurrencyWithIndex   индекс валюты справа (в переданном списке)
     * @return {@code null}, если произошла ошибка, иначе форматированная строка с курсом валютной пары
     */
    @Nullable
    public String formatConversionRate(@Nullable List<Currency> currencies, int fromCurrencyWithIndex, int toCurrencyWithIndex) {
        if (currencies == null ||
                currencies.isEmpty() ||
                currencies.size() <= Math.max(fromCurrencyWithIndex, toCurrencyWithIndex)) {
            return null;
        }
        Currency base = currencies.get(fromCurrencyWithIndex);
        Currency quoted = currencies.get(toCurrencyWithIndex);
        BigDecimal rate = base.getValue()
                .multiply(new BigDecimal(quoted.getNominal()))
                .divide(quoted.getValue(), INTERNAL_SCALE, RoundingMode.HALF_UP)
                .divide(new BigDecimal(base.getNominal()), INTERNAL_SCALE, RoundingMode.HALF_UP);
        String formattedRate = mCurrencyFormat.format(rate);
        return mResourceWrapper.getString(R.string.conversion_rate, formattedRate, base.getCharCode(), quoted.getCharCode());
    }

    /**
     * Форматирует строку с суммой в котируемой валюте, которую можно приобрести за заданное количество базовой валюты
     *
     * @param currencies            список всех валют
     * @param fromCurrencyWithIndex базовая валюта
     * @param toCurrencyWithIndex   котируемая валюта
     * @param amount                количество базовой валюты
     * @return форматированная строка или {@code null} в случае ошибки
     */
    @Nullable
    public String convert(@Nullable List<Currency> currencies,
                          int fromCurrencyWithIndex,
                          int toCurrencyWithIndex,
                          @Nullable String amount) {
        BigDecimal parsedAmount = tryParseAmount(amount);
        if (currencies == null ||
                currencies.isEmpty() ||
                currencies.size() <= Math.max(fromCurrencyWithIndex, toCurrencyWithIndex) ||
                parsedAmount == null) {
            return null;
        }
        Currency base = currencies.get(fromCurrencyWithIndex);
        Currency quoted = currencies.get(toCurrencyWithIndex);
        BigDecimal result = parsedAmount
                .multiply(base.getValue())
                .multiply(new BigDecimal(quoted.getNominal()))
                .divide(quoted.getValue(), INTERNAL_SCALE, RoundingMode.HALF_UP)
                .divide(new BigDecimal(base.getNominal()), INTERNAL_SCALE, RoundingMode.HALF_UP);
        try {
            String formattedResult = mNumberFormat.format(result.setScale(PUBLIC_SCALE, RoundingMode.HALF_UP));
            return mResourceWrapper.getString(R.string.you_will_get, formattedResult, quoted.getCharCode());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Конвертирует введённую пользователем сумму в {@link BigDecimal}
     *
     * @param amount сумма, введённая пользователем
     * @return {@code null} означает некорректный ввод
     */
    @Nullable
    private BigDecimal tryParseAmount(@Nullable String amount) {
        BigDecimal result;
        if (amount == null) {
            result = null;
        } else {
            try {
                result = new BigDecimal(amount);
            } catch (NumberFormatException e) {
                result = null;
            }
        }
        return result;
    }
}
