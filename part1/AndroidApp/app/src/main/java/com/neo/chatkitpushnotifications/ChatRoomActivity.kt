package com.neo.chatkitpushnotifications

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.neo.chatkitpushnotifications.ChatkitApp.Companion.currentUser
import com.pusher.chatkit.messages.multipart.NewPart
import com.pusher.chatkit.rooms.RoomListeners
import kotlinx.android.synthetic.main.activity_chat_room.*

class ChatRoomActivity : AppCompatActivity() {

    private lateinit var textViewMessage: EditText
    private val chatRoomAdapter = ChatRoomAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)
        textViewMessage = findViewById(R.id.editTextMessage)
        setupRecyclerView()
        subscribeToRoom()
        setupButtonListener()
    }


    private fun setupRecyclerView() {
        with(recyclerViewMessages){
            layoutManager = LinearLayoutManager(this@ChatRoomActivity)
            adapter = chatRoomAdapter
            addItemDecoration(DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL))
        }
    }


    private fun subscribeToRoom() {

        currentUser.subscribeToRoomMultipart(
            roomId = currentUser.rooms[0].id,
            listeners = RoomListeners(
                onMultipartMessage = {
                    runOnUiThread {
                        chatRoomAdapter.addMessage(it)
                    }
                }
            ),
            messageLimit = 20, // Optional
            callback = { subscription ->
                // Called when the subscription has started.
                // You should terminate the subscription with subscription.unsubscribe()
                // when it is no longer needed
            }
        )

    }


    private fun setupButtonListener() {

        sendButton.setOnClickListener {
            if (textViewMessage.text.isNotEmpty()) {
                currentUser.sendMultipartMessage(
                    roomId = currentUser.rooms[0].id,
                    parts = listOf(
                        NewPart.Inline(textViewMessage.text.toString(), "text/plain")),
                    callback = { result -> // Result<Int, Error>
                        // The Int is the new message ID
                    }
                )
                textViewMessage.setText("")
                hideKeyboard()
            }

        }
    }


    private fun hideKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentFocus
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}
