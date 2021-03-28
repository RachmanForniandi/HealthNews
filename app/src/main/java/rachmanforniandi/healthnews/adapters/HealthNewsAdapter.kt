package rachmanforniandi.healthnews.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rachmanforniandi.mahasiswacrudapp.model.read.ItemDataRead
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_news.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import rachmanforniandi.healthnews.BuildConfig
import rachmanforniandi.healthnews.R

class HealthNewsAdapter(var dataHealthNews: List<ItemDataRead>, val clicked: onClickItem): RecyclerView.Adapter<HealthNewsAdapter.HealthNewsHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HealthNewsHolder {
        return HealthNewsHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_news,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HealthNewsHolder, position: Int) {
        val item = dataHealthNews[position]
        holder.headLineText.text = item.title
        holder.authorText.text = item.author
        val itemId = item.id

        val imgNews = item.image
        Picasso.get()
            .load(BuildConfig.URL_IMAGE_DATA + imgNews)
            .error(R.drawable.place_holder)
            .into(holder.imgHealthNews)

        holder.itemView.onClick {
            clicked.itemTipsClicked(item)
        }
        holder.imgDataDelete.onClick {
            clicked.itemDeleteClicked(item)
        }
    }

    override fun getItemCount(): Int {
        return dataHealthNews.size
    }

    inner class HealthNewsHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val imgHealthNews = itemView.img_news
        val headLineText = itemView.txt_headline_news
        val authorText = itemView.txt_author
        val imgDataDelete = itemView.btn_delete
    }

    interface onClickItem {
        fun itemTipsClicked(item: ItemDataRead)
        fun itemDeleteClicked(item: ItemDataRead)
    }

    fun setData(newsData: List<ItemDataRead>){
        this.dataHealthNews = newsData
        notifyDataSetChanged()
    }


}