package com.example.autandroidapp;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.android.AIService;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;


public class ChatbotActivity extends AppCompatActivity implements AIListener {

        RecyclerView recyclerView;
        EditText editText;
        RelativeLayout addBtn;
        DatabaseReference ref;
        FirebaseRecyclerAdapter<ChatbotMessage,ChatbotRecycler> adapter;
        Boolean flagFab = true;

        private AIService aiService;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_chatbot_button);

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},1);


            recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
            editText = (EditText)findViewById(R.id.editText);
            addBtn = (RelativeLayout)findViewById(R.id.addBtn);

            recyclerView.setHasFixedSize(true);
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(linearLayoutManager);

            ref = FirebaseDatabase.getInstance().getReference();
            ref.keepSynced(true);

            final AIConfiguration config = new AIConfiguration("acd43e00b5dc415685c033294a283b7a",
                    ai.api.android.AIConfiguration.SupportedLanguages.English,
                    AIConfiguration.RecognitionEngine.System);

            aiService = AIService.getService(this, config);
            aiService.setListener(this);

            final AIDataService aiDataService = new AIDataService(getApplicationContext(),config);
            final AIRequest aiRequest = new AIRequest();

            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String message = editText.getText().toString().trim();

                    if (!message.equals("")) {

                        ChatbotMessage chatMessage = new ChatbotMessage(message, "user");
                        ref.child("chat").push().setValue(chatMessage);

                        aiRequest.setQuery(message);
                        new AsyncTask<AIRequest,Void,AIResponse>(){

                            @Override
                            protected AIResponse doInBackground(AIRequest... aiRequests) {
                                final AIRequest request = aiRequests[0];
                                try {
                                    final AIResponse response = aiDataService.request(aiRequest);
                                    return response;
                                } catch (AIServiceException e) {
                                }
                                return null;
                            }
                            @Override
                            protected void onPostExecute(AIResponse response) {
                                if (response != null) {

                                    Result result = response.getResult();
                                    String reply = result.getFulfillment().getSpeech();
                                    ChatbotMessage chatMessage = new ChatbotMessage(reply, "bot");
                                    ref.child("chat").push().setValue(chatMessage);
                                }
                            }
                        }.execute(aiRequest);
                    }
                    else {
                        aiService.startListening();
                    }

                    editText.setText("");

                }
            });



            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    ImageView fab_img = (ImageView)findViewById(R.id.fab_img);
                    Bitmap img = BitmapFactory.decodeResource(getResources(),R.drawable.ic_send_white_24dp);
                    Bitmap img1 = BitmapFactory.decodeResource(getResources(),R.drawable.ic_mic_white_24dp);


                    if (s.toString().trim().length()!=0 && flagFab){
                        ImageViewAnimatedChange(ChatbotActivity.this,fab_img,img);
                        flagFab=false;

                    }
                    else if (s.toString().trim().length()==0){
                        ImageViewAnimatedChange(ChatbotActivity.this,fab_img,img1);
                        flagFab=true;

                    }


                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            adapter = new FirebaseRecyclerAdapter<ChatbotMessage, ChatbotRecycler>(ChatbotMessage.class,R.layout.activity_chatbot_msg,ChatbotRecycler.class,ref.child("chat")) {
                @Override
                protected void populateViewHolder(ChatbotRecycler viewHolder, ChatbotMessage model, int position) {

                    if (model.getMsgUser().equals("user")) {


                        viewHolder.rightText.setText(model.getMsgText());

                        viewHolder.rightText.setVisibility(View.VISIBLE);
                        viewHolder.leftText.setVisibility(View.GONE);
                    }
                    else {
                        viewHolder.leftText.setText(model.getMsgText());

                        viewHolder.rightText.setVisibility(View.GONE);
                        viewHolder.leftText.setVisibility(View.VISIBLE);
                    }
                }
            };

            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);

                    int msgCount = adapter.getItemCount();
                    int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                    if (lastVisiblePosition == -1 ||
                            (positionStart >= (msgCount - 1) &&
                                    lastVisiblePosition == (positionStart - 1))) {
                        recyclerView.scrollToPosition(positionStart);

                    }

                }
            });
            recyclerView.setAdapter(adapter);
        }

        public void ImageViewAnimatedChange(Context c, final ImageView v, final Bitmap new_image) {
            final Animation anim_out = AnimationUtils.loadAnimation(c, R.anim.zoom_out);
            final Animation anim_in  = AnimationUtils.loadAnimation(c, R.anim.zoom_in);
            anim_out.setAnimationListener(new Animation.AnimationListener()
            {
                @Override public void onAnimationStart(Animation animation) {}
                @Override public void onAnimationRepeat(Animation animation) {}
                @Override public void onAnimationEnd(Animation animation)
                {
                    v.setImageBitmap(new_image);
                    anim_in.setAnimationListener(new Animation.AnimationListener() {
                        @Override public void onAnimationStart(Animation animation) {}
                        @Override public void onAnimationRepeat(Animation animation) {}
                        @Override public void onAnimationEnd(Animation animation) {}
                    });
                    v.startAnimation(anim_in);
                }
            });
            v.startAnimation(anim_out);
        }

    @Override
    public void onResult(ai.api.model.AIResponse response) {
        Result result = response.getResult();

        String message = result.getResolvedQuery();
        ChatbotMessage chatMessage0 = new ChatbotMessage(message, "user");
        ref.child("chat").push().setValue(chatMessage0);


        String reply = result.getFulfillment().getSpeech();
        ChatbotMessage chatMessage = new ChatbotMessage(reply, "bot");
        ref.child("chat").push().setValue(chatMessage);
    }

    @Override
    public void onError(ai.api.model.AIError error) {
    }

    @Override
    public void onAudioLevel(float level) {
    }

    @Override
    public void onListeningStarted() {
    }

    @Override
    public void onListeningCanceled() {
    }

    @Override
    public void onListeningFinished() {
    }
}
