package rachmanforniandi.healthnews.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rachmanforniandi.mahasiswacrudapp.model.update.ResponseUpdate
import okhttp3.MultipartBody
import okhttp3.RequestBody
import rachmanforniandi.healthnews.repositories.UpdateRepository

class UpdateViewModel:ViewModel() {
    private var repo = UpdateRepository()
    var apiError = MutableLiveData<Throwable>()
    var isLoading = MutableLiveData<Boolean>()
    var updateResponder = MutableLiveData<ResponseUpdate>()
    var isEmptyInput = MutableLiveData<Boolean>()

    fun updateDataNews(id: RequestBody, title: RequestBody, content_news: RequestBody,
                     author: RequestBody, img: MultipartBody.Part){
        repo.updateDataHealthNews(id,title,content_news,author,img,{
            updateResponder.value = it
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