package com.example.texttospeech

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private var textToSpeak: TextToSpeech? = null
    private val TTS_REQUEST_CODE = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_speak.setOnClickListener {
            val text = if (!edit_text_source_text.text.toString().isEmpty()) {
                edit_text_source_text.text.toString()
            } else {
                "សូមមេត្តាបញ្ចូលអត្ថបទជាភាសាខ្មែរឬជាភាសាអង់គ្លេសដើម្បីដំណើរការមុខងារText To Speech​ សូមអរគុណ។"
            }
            readText(text)
        }
        startActivityForResult(Intent().setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA), TTS_REQUEST_CODE)
    }

    private fun readText(text: String) {
        textToSpeak?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TTS_REQUEST_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //the user has the necessary data - create the TTS
                textToSpeak = TextToSpeech(this, TextToSpeech.OnInitListener {
                    if (resultCode == TextToSpeech.SUCCESS) {
                        if (textToSpeak?.isLanguageAvailable(Locale.getDefault()) == TextToSpeech.LANG_AVAILABLE){
                            textToSpeak?.language = Locale.forLanguageTag("KH")
//                            textToSpeak?.setPitch(100f)
//                            val a = HashSet<String>()
//                            a.add("male")
//                            textToSpeak?.voice = Voice("en-us-x-sfg#male_2-local",Locale("kh","US"),400,200,true, a)
//                            textToSpeak?.setSpeechRate(1.0f)
                        }
                        else
                            Toast.makeText(this@MainActivity, "Language is not available...", Toast.LENGTH_SHORT).show()
                    } else if (resultCode == TextToSpeech.ERROR) {
                        Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                startActivity(Intent().setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA))
            }
        }
    }
}
