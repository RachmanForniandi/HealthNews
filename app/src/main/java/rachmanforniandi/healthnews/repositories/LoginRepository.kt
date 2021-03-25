package rachmanforniandi.healthnews.repositories

import com.rachmanforniandi.mahasiswacrudapp.model.login.ResponseLogin
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import rachmanforniandi.healthnews.networks.NetworkConfig

class LoginRepository {
    private var api = NetworkConfig.useServiceApi()
    private var compositeDisposable = CompositeDisposable()


    fun forLogin(username:String, password:String,
                 responseHandler: (ResponseLogin) -> Unit, errorHandler: (Throwable) -> Unit){
        compositeDisposable.add(
            api.loginUser(username,password)
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