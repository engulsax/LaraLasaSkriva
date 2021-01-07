import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.adams.laralasaskriva.LearningWord
import com.adams.laralasaskriva.R
import com.adams.laralasaskriva.alphabet
import kotlinx.android.synthetic.main.letter_line_item.view.*
import kotlin.random.Random

class WriteActivityRecyclerAdapter(
    private val layoutId: Int,
    private val clickAction: (view: View) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Char> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LetterViewHolder(
            LayoutInflater.from(parent.context).inflate(
                layoutId,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {

            is LetterViewHolder -> {
                val letter = items[position]
                holder.bind(letter)
                clickAction(holder.itemView)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(letters: List<Char>) {
        items = letters
    }

    class LetterViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val colors = arrayOf(
            R.color.letterGreen,
            R.color.letterPink,
            R.color.letterRed,
            R.color.letterYellow
        )

        val letterView = itemView.findViewById<TextView>(R.id.letter)

        fun bind(letter: Char) {
            letterView.text = letter.toString()

            letterView.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    colors[Random.nextInt(0, colors.size - 1)]
                )
            )
        }
    }
}