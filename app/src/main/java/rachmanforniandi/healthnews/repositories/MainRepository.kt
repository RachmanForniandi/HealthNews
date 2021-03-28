package rachmanforniandi.healthnews.repositories

import com.rachmanforniandi.mahasiswacrudapp.model.delete.ResponseDelete
import com.rachmanforniandi.mahasiswacrudapp.model.insert.ResponseInsert
import com.rachmanforniandi.mahasiswacrudapp.model.read.ResponseRead
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import rachmanforniandi.healthnews.networks.NetworkConfig

class MainRepository {
    private var api = NetworkConfig.useServiceApi()
    private var compositeDisposable = CompositeDisposable()

    fun readNewsEducationData(responseHandler: (ResponseRead) -> Unit, errorHandler: (Throwable) -> Unit){
        compositeDisposable.add(
            api.getDataHealth()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    responseHandler(it)
                }, {
                    errorHandler(it)
                })
        )
    }

    fun restoreDataNews(title: RequestBody, content_news: RequestBody,
                         author: RequestBody, img: MultipartBody.Part,
                         responseHandler: (ResponseInsert) -> Unit, errorHandler: (Throwable) -> Unit){
        compositeDisposable.add(
            api.insertDataHealth(title,content_news,author,img)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    responseHandler(it)
                }, {
                    errorHandler(it)
                })
        )
    }

    fun deleteDataHealthNews(id:String,
                             responseHandler: (ResponseDelete) -> Unit, errorHandler: (Throwable) -> Unit){
        compositeDisposable.add(
            api.deleteData(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    responseHandler(it)
                }, {
                    errorHandler(it)
                })
        )
    }

    fun onClear() {
        compositeDisposable.clear()
    }
}