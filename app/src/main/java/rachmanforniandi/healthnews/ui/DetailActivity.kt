package rachmanforniandi.healthnews.ui

import android.content.Intent
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
import org.jetbrains.anko.startActivity
import rachmanforniandi.healthnews.BuildConfig
import rachmanforniandi.healthnews.R

class DetailActivity : AppCompatActivity() {

   private var dataDetail: ItemDataRead? = null

    /*init {
        dataDetail = ItemDataRead()
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        dataDetail = intent.getSerializableExtra("detailNews")as ItemDataRead
        etDetailTitle.text = dataDetail?.title
        etDetailContentNews.text = dataDetail?.content_news
        etDetailAuthor.text = dataDetail?.author

        Glide.with(this)
            .load(BuildConfig.URL_IMAGE_DATA + dataDetail?.image)
            .placeholder(R.drawable.placeholder_2)
            .centerCrop()
            .into(iv_detail_image)
        Log.e("debugDetail",""+ BuildConfig.URL_IMAGE_DATA + dataDetail?.image)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean{
        menuInflater.inflate(R.menu.menu_detail,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_update->{
                val intentToUpdate=Intent(this@DetailActivity,AddUpdateDataActivity::class.java)
                intentToUpdate.putExtra("detailNews",dataDetail)
                startActivity(intentToUpdate)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}