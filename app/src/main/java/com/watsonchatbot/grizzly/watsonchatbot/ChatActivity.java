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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by kubek on 10/4/18.
 */

/**
 * Main chat activity, connecting to database and sending messages
 */
public class ChatActivity extends AppCompatActivity {

    private Button addMesage;

    ArrayList<String> messages=new ArrayList<>();
    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        addMesage = findViewById(R.id.fab);

        addMesage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = findViewById(R.id.input);
                messages.add(input.getText().toString());
                input.setText("");
                displayChatMessage();
            }
        });

    }


    /**
     * Show chat message in ListView
     */
    private void displayChatMessage() {
        if(messages.size() != 0) {
            ListView listOfMessages = findViewById(R.id.list_of_messages);
            adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1,
                    messages);
            listOfMessages.setAdapter(adapter);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out );
    }
}


















