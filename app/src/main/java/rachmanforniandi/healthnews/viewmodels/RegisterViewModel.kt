package rachmanforniandi.healthnews.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rachmanforniandi.mahasiswacrudapp.model.register.ResponseRegister
import rachmanforniandi.healthnews.repositories.RegisterRepository

class RegisterViewModel:ViewModel() {
    private var repo = RegisterRepository()
    var apiError = MutableLiveData<Throwable>()
    var isLoading = MutableLiveData<Boolean>()
    var registerResponder =MutableLiveData<ResponseRegister>()
    var isEmptyInput = MutableLiveData<Boolean>()


    fun registerUser(username:String,password:String,alamat_domisili:String,no_hp:String,email:String){
        isLoading.value = true
        repo.forRegister(username,password,alamat_domisili,no_hp,email,{
            registerResponder.value =it
            isLoading.value = false
        },{
            apiError.value = it
            isLoading.value = false
        })
    }

    override fun onCleared() {
        super.onCleared()
        repo.onClear()
    }


}