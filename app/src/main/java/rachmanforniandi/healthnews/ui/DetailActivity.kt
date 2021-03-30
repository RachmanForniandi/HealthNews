package rachmanforniandi.healthnews.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.rachmanforniandi.mahasiswacrudapp.model.read.ItemDataRead
import kotlinx.android.synthetic.main.activity_add_update_data.*
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.image
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.startActivity
import rachmanforniandi.healthnews.BuildConfig
import rachmanforniandi.healthnews.R

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val dataDetail = intent.getSerializableExtra("detailNews")as ItemDataRead
        etDetailTitle.text = dataDetail.title
        etDetailContentNews.text = dataDetail.content_news
        etDetailAuthor.text = dataDetail.author

        Glide.with(this)
            .load(BuildConfig.URL_IMAGE_DATA + dataDetail.image)
            .placeholder(R.drawable.placeholder_2)
            .centerCrop()
            .into(iv_detail_image)
        Log.e("debugDetail",""+ BuildConfig.URL_IMAGE_DATA + dataDetail.image)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean{
        menuInflater.inflate(R.menu.menu_detail,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_update->{
                startActivity<AddUpdateDataActivity>()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}