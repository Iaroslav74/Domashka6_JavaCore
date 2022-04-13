package Domashka_5_6_JavaCore;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public interface WeatherModel {
    @NotNull
    Object NOW = null;
    @NotNull
    Object FIVE_DAYS = null;

    void getWeather(String selectedCity, Period period) throws IOException;

    public List<Weather> getSavedToDBWeather();
}