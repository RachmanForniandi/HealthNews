package rachmanforniandi.healthnews.repositories

import com.rachmanforniandi.mahasiswacrudapp.model.delete.ResponseDelete
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import rachmanforniandi.healthnews.networks.NetworkConfig

class DeleteRepository {
    private var api = NetworkConfig.useServiceApi()
    private var compositeDisposable = CompositeDisposable()

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