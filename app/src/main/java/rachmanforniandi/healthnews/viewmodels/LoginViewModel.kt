package rachmanforniandi.healthnews.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rachmanforniandi.mahasiswacrudapp.model.login.ResponseLogin
import rachmanforniandi.healthnews.repositories.LoginRepository

class LoginViewModel:ViewModel() {

    private var repo = LoginRepository()
    var apiError = MutableLiveData<Throwable>()
    var isLoading = MutableLiveData<Boolean>()
    var loginResponder = MutableLiveData<ResponseLogin>()
    var isEmptyInput = MutableLiveData<Boolean>()

    fun loginUser(username:String,password:String){
        if (username.isEmpty() || password.isEmpty()){
            isEmptyInput.value=true
        }else{
            isLoading.value = true

            isEmptyInput.value=false

            repo.forLogin(username,password,{
                loginResponder.value =it
                isLoading.value = false
            },{
                apiError.value = it
                isLoading.value = false
            })
        }
    }

    override fun onCleared() {
        super.onCleared()
        repo.onClear()
    }

}