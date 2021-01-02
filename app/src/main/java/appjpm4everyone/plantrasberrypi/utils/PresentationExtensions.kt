package appjpm4everyone.plantrasberrypi.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import appjpm4everyone.plantrasberrypi.PlantRasberryPiApp
import com.squareup.picasso.Picasso

//import com.bumptech.glide.Glide


inline fun <reified T : Activity> Context.intentFor(body: Intent.() -> Unit): Intent {
    return Intent(this, T::class.java).apply(body)
}

inline fun <reified T : Activity> Context.startActivity(body: Intent.() -> Unit) {
    startActivity(intentFor<T>(body))
}

inline fun <reified T : ViewModel> FragmentActivity.getViewModel(): T {
    return ViewModelProviders.of(this)[T::class.java]
}

inline fun <reified T : ViewModel> FragmentActivity.getViewModel(crossinline factory: () -> T): T {
    val vmFactory = object : ViewModelProvider.Factory {
        override fun <U : ViewModel> create(modelClass: Class<U>): U = factory() as U
    }
    return ViewModelProviders.of(this, vmFactory)[T::class.java]
}

inline fun <reified T : ViewModel> Fragment.getViewModel(crossinline factory: () -> T): T {
    val vmFactory = object : ViewModelProvider.Factory {
        override fun <U : ViewModel> create(modelClass: Class<U>): U = factory() as U
    }
    return ViewModelProviders.of(this, vmFactory)[T::class.java]
}

fun <T : ViewDataBinding> ViewGroup.bindingInflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = true): T =
    DataBindingUtil.inflate(LayoutInflater.from(context), layoutRes, this, attachToRoot)

val Context.app: PlantRasberryPiApp get() = applicationContext as PlantRasberryPiApp

/*
fun ImageView.loadImage(image: Int) {
    Glide.with(context).load(image)
        .into(this)
}*/

fun ImageView.fromUrl(url:String){
    Picasso.get().load(url).into(this)
}
