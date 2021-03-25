package rachmanforniandi.healthnews.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rachmanforniandi.mahasiswacrudapp.model.read.ResponseRead
import com.rachmanforniandi.mahasiswacrudapp.model.register.ResponseRegister
import rachmanforniandi.healthnews.repositories.ReadRepository
import rachmanforniandi.healthnews.repositories.RegisterRepository

class ReadViewModel: ViewModel()  {
    private var repo = ReadRepository()
    var apiError = MutableLiveData<Throwable>()
    var isLoading = MutableLiveData<Boolean>()
    var readResponder = MutableLiveData<ResponseRead>()

    fun showDataNews(){
        repo.newsEducationData({
            readResponder.value = it
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