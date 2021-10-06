package com.example.coroutinesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import org.json.JSONObject
import java.net.URL
class MainActivity : AppCompatActivity() {
    lateinit var btn:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn=findViewById(R.id.btn)


        runOnUiThread {
            btn.setOnClickListener{
                requestAPI()

            }        }
    }


    private fun disp(str: String) {

        val responseText = findViewById<View>(R.id.tvtxt) as TextView

        responseText.text = str
    }
    private fun requestAPI() {
        // we use Coroutines to fetch the data, then update the Recycler View if the data is valid
        CoroutineScope(IO).launch {
            // we fetch the prices
            val data = async { fetchPrices() }.await()
            // once the data comes back, we populate our Recycler View
            if (data.isNotEmpty()) {
               // getData(data)
                updateAdvie(data)

            } else {
                Log.d("MAIN", "Unable to get data")
            }
        }
    }

    private fun fetchPrices():String {
        // we will use URL.readText() to get our data (https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.io/java.net.-u-r-l/read-text.html)
        // we make a call to the following API: https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/eur.json
        var response = ""
        try {
            response =
                URL("https://api.adviceslip.com/advice").readText()
        } catch (e: Exception) {
            Log.d("MAIN", "ISSUE: $e")
        }
        // our response is saved as a string
        return response
    }
    private suspend fun updateAdvie(data:String)
    {
        withContext(Main)
        {

            val jsonObject = JSONObject(data)
            val advice = jsonObject.getJSONObject("slip").getString("advice")

disp(advice)
        }

    }
}