package rachmanforniandi.healthnews.networks

import com.rachmanforniandi.mahasiswacrudapp.model.delete.ResponseDelete
import com.rachmanforniandi.mahasiswacrudapp.model.insert.ResponseInsert
import com.rachmanforniandi.mahasiswacrudapp.model.login.ResponseLogin
import com.rachmanforniandi.mahasiswacrudapp.model.read.ResponseRead
import com.rachmanforniandi.mahasiswacrudapp.model.register.ResponseRegister
import com.rachmanforniandi.mahasiswacrudapp.model.update.ResponseUpdate
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface APIService {
    @FormUrlEncoded
    @POST("login.php")
    fun loginUser(@Field("email")email:String?,
                  @Field("password")password:String?): Single<ResponseLogin>

    @FormUrlEncoded
    @POST("register.php")
    fun registerUser(@Field("username")username:String?,
                     @Field("password")password:String?,
                     @Field("alamat_domisili")jurusan:String?,
                     @Field("no_hp")noHp:String?,
                     @Field("email")email:String?): Single<ResponseRegister>

    @GET("read_health.php")
    fun getData(): Observable<ResponseRead>

    @Multipart
    @POST("insert.php")
    fun insertData(
        @Part("title")title:RequestBody,
        @Part("content_news")content_news:String,
        @Part("author")author:String,
        @Part file:MultipartBody.Part): Single<ResponseInsert>


    @Multipart
    @POST("update.php")
    fun updateData(@Field("id")id:String,
                   @Part("title")title:RequestBody,
                   @Part("content_news")content_news:String,
                   @Part("author")author:String,
                   @Part file:MultipartBody.Part): Single<ResponseUpdate>

    @FormUrlEncoded
    @POST("delete.php")
    fun deleteData(@Field("id")id:String?): Single<ResponseDelete>
}