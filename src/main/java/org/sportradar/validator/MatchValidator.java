package org.sportradar.validator;

import org.sportradar.exception.DuplicateCountryException;
import org.sportradar.exception.InvalidCountryNameException;

import java.util.*;

public class MatchValidator {

    private static class LazyHolder {
        private static final Set<String> COUNTRIES = initCountries();

        private static final MatchValidator INSTANCE = new MatchValidator();

        private static Set<String> initCountries() {
            Set<String> countries = new HashSet<>();
            for (String countryCode : Locale.getISOCountries()) {
                Locale locale = new Locale("", countryCode);
                countries.add(locale.getDisplayCountry(Locale.ENGLISH));
            }
            return Collections.unmodifiableSet(countries);
        }
    }

    private MatchValidator() {
    }

    public static MatchValidator getInstance() {
        return LazyHolder.INSTANCE;
    }

    public void validateCountries(final String firstCountry, final String secondCountry) throws InvalidCountryNameException, DuplicateCountryException {
        validateCountriesByName(firstCountry, secondCountry);

        if (firstCountry.equals(secondCountry)) {
            throw new DuplicateCountryException();
        }
    }

    private void validateCountriesByName(final String... countryNames) throws InvalidCountryNameException {
        for (String countryName : countryNames) {
            if (!LazyHolder.COUNTRIES.contains(countryName)) {
                throw new InvalidCountryNameException(countryName);
            }
        }
    }

}
