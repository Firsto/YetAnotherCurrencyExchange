package studio.inprogress.yace.app.di.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import studio.inprogress.yace.app.BuildConfig;
import timber.log.Timber;

import java.util.concurrent.TimeUnit;

@Module
public class NetworkModule {

    @Provides
    public HttpLoggingInterceptor loggingInterceptor() {
        return new HttpLoggingInterceptor(message -> Timber.i(message)).setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @Provides
    public OkHttpClient okHttpClient(HttpLoggingInterceptor interceptor) {
        return new OkHttpClient().newBuilder()
                .addNetworkInterceptor(interceptor)
                .addInterceptor(chain -> {
                    Response response = chain.proceed(chain.request());
                    return response;
                })
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

    @Provides
    public Retrofit provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .baseUrl(BuildConfig.API_ENDPOINT)
                .build();
    }

    private Gson getGson() {
        GsonBuilder builder = new GsonBuilder();
        return builder.excludeFieldsWithoutExposeAnnotation().create();
    }
}
