package appjpm4everyone.plantrasberrypi.ui.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import appjpm4everyone.plantrasberrypi.R
import appjpm4everyone.plantrasberrypi.databinding.ActivitySplashBinding
import appjpm4everyone.plantrasberrypi.ui.main.MainActivity
import appjpm4everyone.plantrasberrypi.utils.EventObserver
import appjpm4everyone.plantrasberrypi.utils.getViewModel
import appjpm4everyone.plantrasberrypi.utils.startActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var viewModel: SplashViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_splash)

        val binding: ActivitySplashBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_splash)

        viewModel = getViewModel { SplashViewModel() }

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.navigateToLogin.observe(
                this, EventObserver {
            startActivity<MainActivity> {  }
            finish()
        }
        )
    }
}