package rachmanforniandi.healthnews.ui

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.rachmanforniandi.mahasiswacrudapp.model.delete.ResponseDelete
import com.rachmanforniandi.mahasiswacrudapp.model.read.ItemDataRead
import com.rachmanforniandi.mahasiswacrudapp.model.read.ResponseRead
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import rachmanforniandi.healthnews.R
import rachmanforniandi.healthnews.adapters.HealthNewsAdapter
import rachmanforniandi.healthnews.utility.SessionManager
import rachmanforniandi.healthnews.utility.SwipeToDelete
import rachmanforniandi.healthnews.viewmodels.MainViewModel

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var sessionManager: SessionManager

    //private val healthDoAdapter:HealthNewsAdapter by lazy { HealthNewsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        sessionManager = SessionManager(this)

        mainViewModel.showDataNews()
        observeMainPageData()

        /*val fab: FloatingActionButton = findViewById(R.id.btn_add_news_health)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }*/
        btn_add_news_health.setOnClickListener {
            startActivity<AddUpdateDataActivity>()
        }

        swipeRefreshData.setOnRefreshListener {
            val handler = Handler()
            handler.postDelayed({
                mainViewModel.showDataNews()
                swipeRefreshData.isRefreshing = false
            }, 3000)
        }
        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun observeMainPageData() {
        mainViewModel.isLoading.observe(this,{showLoadingMainData(it)})
        mainViewModel.readResponder.observe(this,{displayDataHealthNews(it)})
        mainViewModel.apiError.observe(this,{errorDisplayDataHealthNews(it)})

    }

    private fun showLoadingMainData(it: Boolean?) {
        if (it == true) {
            progress_main.visibility = View.VISIBLE
        } else {
            progress_main.visibility = View.GONE
        }
    }

    private fun displayDataHealthNews(it: ResponseRead?) {
        listItemUser.adapter = HealthNewsAdapter(it?.data as ArrayList<ItemDataRead>,object :HealthNewsAdapter.onClickItem{
            override fun itemTipsClicked(item: ItemDataRead) {
                startActivity<DetailActivity>("detailNews" to item)
            }

            override fun itemDeleteClicked(item: ItemDataRead) {
                dialogToAskForDelete(item)
                //item.id?.let { it1 -> mainViewModel.deleteDataNews(it1) }
            }
        })
        //listItemUser.let { swipeToDelete(it) }
    }

    private fun errorDisplayDataHealthNews(it: Throwable?) {
        toast(it?.message ?: "")
    }

    private fun dialogToAskForDelete(item: ItemDataRead) {
        AlertDialog.Builder(this)
            .setMessage("Are you sure you want to delete this data ?")
            .setPositiveButton("Yes"){ _, _ ->
                item.id?.let { it1 -> mainViewModel.deleteDataNews(it1) }
                observeDeleteData()
            }
            .setNegativeButton("No"){
                    dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun observeDeleteData() {
        mainViewModel.deleteResponder.observe(this,{deleteCertainData(it)})
        mainViewModel.apiError.observe(this,{errorDelete(it)})
        mainViewModel.isLoading.observe(this,{progressDelete(it)})
    }

    private fun progressDelete(it: Boolean?) {
        if (it == true) {
            progress_main.visibility = View.VISIBLE
        } else {
            progress_main.visibility = View.GONE
        }
    }


    private fun deleteCertainData(it: ResponseDelete?) {
        if (it?.message.equals("Data Berhasil Dihapus.")){
            startActivity<MainActivity>()
            finish()
        }else{
            toast(it?.message ?: "")
        }
    }

    private fun errorDelete(it: Throwable?) {
        toast(it?.message ?: "")
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean{
        val id = item.itemId
        if (id == R.id.nav_logout) {
            sessionManager.logOut()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)

        }
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}