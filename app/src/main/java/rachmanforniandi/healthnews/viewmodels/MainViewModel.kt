package rachmanforniandi.healthnews.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rachmanforniandi.mahasiswacrudapp.model.delete.ResponseDelete
import com.rachmanforniandi.mahasiswacrudapp.model.insert.ResponseInsert
import com.rachmanforniandi.mahasiswacrudapp.model.read.ResponseRead
import okhttp3.MultipartBody
import okhttp3.RequestBody
import rachmanforniandi.healthnews.repositories.MainRepository

class MainViewModel: ViewModel() {
    private var repo = MainRepository()
    var apiError = MutableLiveData<Throwable>()
    var isLoading = MutableLiveData<Boolean>()
    var readResponder = MutableLiveData<ResponseRead>()
    //var restoreResponder =MutableLiveData<ResponseInsert>()
    var deleteResponder = MutableLiveData<ResponseDelete>()

    fun showDataNews(){
        repo.readNewsEducationData({
            readResponder.value = it
            isLoading.value = false
        },{
            apiError.value = it
            isLoading.value = false
        })
    }

    /*fun insertRestoreDataNews(title: RequestBody, content_news: RequestBody,
                       author: RequestBody, img: MultipartBody.Part){
        repo.restoreDataNews(title,content_news,author,img,{
            restoreResponder.value = it
            isLoading.value = false
        },{
            apiError.value = it
            isLoading.value = false
        })
    }*/

    fun deleteDataNews(id: String){
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