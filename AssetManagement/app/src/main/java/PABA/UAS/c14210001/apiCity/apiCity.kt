package PABA.UAS.c14210001.apiCity

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface apiCity {
    @Headers("key: 57271e94be6778d8dcbba41c1f41856f")

    @GET("city")
    fun getAllCity() : Call<DcResponseCity>
}