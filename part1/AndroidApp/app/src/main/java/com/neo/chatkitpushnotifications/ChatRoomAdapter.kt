package com.neo.chatkitpushnotifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pusher.chatkit.messages.multipart.Message
import com.pusher.chatkit.messages.multipart.Payload

class ChatRoomAdapter : RecyclerView.Adapter<ChatRoomAdapter.ViewHolder>() {

    private var messageList = ArrayList<Message>()

    fun addMessage(model:Message){
        this.messageList.add(model)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_list_row, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(messageList[position])

    override fun getItemCount() = messageList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val username: TextView = itemView.findViewById(R.id.editTextUsername)
        private val message: TextView = itemView.findViewById(R.id.editTextMessage)

        fun bind(item: Message) = with(itemView) {
            username.text = item.sender.name
            when (val data = item.parts[0].payload){

                is Payload.Inline -> {
                    message.text = data.content
                }

            }

        }

    }


}