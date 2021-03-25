package rachmanforniandi.healthnews.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rachmanforniandi.mahasiswacrudapp.model.delete.ResponseDelete
import rachmanforniandi.healthnews.repositories.DeleteRepository

class DeleteViewModel:ViewModel() {
    private var repo = DeleteRepository()
    var apiError = MutableLiveData<Throwable>()
    var isLoading = MutableLiveData<Boolean>()
    var deleteResponder = MutableLiveData<ResponseDelete>()

    fun updateDataNews(id: String){
        repo.deleteDataHealthNews(id,{
            deleteResponder.value = it
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