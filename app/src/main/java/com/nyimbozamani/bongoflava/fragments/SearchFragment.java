package com.nyimbozamani.nyimbozazamani.fragments;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.flyco.animation.ZoomEnter.ZoomInTopEnter;
import com.flyco.animation.ZoomExit.ZoomOutBottomExit;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import com.nyimbozamani.nyimbozazamani.database.Chat;
import com.nyimbozamani.nyimbozazamani.database.ChatHolder;
import com.nyimbozamani.nyimbozazamani.FlycoMenuDialog;
import com.nyimbozamani.nyimbozazamani.R;
import com.nyimbozamani.nyimbozazamani.SendIntentHelper;

import static android.content.ContentValues.TAG;
import android.os.Handler;
public class SearchFragment extends Fragment {

    Typeface font;
    DownloadManager downloadManager;
    private long myDownloadReference;
    private Button mSendButton;

    private EditText mMessageEdit;
    private RecyclerView mMessages;
    private LinearLayoutManager mManager;
    private FirebaseRecyclerAdapter<Chat, ChatHolder> mAdapter;
    private TextView mEmptyListMessage;
    EditText searchText;
    ListView lv;
    ArrayList<HashMap<String, String>> trackList;
    InputMethodManager inputManager;
    SendIntentHelper sendIntentHelper;
    ConnectivityManager cm;
    NetworkInfo activeNetwork;
    Button searchButton;
    private DatabaseReference mRef;
    private InterstitialAd mInterstitialAd;
    private DatabaseReference mChatRef;
    ProgressDialog pDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.searchfragment_layout, container, false);
        downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
       font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/QuicksandRegular.otf");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId("ca-app-pub-5942769310685348/9251031316");
        // [END instantiate_interstitial_ad]

        // [START create_interstitial_ad_listener]
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                //   beginSecondActivity();
            }

            @Override
            public void onAdLoaded() {
                // Ad received, ready to display
                // [START_EXCLUDE]

                // [END_EXCLUDE]
            }

            @Override
            public void onAdFailedToLoad(int i) {
                // See https://goo.gl/sCZj0H for possible error codes.
                Log.w(TAG, "onAdFailedToLoad:" + i);
            }
        });
        mRef = FirebaseDatabase.getInstance().getReference();
        mChatRef = mRef.child("audios");

        mManager = new LinearLayoutManager(getActivity());
     //   mManager.setReverseLayout(false);
        mMessages = (RecyclerView) v.findViewById(R.id.recycle_viewwwww);
       // mMessages.setHasFixedSize(false);
        mMessages.setLayoutManager(mManager);
        inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
      //  searchButton = (Button)v.findViewById(R.id.search_button);
     //   searchText = (EditText)v.findViewById(R.id.edittext_query);


        //dlSongs = new ArrayList<Song>();


        /*searchText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    inputManager.toggleSoftInput(0,0);
                }
            }
        });*/


        sendIntentHelper = new SendIntentHelper();
       // String text = sendIntentHelper.cleanUpText(getActivity().getIntent());
      //  searchText.setText(text);
 /*       searchButton.setEnabled(!searchText.getText().toString().trim().isEmpty());

        searchText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                searchButton.setEnabled(!searchText.getText().toString().trim().isEmpty());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchButton.setEnabled(!searchText.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchButton.setEnabled(!searchText.getText().toString().trim().isEmpty());
            }
        });*/



       /* searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!searchButton.isEnabled()) return handled;
                    if (isConnected()) {

                    } else {
                        //display dialog
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());

                        alertDialogBuilder.setTitle(R.string.network_dialog_title);
                        alertDialogBuilder.setMessage(R.string.network_dialog_message);

                        //set neutral button OK message
                        alertDialogBuilder.setNeutralButton(R.string.network_dialog_button_text, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }


                    //inputManager.hideSoftInputFromWindow((null == getActivity().getCurrentFocus()) ? null : getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    handled = true;
                }


                return handled;
            }
        });*/



   /*     searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!searchText.getText().toString().trim().isEmpty()) {


                    if (isConnected()) {
                     //  searchNyimbo();
                    } else {
                        //display dialog
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());

                        alertDialogBuilder.setTitle(R.string.network_dialog_title);
                        alertDialogBuilder.setMessage(R.string.network_dialog_message);

                        //set neutral button OK message
                        alertDialogBuilder.setNeutralButton(R.string.network_dialog_button_text, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                }

                inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                //inputManager.hideSoftInputFromWindow((null == getActivity().getCurrentFocus()) ? null : getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });*/
