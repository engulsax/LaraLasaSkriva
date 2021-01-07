import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.adams.laralasaskriva.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.letter_line_item.view.*
import kotlin.random.Random

class MainActivityRecyclerAdapter(
    private val clickAction: (view: View) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var categories = Settings.getAllCategories()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LetterViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.category_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {

            is LetterViewHolder -> {

                holder.bind(categories[position])
                holder.imageView.setOnClickListener {
                    clickAction(holder.itemView)
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }


    class LetterViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val titleView = itemView.findViewById<TextView>(R.id.title)
        val imageView = itemView.findViewById<CircleImageView>(R.id.image)

        fun bind(category: Category) {
            titleView.setText(category.nameTitleId)
            titleView.setTextColor(category.colorId)
            imageView.setImageResource(category.backgroundId)

        }
    }
}