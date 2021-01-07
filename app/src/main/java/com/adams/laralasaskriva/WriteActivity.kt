package com.adams.laralasaskriva

import WriteActivityRecyclerAdapter
import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Color
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.view.View.DRAG_FLAG_OPAQUE
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import nl.dionsegijn.konfetti.KonfettiView
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size


class WriteActivity : AppCompatActivity() {

    private val UPPER_CASE = 0
    private val LOWER_CASE = 1
    private var currentCase = UPPER_CASE

    private lateinit var word: LearningWord
    private var foundLetters = 0

    private lateinit var soundPool: SoundPool

    private lateinit var recyclerViewDrop: RecyclerView
    private lateinit var recyclerViewDrag1: RecyclerView
    private lateinit var recyclerViewDrag2: RecyclerView

    private lateinit var recyclerAdapterViewDrop: WriteActivityRecyclerAdapter
    private lateinit var recyclerAdapterViewDrag1: WriteActivityRecyclerAdapter
    private lateinit var recyclerAdapterViewDrag2: WriteActivityRecyclerAdapter


    private val wordFactory = LearningWordFactory()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)
        word = wordFactory.getRandomWord(1)
        val imageBackground = findViewById<ConstraintLayout>(R.id.picture_background)
        val imageView = findViewById<ImageView>(R.id.picture)
        imageView.setImageDrawable(getDrawable(word.imageId))
        setUpDragAndDropLetters(word)

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(10)
            .setAudioAttributes(audioAttributes)
            .build()

        playSound(word.soundId)

        val backButton = findViewById<ImageButton>(R.id.back_button)
        val refreshButton = findViewById<ImageButton>(R.id.reset_button)
        val changeCapital = findViewById<ImageButton>(R.id.capital_button)

        imageBackground.setOnClickListener {
            playSound(word.soundId)
        }
        backButton.setOnClickListener {
            backButtonAction()
        }
        refreshButton.setOnClickListener {
            refreshButtonAction()
        }
        changeCapital.setOnClickListener {
            changeCapitalAction(word)
        }

    }

    private fun setUpDragAndDropLetters(word: LearningWord) {

        val wordLetters = wordFactory.getWordAsCharList(word.word)

        val randomLetters = wordFactory.getRandomLetters(word.length)
        val letters = wordLetters + randomLetters
        val shuffledLetters = letters.shuffled()

        val chunkLetters = shuffledLetters.chunked(shuffledLetters.size / 2)

        initRecyclerViewDrop(wordLetters)
        initRecyclerViewDrag1(chunkLetters[0] as ArrayList<Char>)
        initRecyclerViewDrag2(chunkLetters[1] as ArrayList<Char>)
    }

    private fun initRecyclerViewDrop(letters: ArrayList<Char>) {
        recyclerViewDrop = findViewById(R.id.letter_drop)
        recyclerViewDrop.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        recyclerAdapterViewDrop =
            WriteActivityRecyclerAdapter(R.layout.letter_line_item) { view: View ->
                addDragListener(view)
            }
        recyclerViewDrop.adapter = recyclerAdapterViewDrop
        recyclerAdapterViewDrop.submitList(letters)
    }

    private fun initRecyclerViewDrag1(letters: ArrayList<Char>) {
        recyclerViewDrag1 = findViewById(R.id.letters_drag_1)
        recyclerViewDrag1.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        recyclerAdapterViewDrag1 =
            WriteActivityRecyclerAdapter(R.layout.letter_drag_item) { view: View ->
                setDragAction(view)
            }
        recyclerViewDrag1.adapter = recyclerAdapterViewDrag1
        recyclerAdapterViewDrag1.submitList(letters)
    }

    private fun initRecyclerViewDrag2(letters: ArrayList<Char>) {
        recyclerViewDrag2 = findViewById(R.id.letters_drag_2)
        recyclerViewDrag2.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        recyclerAdapterViewDrag2 =
            WriteActivityRecyclerAdapter(R.layout.letter_drag_item) { view: View ->
                setDragAction(view)
            }
        recyclerViewDrag2.adapter = recyclerAdapterViewDrag2
        recyclerAdapterViewDrag2.submitList(letters)
    }

    private fun setDragAction(view: View) {
        view.setOnTouchListener { v: View, _ ->

            // Create a new ClipData.
            // This is done in two steps to provide clarity. The convenience method
            // ClipData.newPlainText() can create a plain text ClipData in one step.
            // Create a new ClipData.Item from the ImageView object's tag/*
            val letterChar = v.findViewById<TextView>(R.id.letter).text
            val item = ClipData.Item(letterChar)

            // Create a new ClipData using the tag as a label, the plain text MIME type, and
            // the already-created item. This will create a new ClipDescription object within the
            // ClipData, and set its MIME type entry to "text/plain"
            val dragData = ClipData(
                letterChar,
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                item
            )

            //startDrag is deprecated, but min API for startDragAndDrop is 24
            // while my supported min API is 21
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                v.startDragAndDrop(
                    dragData,   // the data to be dragged
                    DragShadow(v),   // the drag shadow builder
                    null,       // no need to use local data
                    DRAG_FLAG_OPAQUE
                )
            } else {
                v.startDrag(
                    dragData,
                    DragShadow(v),
                    null,
                    0 // Not using flags here
                )
            }
        }
    }

    private fun addDragListener(view: View) {
        //CHANGE BACKGROUND WORD HERE
        view.findViewById<TextView>(R.id.letter).visibility = View.INVISIBLE
        view.setOnDragListener { v, event ->
            // Handles each of the expected events
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    // Determines if this View can accept the dragged data
                    if (event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                        //CHANGE BACKGROUND WORD HERE

                        true
                    } else {
                        // Returns false. During the current drag and drop operation, this View will
                        // not receive events again until ACTION_DRAG_ENDED is sent.
                        false
                    }
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    //CHANGE BACKGROUND WORD HERE
                    v.invalidate()
                    true
                }

                DragEvent.ACTION_DRAG_LOCATION ->
                    // Ignore the event
                    true
                DragEvent.ACTION_DRAG_EXITED -> {
                    //CHANGE BACKGROUND WORD HERE
                    v.invalidate()
                    true
                }
                DragEvent.ACTION_DROP -> {

                    val correctLetter = v.findViewById<TextView>(R.id.letter).text
                    // Gets the item containing the dragged data
                    val item: ClipData.Item = event.clipData.getItemAt(0)

                    // Gets the text data from the item.
                    val dragData = item.text

                    // Displays a message containing the dragged data.
                    var soundId = R.raw.correct
                    var view = v.findViewById<TextView>(R.id.letter)
                    if (dragData == correctLetter && view.visibility != View.VISIBLE) {
                        view.visibility = View.VISIBLE
                        foundLetters += 1
                    } else {
                        soundId = R.raw.wrong
                    }
                    playSound(soundId)

                    if (foundLetters == word.length) {
                        playSound(R.raw.finished, 0.7F)
                        makeConfetti()
                        showNewGameOptions()
                    }
                    // Displays a message containing the dragged data.
                    /* Toast.makeText(this, "Dragged data is " + dragData, Toast.LENGTH_LONG)
                         .show()*/

                    v.invalidate()

                    // Returns true. DragEvent.getResult() will return true.
                    true
                }

                DragEvent.ACTION_DRAG_ENDED -> {

                    // Gets the item containing the dragged data
//                    val item: ClipData.Item = event.clipData.getItemAt(0)

                    // Gets the text data from the item.
                    //                  val dragData = item.text

                    // Turns off any color tinting
                    //CHANGE BACKGROUND WORD HERE

                    // Invalidates the view to force a redraw
                    v.invalidate()

                    // Does a getResult(), and displays what happened.
                    if (event.result) {
                        // v.findViewById<TextView>(R.id.letter).visibility = View.VISIBLE
                    }


                    // returns true; the value is ignored.
                    true
                }
                else -> {
                    // An unknown action type was received.
                    Log.e("DragDrop Example", "Unknown action type received by OnDragListener.")
                    false
                }
            }
        }
    }

    private fun playSound(soundId: Int, volume: Float = 1F) {
        val sound = soundPool.load(this, soundId, 1)
        soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
            soundPool.play(sound, volume, volume, 1, 0, 1F)
        }
    }

    private fun refreshButtonAction() {
        val intent = intent
        finish()
        startActivity(intent)
    }

    private fun backButtonAction() {
        finish()
    }

    private fun changeCapitalAction(word: LearningWord) {
        val randomLetters = wordFactory.getRandomLetters(word.length)
        val wordLetters = wordFactory.getWordAsCharList(word.word)
        val letters = wordLetters + randomLetters
        val shuffledLetters = letters.shuffled()
        val chunkLetters = shuffledLetters.chunked(shuffledLetters.size / 2)

        if (currentCase == UPPER_CASE) {
            recyclerAdapterViewDrop.submitList(wordLetters.map { it.toLowerCase() })
            recyclerAdapterViewDrag1.submitList(chunkLetters[0].map { it.toLowerCase() })
            recyclerAdapterViewDrag2.submitList(chunkLetters[1].map { it.toLowerCase() })
            currentCase = LOWER_CASE
        } else {
            recyclerAdapterViewDrop.submitList(wordLetters)
            recyclerAdapterViewDrag1.submitList(chunkLetters[0])
            recyclerAdapterViewDrag2.submitList(chunkLetters[1])
            currentCase = UPPER_CASE
        }
        recyclerAdapterViewDrop.notifyDataSetChanged()
        recyclerAdapterViewDrag1.notifyDataSetChanged()
        recyclerAdapterViewDrag2.notifyDataSetChanged()
        setCorrectCaseIcon()

    }

    private fun setCorrectCaseIcon() {
        if (currentCase == UPPER_CASE) {
            findViewById<ImageButton>(R.id.capital_button).setImageResource(R.drawable.noncapital)
        } else {
            findViewById<ImageButton>(R.id.capital_button).setImageResource(R.drawable.capital)
        }
    }

    private fun makeConfetti(){
        val confetti = findViewById<KonfettiView>(R.id.confetti)
        confetti.build()
            .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
            .setDirection(0.0, 359.0)
            .setSpeed(1f, 5f)
            .setFadeOutEnabled(true)
            .setTimeToLive(2000L)
            .addShapes(Shape.Square, Shape.Circle)
            .addSizes(Size(10))
            .setPosition(-50f, confetti.width + 50f, -50f, -50f)
            .streamFor(100, 5000L)
    }

    private fun showNewGameOptions(){
        val newGameButton = findViewById<ImageButton>(R.id.game_over_restart)
        newGameButton.visibility = View.VISIBLE
        newGameButton.setOnClickListener {
            refreshButtonAction()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }

}
