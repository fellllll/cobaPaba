package PABA.UAS.c14210001.apiCostService

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface apiCost {
    @Headers("key: 57271e94be6778d8dcbba41c1f41856f")

    @POST("cost")
    fun getCost(
        @Body request: costModel
    ) : Call<DcResponseCost>
}