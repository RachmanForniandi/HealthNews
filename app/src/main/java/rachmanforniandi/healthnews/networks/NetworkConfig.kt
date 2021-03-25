package rachmanforniandi.healthnews.networks

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import rachmanforniandi.healthnews.BuildConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkConfig {

    fun receivedInterceptor(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()
        return okHttpClient
    }

    fun useRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.URL_SERVICE)
            .client(receivedInterceptor())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    fun useServiceApi() = useRetrofit().create(APIService::class.java)
}