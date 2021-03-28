package rachmanforniandi.healthnews.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.rachmanforniandi.mahasiswacrudapp.model.read.ItemDataRead
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.startActivity
import rachmanforniandi.healthnews.R

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val dataDetail = intent.getSerializableExtra("detailNews")as ItemDataRead




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