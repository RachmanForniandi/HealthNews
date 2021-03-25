package rachmanforniandi.healthnews.repositories

import com.rachmanforniandi.mahasiswacrudapp.model.register.ResponseRegister
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import rachmanforniandi.healthnews.networks.NetworkConfig

class RegisterRepository {
    private var api = NetworkConfig.useServiceApi()
    private var compositeDisposable = CompositeDisposable()


    fun forRegister(username:String, password:String, alamat_domisili:String, no_hp:String, email:String,
                    responseHandler: (ResponseRegister) -> Unit, errorHandler: (Throwable) -> Unit){
        compositeDisposable.add(
            api.registerUser(username,password,alamat_domisili,no_hp,email)
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