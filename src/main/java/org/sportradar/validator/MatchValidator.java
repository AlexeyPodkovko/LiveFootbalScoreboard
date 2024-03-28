package org.sportradar.validator;

import org.sportradar.exception.DuplicateCountryException;
import org.sportradar.exception.InvalidCountryNameException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * The {@code MatchValidator} class is designed to validate country names in the context of sports matches.
 * It employs a singleton pattern to ensure a single instance of the validator, preventing repeated initialization
 * of the country list and avoiding static initialization blocks.
 *
 * <p>This class maintains a set of valid country names derived from ISO country codes, which it uses to
 * validate the names of countries involved in matches. It offers methods for checking if country names are valid
 * and if they are duplicates, throwing specific exceptions in each case.</p>
 *
 * <p>Note: In applications utilizing the Spring Framework, this class's functionality can be significantly
 * simplified by using Spring beans. The singleton pattern here is manually implemented to avoid
 * repeated initialization in non-Spring environments and to illustrate the pattern's usage.</p>
 *
 * @author Alexey Podkovko
 */
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

    /**
     * Returns the single instance of the {@code MatchValidator}, creating it if it does not exist.
     *
     * @return The single instance of {@code MatchValidator}.
     */
    public static MatchValidator getInstance() {
        return LazyHolder.INSTANCE;
    }

    /**
     * Validates the provided country names.
     *
     * @param firstCountry The name of the first country to validate.
     * @param secondCountry The name of the second country to validate.
     * @throws InvalidCountryNameException If a country name is not in the set of valid countries.
     * @throws DuplicateCountryException If the two country names are identical.
     */
    public void validateCountries(final String firstCountry, final String secondCountry) throws InvalidCountryNameException, DuplicateCountryException {
        validateCountriesByName(firstCountry, secondCountry);

        if (firstCountry.equals(secondCountry)) {
            throw new DuplicateCountryException();
        }
    }

    /**
     * Validates each provided country name against the set of valid countries.
     *
     * @param countryNames The country names to validate.
     * @throws InvalidCountryNameException If any country name is not in the set of valid countries.
     */
    private void validateCountriesByName(final String... countryNames) throws InvalidCountryNameException {
        for (String countryName : countryNames) {
            if (!LazyHolder.COUNTRIES.contains(countryName)) {
                throw new InvalidCountryNameException(countryName);
            }
        }
    }

}
