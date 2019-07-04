package com.example.texttospeech

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.v4.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import java.util.*


class MainActivity : AppCompatActivity() {

    var textToSpeak : TextToSpeech ?= null
    val RESPONSE_CODE = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_speak.setOnClickListener {
            readText(edit_text_source_text.text.toString())
        }
        startActivityForResult(Intent().setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA), RESPONSE_CODE)
    }

    private fun readText(text: String) {
        textToSpeak?.speak(text, TextToSpeech.QUEUE_FLUSH, null)
        textToSpeak?.speak(text, TextToSpeech.QUEUE_FLUSH, null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESPONSE_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //the user has the necessary data - create the TTS
                textToSpeak = TextToSpeech(this, TextToSpeech.OnInitListener {
                    if (resultCode == TextToSpeech.SUCCESS) {
                        if (textToSpeak?.isLanguageAvailable(Locale.ENGLISH) == TextToSpeech.LANG_AVAILABLE)
                            textToSpeak?.language = Locale.US
                    } else if (resultCode == TextToSpeech.ERROR) {
                        Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                //no data - install it now
                val installTTSIntent = Intent()
                installTTSIntent.action = TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA
                startActivity(installTTSIntent)
            }
        }
    }
}
