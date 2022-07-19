package com.imtmobileapps.data.remote

import com.imtmobileapps.model.*
import retrofit2.http.*

interface CryptoApi {

    @GET("managecoins?action=personcmccoins")
    suspend fun getPersonCoins(@Query("person_id") person_id: Int): List<CryptoValue>

    @GET("person")
    suspend fun getStates(): List<State>

    @GET("totals")
    suspend fun getTotals(@Query("person_id") person_id: Int): TotalValues

    @GET("managecoins?action=cmccoins")
    suspend fun getAllCoins() : MutableList<Coin>

    @POST("holdings?action=addholding")
    fun addHolding(@Body coinHolding: CoinHolding) : Holdings

    @POST("holdings?action=deleteholding")
    fun deleteHolding(@Body holdings: Holdings) : Holdings

    @POST("holdings?action=updateholding")
    fun updateHolding(@Body coinHolding: CoinHolding) : Holdings

    @POST("person?action=update")
    suspend fun updatePerson(@Body person : Person) : Person

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("uname") uname: String?,
        @Field("pass") pass: String?
    ): SignUp

    @FormUrlEncoded
    @POST("resetpassword")
    fun resetPassword(
        @Field("email") email: String?
    ): ReturnDTO

    @POST("logout")
    suspend fun logout() : Boolean

    @POST("signup")
    suspend fun signUp(@Body signUp: SignUp): SignUp

    //Stubbed only, we need a new provider
    /* @GET("coinnews")
     fun getCoinNews(@Query("coin_name") coin_name: String): <List<Article>*/

}