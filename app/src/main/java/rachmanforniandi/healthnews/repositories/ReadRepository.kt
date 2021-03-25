package rachmanforniandi.healthnews.repositories

import com.rachmanforniandi.mahasiswacrudapp.model.read.ResponseRead
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.RequestBody
import rachmanforniandi.healthnews.networks.NetworkConfig
import java.nio.file.Path

class ReadRepository {

    private var api = NetworkConfig.useServiceApi()
    private var compositeDisposable = CompositeDisposable()


    fun newsEducationData(responseHandler: (ResponseRead) -> Unit,errorHandler: (Throwable) -> Unit){
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

    fun onClear() {
        compositeDisposable.clear()
    }
}