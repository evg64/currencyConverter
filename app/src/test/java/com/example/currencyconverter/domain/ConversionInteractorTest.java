package com.example.currencyconverter.domain;

import androidx.annotation.Nullable;

import com.example.currencyconverter.R;
import com.example.currencyconverter.domain.model.Currency;
import com.example.currencyconverter.presentation.utils.IResourceWrapper;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Юнит тесты на {@link ConversionInteractor}
 *
 * @author Evgeny Chumak
 **/
public class ConversionInteractorTest {

    private ConversionInteractor mConversionInteractor;
    private IResourceWrapper mResourceWrapper;
    private String mCharCode1 = "charCode1";
    private BigDecimal mValue1 = BigDecimal.ONE;
    private String mCharCode2 = "charCode2";
    private BigDecimal mValue2 = BigDecimal.TEN;
    private List<Currency> mCurrencies;

    @Before
    public void setUp() {
        mResourceWrapper = mock(IResourceWrapper.class);
        mConversionInteractor = new ConversionInteractor(mResourceWrapper);
        String id1 = "id1";
        long nominal1 = 10L;
        String name1 = "name1";
        String id2 = "id2";
        long nominal2 = 20L;
        String name2 = "name2";
        mCurrencies = Arrays.asList(
                new Currency(
                        id1,
                        mCharCode1,
                        nominal1,
                        name1,
                        mValue1
                ),
                new Currency(
                        id2,
                        mCharCode2,
                        nominal2,
                        name2,
                        mValue2
                )
        );
    }

    @Test
    public void testFormatConversionRate() {
        testFormatConversionRate(null, 0, 0, null);
        testFormatConversionRate(new ArrayList<>(), 0, 0, null);
        testFormatConversionRate(mCurrencies, 4, 6, null);

        String expectedResult = "expectedResult";
        when(mResourceWrapper.getString(R.string.conversion_rate, "0.2", mCharCode1, mCharCode2))
                .thenReturn(expectedResult);
        testFormatConversionRate(mCurrencies, 0, 1, expectedResult);
    }

    @Test
    public void testConvert() {
        testConvert(null, 0, 0, "10", null);
        testConvert(new ArrayList<>(), 0, 0, "10", null);
        testConvert(mCurrencies, 4, 6, "10", null);
        testConvert(mCurrencies, 0, 1, "incorrect input", null);

        String expectedResult = "expectedResult";
        when(mResourceWrapper.getString(R.string.you_will_get, "2", mCharCode2))
                .thenReturn(expectedResult);
        testConvert(mCurrencies, 0, 1, "10", expectedResult);
    }

    private void testFormatConversionRate(
            @Nullable List<Currency> currencies,
            int fromCurrencyWithIndex,
            int toCurrencyWithIndex,
            @Nullable String expectedOutput) {
        // act
        String output = mConversionInteractor.formatConversionRate(currencies, fromCurrencyWithIndex, toCurrencyWithIndex);

        // assert
        assertThat(output, is(expectedOutput));
    }

    private void testConvert(@Nullable List<Currency> currencies,
                             int fromCurrencyWithIndex,
                             int toCurrencyWithIndex,
                             @Nullable String amount,
                             @Nullable String expectedOutput) {
        // act
        String output = mConversionInteractor.convert(currencies,
                fromCurrencyWithIndex,
                toCurrencyWithIndex,
                amount);

        // assert
        assertThat(output, is(expectedOutput));
    }
}
