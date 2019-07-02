package com.example.project

import android.graphics.BitmapFactory
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.github.bassaer.chatmessageview.model.ChatUser
import com.github.bassaer.chatmessageview.model.Message
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.FuelManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val ACCESS_TOKEN = "d4c2c418315844cc835c24dee9ccb071"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FuelManager.instance.baseHeaders = mapOf(
            "Authorization" to "Bearer $ACCESS_TOKEN"
        )

        FuelManager.instance.basePath =
            "https://api.dialogflow.com/v1/query?v=20170712"

        FuelManager.instance.baseParams = listOf(
            "v" to "20170712",                  //latest protocol
            "sessionId" to UUID.randomUUID(),   //random ID
            "lang" to "en"                      //English language
        )

        val human = ChatUser(
            1,
            "User",
            BitmapFactory.decodeResource(resources,
                R.drawable.youngman)
        )

        val agents = ChatUser(
            2,
            "KKU-BOT",
            BitmapFactory.decodeResource(resources,
                R.drawable.logo)
        )

        my_chat_view.setLeftBubbleColor(ContextCompat.getColor(this, R.color.green500))
        my_chat_view.setRightBubbleColor(ContextCompat.getColor(this, R.color.green500))
        my_chat_view.setLeftMessageTextColor(Color.WHITE)
        my_chat_view.setUsernameTextColor(Color.BLUE)
        my_chat_view.setSendTimeTextColor(Color.BLUE)
        my_chat_view.setDateSeparatorColor(Color.BLUE)


        my_chat_view.setOnClickSendButtonListener(
            View.OnClickListener {
                my_chat_view.send(
                    Message.Builder()
                        .setUser(human)
                        .setText(my_chat_view.inputText)
                        .build()

                )


                //More code here
                Fuel.get("/query", listOf("query" to my_chat_view.inputText))
                    .responseJson { _, _, result ->
                        val reply = result.get().obj()
                            .getJSONObject("result")
                            .getJSONObject("fulfillment")
                            .getString("speech")

                        //More code here
                        my_chat_view.send(
                            Message.Builder()
                                .setRight(true)
                                .setUser(agents)
                                .setType(Message.Type.LINK)
                                .setText(reply)
                                .build()
                        )
                    }
                my_chat_view.inputText= ""
            }

        )
    }
}
