package com.neo.chatkitpushnotifications

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.pusher.chatkit.AndroidChatkitDependencies
import com.pusher.chatkit.ChatManager
import com.pusher.chatkit.ChatkitTokenProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loginButton.setOnClickListener { setupChatManager() }
    }

    private fun setupChatManager() {
        val chatManager = ChatManager(
            instanceLocator = "v1:us1:3bff3c2b-c602-4626-9da9-66e758fb238a",
            userId = username.text.toString(),
            dependencies = AndroidChatkitDependencies(
                tokenProvider = ChatkitTokenProvider(
                    endpoint = "http://10.0.2.2:3000/token",
                    userId = username.text.toString()
                ),
                context = this.applicationContext
            )
        )

        chatManager.connect { result ->
            when (result) {
                is com.pusher.util.Result.Success -> {
                    ChatkitApp.currentUser = result.value
                    result.value.enablePushNotifications { pushNotifResult ->
                        when(pushNotifResult) {
                            is com.pusher.util.Result.Success -> {
                                // Push Notifications Service Enabled!
                                startActivity(Intent(this@MainActivity,ChatRoomActivity::class.java))
                                finish()
                            }

                            is Error -> Log.e("Error", "Error starting Push Notifications")
                        }
                    }
                }

                is com.pusher.util.Result.Failure -> {
                }
            }
        }

    }


}
