/*
* Copyright 2018 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.watsonchatbot.grizzly.watsonchatbot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

/**
 * Created by kubek on 11/04/18.
 */

/**
 * Main chat activity, connecting to database and sending messages
 */
public class ChatActivity extends AppCompatActivity {

    private FirebaseListAdapter<ChatMessage> mAdapter;

    private Button mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mFab = findViewById(R.id.submit_message);


        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = findViewById(R.id.input_message);
                FirebaseDatabase.getInstance().getReference().child("messages").push()
                        .setValue(new ChatMessage(input.getText().toString(),
                                "device user"));
                input.setText("");
            }
        });

        //check if not sign in then navidate to sign in
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(ChatActivity.this, "no user", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(ChatActivity.this, "welcome " + FirebaseAuth.getInstance()
                    .getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
            displayChatMessage();
        }

    }

    /**
     * Show chat message in ListView
     */
    private void displayChatMessage() {
        ListView listOfMessages = findViewById(R.id.messages_list);

        mAdapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class, R.layout.send_message,
                FirebaseDatabase.getInstance().getReference().child("messages")) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                //get reference to to the value og list_item.xml
                TextView messageText, messageTime;
                messageText = v.findViewById(R.id.send_message_body);
                messageTime = v.findViewById(R.id.send_message_time);

                messageText.setText(model.getmMessageText());
                messageTime.setText(DateFormat.format("HH:mm", model.getmMessageTime()));
            }
        };
        listOfMessages.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out );
    }
}


















