package com.medco.Travel.insurance.serviceImpl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medco.Travel.insurance.entity.ExchangeRate;
import com.medco.Travel.insurance.repository.ExchangeRateRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class Cur {
    @JsonProperty("CurrencyCode")
    String currencyCode;
}

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class Rate {
    float cashSelling;
    Cur currency;
}

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class Currency {
    String _id;
    String published_at;

    @JsonProperty("ExchangeRate")
    List<Rate> exchangeRate;

}


@Service
public class ExchangeRateService {

    private static final String URL = "https://combanketh.et/cbeapi/daily-exchange-rates?_limit=1&_sort=Date%3ADESC&csrt=133792713376438398";

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    public double getEuroToBirrRate() {

        cleanupOldRates();

        Optional<ExchangeRate> latestRate = exchangeRateRepository.findTopByOrderByLastUpdatedDesc();

        if (latestRate.isPresent() && isRateValid(latestRate.get().getLastUpdated())) {
            return latestRate.get().getEuroToBirrRate();
        }

        float newRate = fetchEuroToBirrRateFromWebsite();

        saveExchangeRate(newRate);

        return newRate;

    }

    private boolean isRateValid(LocalDateTime lastUpdated) {
        // Check if the rate is within the last 24 hours
        return lastUpdated.isAfter(LocalDateTime.now().minusDays(1));
    }

    private void cleanupOldRates() {

        exchangeRateRepository.deleteAllByLastUpdatedBefore(LocalDateTime.now().minusDays(1));
    }

    private float fetchEuroToBirrRateFromWebsite() {
        try {
            Connection.Response response = Jsoup.connect(URL).ignoreContentType(true).method(Connection.Method.GET).execute();

            ObjectMapper objectMapper = new ObjectMapper();
            List<Currency> currencies = objectMapper.readValue(response.body(), new TypeReference<List<Currency>>() {});

            for (Rate rate : currencies.get(0).getExchangeRate()) {
                if (rate.getCurrency() != null && "EUR".equals(rate.getCurrency().getCurrencyCode())) {
                    System.out.println("EUR to Birr Cash Selling Rate: " + rate.getCashSelling());
                    return rate.getCashSelling();
                }
            }

            throw new RuntimeException("EUR to Birr exchange rate not found");

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch the exchange rate", e);
        }
    }

    private void saveExchangeRate(float rate) {
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setEuroToBirrRate(rate);
        exchangeRate.setLastUpdated(LocalDateTime.now());
        exchangeRateRepository.save(exchangeRate);
    }

}


