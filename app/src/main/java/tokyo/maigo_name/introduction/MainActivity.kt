package tokyo.maigo_name.introduction

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tokyo.maigo_name.introduction.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        TaskActivity.start(this)
    }
}
