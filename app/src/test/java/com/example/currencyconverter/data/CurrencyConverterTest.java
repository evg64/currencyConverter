package com.example.currencyconverter.data;

import com.example.currencyconverter.data.model.CurrencyData;
import com.example.currencyconverter.domain.model.Currency;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Юнит тесты на {@link CurrencyConverter}
 *
 * @author Evgeny Chumak
 **/
public class CurrencyConverterTest {

    private CurrencyConverter mCurrencyConverter;

    @Before
    public void setUp() {
        mCurrencyConverter = new CurrencyConverter();
    }

    @Test
    public void testConvert() {
        // arrange
        String id = "id";
        int numCode = 15;
        String charCode = "charCode";
        long nominal = 10L;
        String name = "Ruble";
        BigDecimal value = BigDecimal.ONE;
        List<CurrencyData> input = Collections.singletonList(new CurrencyData(
                id,
                numCode,
                charCode,
                nominal,
                name,
                value
        ));
        List<Currency> expectedOutput = Collections.singletonList(new Currency(
                id,
                charCode,
                nominal,
                name,
                value
        ));

        // act
        List<Currency> output = mCurrencyConverter.convert(input);

        // assert
        assertThat(output, is(expectedOutput));
    }
}