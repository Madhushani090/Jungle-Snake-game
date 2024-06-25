package com.example.snakegame
     import android.content.Intent
     import android.media.MediaPlayer
     import android.os.Bundle
     import android.view.View
     import androidx.appcompat.app.AppCompatActivity

class start {



    class StartActivity : AppCompatActivity() {


        private lateinit var mediaPlayer: MediaPlayer


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.start)

            // Initialize the MediaPlayer object with the music file
            mediaPlayer = MediaPlayer.create(this, R.raw.musicnew)

            // Start playing the music
            mediaPlayer.start()

            // Set the onCompletionListener to loop the music
            mediaPlayer.setOnCompletionListener {
                mediaPlayer.seekTo(0) // Rewind to the beginning
                mediaPlayer.start() // Start playing again
            }
        }

        fun startGame(view: View) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        fun exitApp(view: View) {
            finish()
        }
        override fun onDestroy() {
            super.onDestroy()
            mediaPlayer.release() // Release the MediaPlayer when the activity is destroyed
        }
    }

}