requestNewInterstitial();


        return v;

    }

    @Override
    public void onResume() {
        super.onResume();

        if (!mInterstitialAd.isLoaded()) {
            requestNewInterstitial();
        }

    }
    @Override
    public void onStart() {
        super.onStart();

        // Default Database rules do not allow unauthenticated reads, so we need to
        // sign in before attaching the RecyclerView adapter otherwise the Adapter will
        // not be able to read any data from the Database.

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Searching...");
        pDialog.setCancelable(true);
        pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                pDialog.dismiss();;
            }
        });
        pDialog.show();

        attachRecyclerViewAdapter();


    }


    public boolean isConnected() {
        boolean connected;

        cm = (ConnectivityManager) getActivity().getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();
        connected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return connected;
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private void attachRecyclerViewAdapter() {

        Query lastFifty = mChatRef;
        mAdapter = new FirebaseRecyclerAdapter<Chat, ChatHolder>(
                Chat.class, R.layout.list_item, ChatHolder.class, lastFifty) {
            @Override
            public void populateViewHolder(ChatHolder holder, final Chat chat, final int position) {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }

                holder.setName(chat.getName());
                holder.setArtist(chat.getArtist());
              //  holder.setDirect(chat.getDownloadUrl());
                holder.setFont(font);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String name = ((TextView) view.findViewById(R.id.name)).getText().toString().trim();
                        final String artist = ((TextView) view.findViewById(R.id.artist)).getText().toString().trim();
                        String o_name = name.replaceAll(" ", "_").replaceAll("\\(\\d*:\\d*\\)", "").replaceAll(":", "_").toLowerCase().trim();
                        String o_artist = artist.replaceAll(" ", "_").toLowerCase().trim();
                        final String outFile = o_name + "_" + o_artist + ".mp3";          //_musicbandit.mp3";
                        Log.d("outFile", outFile);
                        //remove spaces from artist

                        //check if File exists before
                        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                        File file = new File(path, outFile);
                        Log.d("FILE TO WRITE", file.toString());
                        if (file.exists()) {
                            System.out.println("FILE EXISTS");
                            Toast.makeText(getActivity(), "Song file already exists. Delete current file first.", Toast.LENGTH_LONG).show();
                            return;
                        }



                        if (isConnected()) {
                            Log.d("FILE TO WRITE", file.toString());
                            new ProgressDialog.Builder(getActivity())
                                    .setTitle("Download Song")
                                    .setMessage("Download song: " + name + "?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    //make sure not currently downloading file already
                                                    final Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            // Do something after 5s = 5000ms
                                                            if (mInterstitialAd.isLoaded()) {
                                                                mInterstitialAd.show();
                                                            }
                                                        }
                                                    }, 10000);

                                                    Toast.makeText(getActivity(), "Starting download...", Toast.LENGTH_SHORT).show();

                                                    //handle the download....whoo hoo, bitches!!!
                                                    Uri uri = Uri.parse(chat.getDownloadUrl().toString());
                                                    DownloadManager.Request request = new DownloadManager.Request(uri);
                                                    request.setMimeType("audio/mpeg");
                                                    request.setTitle(name);
                                                    request.setDescription("Artist: " + artist);
                                                    request.allowScanningByMediaScanner();
                                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);


                                                    //request.addRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

                                                    if(isExternalStorageWritable()){
                                                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, outFile);
                                                        //request.setDestinationInExternalFilesDir(MainActivity.this,"mb_music", name + "_" + artist + ".mp3");
                                                        myDownloadReference = downloadManager.enqueue(request);
                                                        Log.d("myDownloadReference", String.valueOf(myDownloadReference));
                                                    }else{
                                                        FlycoMenuDialog noWriteableStorageDialog = new FlycoMenuDialog(getActivity(),new ZoomInTopEnter(), new ZoomOutBottomExit(), getActivity().getResources().getString(R.string.no_writeable_storage_title),getActivity().getResources().getString(R.string.no_writeable_storage_content), "Okay");
                                                        noWriteableStorageDialog.showMaterialDialog();
                                                    }



                                                }
                                            }

                                    )
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Toast.makeText(getActivity(), "Selection cancelled", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                    ).

                                    show();
                        } else {
                            //display dialog
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                            alertDialogBuilder.setTitle(R.string.network_dialog_title);
                            alertDialogBuilder.setMessage(R.string.network_dialog_message);

                            //set neutral button OK message
                            alertDialogBuilder.setNeutralButton(R.string.network_dialog_button_text, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }


                    }
                });
               // FirebaseUser currentUser = mAuth.getCurrentUser();



            }

            @Override
            protected void onDataChanged() {
                // If there are no chat messages, show a view that invites the user to add a message.
               // mEmptyListMessage.setVisibility(mAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        };

        // Scroll to bottom on new messages
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
             //   mManager.smoothScrollToPosition(mMessages, null, mAdapter.getItemCount());
            }

        });
        mMessages.setAdapter(mAdapter);
    }
    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        mInterstitialAd.loadAd(adRequest);

    }
}
