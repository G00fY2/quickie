package com.g00fy2.quickiesample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.g00fy2.quickie.QuickieScan
import com.g00fy2.quickiesample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    binding.buttonQrScanner.setOnClickListener {
      registerForActivityResult(QuickieScan()) { result ->
        Toast.makeText(this, result ?: "Canceled", Toast.LENGTH_SHORT).show()
      }.launch(null)
    }
  }
}