package Domashka_5_6_JavaCore

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import javafx.util.Builder as Builder1


class AccuweatherModel : WeatherModel {
    private val dataBaseRepository = DataBaseRepository()

    @Throws(IOException::class)
    override fun getWeather(selectedCity: String, period: Period) {
        when (period) {
            Period.NOW -> {
                val httpUrl: HttpUrl = Builder1()
                        .scheme(PROTOKOL)
                        .host(BASE_HOST)
                        .addPathSegment(FORECASTS)
                        .addPathSegment(VERSION)
                        .addPathSegment(DAILY)
                        .addPathSegment(ONE_DAY)
                        .addPathSegment(detectCityKey(selectedCity))
                        .addQueryParameter(API_KEY_QUERY_PARAM, API_KEY)
                        .build()
                val request: Request = Builder1()
                        .url(httpUrl)
                        .build()
                val oneDayForecastResponse = okHttpClient.newCall(request).execute()
                val weatherResponse = oneDayForecastResponse.body!!.string()
                println(weatherResponse)
            }
            Period.FIVE_DAYS -> {
            }
        }
    }

    override fun getSavedToDBWeather(): List<Weather> {
        return dataBaseRepository.savedToDBWeather
    }

    @Throws(IOException::class)
    private fun detectCityKey(selectCity: String): String {
        //http://dataservice.accuweather.com/locations/v1/cities/autocomplete
        val httpUrl: HttpUrl = Builder1()
                .scheme(PROTOKOL)
                .host(BASE_HOST)
                .addPathSegment(LOCATIONS)
                .addPathSegment(VERSION)
                .addPathSegment(CITIES)
                .addPathSegment(AUTOCOMPLETE)
                .addQueryParameter(API_KEY_QUERY_PARAM, API_KEY)
                .addQueryParameter("q", selectCity)
                .build()
        val request: Request = Builder1()
                .url(httpUrl)
                .get()
                .addHeader("accept", "application/json")
                .build()
        val response = okHttpClient.newCall(request).execute()
        val responseString = response.body!!.string()
        return objectMapper.readTree(responseString)[0].at("/Key").asText()
    }

    companion object {
        //http://dataservice.accuweather.com/forecasts/v1/daily/1day/349727
        private const val PROTOKOL = "https"
        private const val BASE_HOST = "dataservice.accuweather.com"
        private const val FORECASTS = "forecasts"
        private const val VERSION = "v1"
        private const val DAILY = "daily"
        private const val ONE_DAY = "1day"
        private const val API_KEY = "pXJd8MokcZCdrd2MsoGl2DBZAyCa0zvv"
        private const val API_KEY_QUERY_PARAM = "apikey"
        private const val LOCATIONS = "locations"
        private const val CITIES = "cities"
        private const val AUTOCOMPLETE = "autocomplete"
        private val okHttpClient = OkHttpClient()
        private val objectMapper = ObjectMapper()
    }
}