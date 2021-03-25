package rachmanforniandi.healthnews.viewmodels

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rachmanforniandi.mahasiswacrudapp.model.insert.ResponseInsert
import com.rachmanforniandi.mahasiswacrudapp.model.update.ResponseUpdate
import okhttp3.MultipartBody
import okhttp3.RequestBody
import rachmanforniandi.healthnews.repositories.InsertRepository
import rachmanforniandi.healthnews.repositories.UpdateRepository

class InsertViewModel:ViewModel() {
    private var repo = InsertRepository()
    var apiError = MutableLiveData<Throwable>()
    var isLoading = MutableLiveData<Boolean>()
    var insertResponder = MutableLiveData<ResponseInsert>()

    fun updateDataNews(title: RequestBody, content_news: RequestBody,
                       author: RequestBody, img: MultipartBody.Part){
        repo.insertHealthNews(title,content_news,author,img,{
            insertResponder.value = it
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