package edu.vt.mobiledev.allmarketstracker

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import edu.vt.mobiledev.allmarketstracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val testVM: TestApiViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Only add the fragment if it's not already there (e.g., rotation)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, CryptoListFragment())
                .commit()
        }

        val testApiButton: Button = findViewById(R.id.test_api_button)
        testApiButton.setOnClickListener {
            // For now, just log something
            Log.d("MainActivity", "Test API button clicked")
        }
    }
}