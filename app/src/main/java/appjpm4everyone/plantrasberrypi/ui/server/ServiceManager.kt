package appjpm4everyone.plantrasberrypi.ui.server

import appjpm4everyone.plantrasberrypi.ui.server.dogs.GetDogs
import appjpm4everyone.plantrasberrypi.ui.server.rasberrypi.GetRasberryPi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.security.cert.CertificateException

object ServiceManager {
    private val BASE_DOGS_URL: String = "https://dog.ceo/api/breed/"
    private const val TIME_OUT: Long = 45
    //private val BASE_RASBERRY_URL: String = "https://srv-iot.diatel.upm.es/api/v1/lV4IPynzpl9jHJTmIKYo/"
    private val BASE_RASBERRY_URL: String = "https://srv-iot.diatel.upm.es/api/v1/lV4IPynzpl9jHJTmIKYo/"

    private val okHttpClient = HttpLoggingInterceptor().run {

        // Create a trust manager that does not validate certificate chains
        val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate?> {
                        return arrayOfNulls(0)
                    }
                }
            )

        // Install the all-trusting trust manager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())
        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory = sslContext.socketFactory

        level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder().addInterceptor(this)
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .build()
    }

    val serviceDogs = Retrofit.Builder()
        .baseUrl(BASE_DOGS_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .run {
            create(GetDogs::class.java)
        }

    val serviceRasberry = Retrofit.Builder()
        .baseUrl(BASE_RASBERRY_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .run {
            create(GetRasberryPi::class.java)
        }

}