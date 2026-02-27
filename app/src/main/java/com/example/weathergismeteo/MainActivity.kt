package com.example.weathergismeteo // Убедись, что название пакета совпадает с твоим!

import android.content.Intent
import android.content.ActivityNotFoundException
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var citySpinner: Spinner
    private lateinit var periodRadioGroup: RadioGroup
    private lateinit var launchButton: Button

    // Соответствие городов их кодам для Gismeteo
    private val cityCodes = mapOf(
        "Москва" to "weather-moscow-4368",
        "Санкт-Петербург" to "weather-sankt-peterburg-4079",
        "Новосибирск" to "weather-novosibirsk-4690",
        "Екатеринбург" to "weather-yekaterinburg-4517",
        "Казань" to "weather-kazan-4364",
        "Нижний Новгород" to "weather-nizhny-novgorod-4355"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Находим все элементы на экране
        citySpinner = findViewById(R.id.city_spinner)
        periodRadioGroup = findViewById(R.id.period_radio_group)
        launchButton = findViewById(R.id.launch_button)

        // Загружаем города из arrays.xml в Spinner
        val cities = resources.getStringArray(R.array.cities)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        citySpinner.adapter = adapter

        // Обрабатываем нажатие на кнопку
        launchButton.setOnClickListener {
            openGismeteo()
        }
    }

    private fun openGismeteo() {
        // Получаем выбранный город
        val selectedCity = citySpinner.selectedItem.toString()
        val cityCode = cityCodes[selectedCity] ?: "moscow"

        // Получаем выбранный период
        val selectedPeriodId = periodRadioGroup.checkedRadioButtonId
        val periodPath = when (selectedPeriodId) {
            R.id.period_today -> ""
            R.id.period_tomorrow -> "tomorrow"
            R.id.period_3days -> "3-days"
            R.id.period_10days -> "10-days"
            else -> ""
        }

        // Формируем URL
        val url = "https://www.gismeteo.ru/$cityCode/$periodPath/"

        // Создаем Intent для открытия браузера
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                this,
                "На устройстве не найдено приложения для открытия веб-страниц",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}