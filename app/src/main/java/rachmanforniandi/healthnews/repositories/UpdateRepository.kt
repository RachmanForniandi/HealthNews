package rachmanforniandi.healthnews.repositories

import com.rachmanforniandi.mahasiswacrudapp.model.update.ResponseUpdate
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import rachmanforniandi.healthnews.networks.NetworkConfig

class UpdateRepository {

    private var api = NetworkConfig.useServiceApi()
    private var compositeDisposable = CompositeDisposable()

    fun updateDataHealthNews(id: RequestBody, title: RequestBody, content_news: RequestBody,
                             author: RequestBody, img: MultipartBody.Part,
                             responseHandler: (ResponseUpdate) -> Unit, errorHandler: (Throwable) -> Unit){
        compositeDisposable.add(
            api.updateData(id,title,content_news,author,img)
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