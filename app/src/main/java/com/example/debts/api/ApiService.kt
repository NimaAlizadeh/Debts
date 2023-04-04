package com.example.debts.api

import com.example.debts.models.entity.CheckModel
import com.example.debts.models.entity.CheckResponse
import com.example.debts.models.entity.NewEntityResponse
import com.example.debts.models.entity.SendRefreshModel
import com.example.debts.database.DebtEntity
import com.example.debts.database.PaymentEntity
import com.example.debts.models.PaymentsWithDebts
import com.example.debts.models.RegisterResponse
import com.example.debts.models.registerBody
import com.example.debts.models.user.UserEntity
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface ApiService
{
    //refresh ................................................................................................................

    @POST("/entity/sync/{userId}")
    suspend fun syncDebts(@Path("userId") id: String, @Body body: List<DebtEntity>): Response<List<DebtEntity>>

    @POST("/payment/sync/{userId}")
    suspend fun syncPayments(@Path("userId") id: String, @Body body: PaymentsWithDebts): Response<List<PaymentEntity>>

    //entity ................................................................................................................

//    @POST("/entity/{userId}")
//    suspend fun insertDebt(@Path("userId") id:String, @Body body: DebtEntity): Response<String>
//
//    @PUT("entity/{entityId}")
//    suspend fun updateDebt(@Path("entityId") id: Int, @Body body: DebtEntity): Response<String>
//
//    @DELETE("entity/:entityId")
//    suspend fun deleteDebt(@Path("entityId") id:Int): Response<String>

    //payment .................................................................................................................

//    @POST("payment/:entityId")
//    suspend fun newPayment(@Path("userId") id:Int, @Body body: PaymentEntity): Response<NewEntityResponse>
//
//    @PUT("payment/:paymentId")
//    suspend fun updatePayment(@Path("paymentId") id: Int, @Body body: DebtEntity): Response<String>
//
//    @DELETE("payment/:paymentId")
//    suspend fun deletePayment(@Path("paymentId") id:Int): Response<String>

    //user .................................................................................................................

    @POST("user")
    suspend fun newUser(@Body body: UserEntity): Response<String> //check again

    @POST("enterUser")
    suspend fun enterUser(): Response<String>

//    @DELETE(":userId")
//    suspend fun deleteUser(@Path("userId") id:Int): Response<String>
//
//    @PUT(":userId")
//    suspend fun updateUser(@Path("userId") id:Int, @Body body: UserEntity): Response<String>
}