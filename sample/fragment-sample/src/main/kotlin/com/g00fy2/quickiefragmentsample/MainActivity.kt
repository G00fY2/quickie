package com.g00fy2.quickiefragmentsample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.g00fy2.quickiefragmentsample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(ActivityMainBinding.inflate(layoutInflater).root)
  }
}