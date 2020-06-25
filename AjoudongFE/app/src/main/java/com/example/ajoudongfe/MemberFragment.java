package com.example.ajoudongfe;

import android.app.DownloadManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MemberFragment extends Fragment {


    final String TAG = getClass().getSimpleName();
    private final int GET_GALLERY_IMAGE = 200;
    private final int ACTIVITY_CHOOSE_FILE1 = 350;

    private RecyclerView MemberRecyclerView;
    private MemberRecyclerAdapter MemberRecyclerAdapter;
    private int clubID;
    private List<MemberInfoObject> listData = new ArrayList<>();
    private Retrofit retrofit;
    private Retrofit csv_retrofit;
    public static String BASE_URL= Keys.getServerUrl();
    private Button addNewMemberButton;
    private Button memberCSVButton;
    private DownloadManager downloadManager;
    private DownloadManager.Request request;
    private long downloadQueueID;
    private String filename;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        final ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_member, container, false);
        addNewMemberButton = (Button)rootView.findViewById(R.id.addNewMemberButton);
        memberCSVButton = (Button)rootView.findViewById(R.id.memberCSVButton);
        MemberRecyclerView = (RecyclerView) rootView.findViewById(R.id.memberRecyclerView);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        Bundle bundle = getArguments();
        clubID = bundle.getInt("clubID");
        downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);



        addNewMemberButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                //NewMemberPopup newMemberPopup = new NewMemberPopup(clubID);

                //newMemberPopup.show(getFragmentManager(), "dialog");
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("text/*");
                startActivityForResult(Intent.createChooser(intent, "Open CSV"), ACTIVITY_CHOOSE_FILE1);

            }
        });

        memberCSVButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadMemberCSV(BASE_URL + "/management/membercsv/" + Integer.toString(clubID));
            }
        });

        init();

        return rootView;
    }

    private void init() {
        Call<UserListObject> call = getMemberList(clubID);

        call.enqueue(new Callback<UserListObject>() {
            @Override
            public void onResponse(Call<UserListObject> call, Response<UserListObject> response) {
                UserListObject data = response.body();
                listData = data.getContent();
                for (int i = 0; i < listData.size(); i++) {
                    System.out.println(listData.get(i).getuSchoolID());
                }

                MemberRecyclerAdapter = new MemberRecyclerAdapter(getActivity(), listData, clubID);
                MemberRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                MemberRecyclerView.setAdapter(MemberRecyclerAdapter);

            }

            @Override
            public void onFailure(Call<UserListObject> call, Throwable t) {
                t.printStackTrace();
                Log.e("연결실패", "실패");
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ACTIVITY_CHOOSE_FILE1 && data != null && data.getData() != null) {
            RetroService retroService = retrofit.create(RetroService.class);

            String filePath = null;
            Uri _uri = data.getData();
            Log.d("","URI = "+ _uri);
            filePath = getPath(getActivity().getApplicationContext(), _uri);
            Log.d("","Chosen path = "+ filePath);


            File file = new File(Objects.requireNonNull(filePath));
            Log.d("FILENAME", file.getName());
            RequestBody requestFile = RequestBody.create(MediaType.parse("text/csv"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("members.csv", file.getName(), requestFile);
            String descriptionString = "Member list by csv for updating";
            Log.d("desc", descriptionString);
            RequestBody description = RequestBody.create(okhttp3.MultipartBody.FORM, descriptionString);

            Call<ResponseObject> call = retroService.uploadCSV(body, clubID);

            call.enqueue(new Callback<ResponseObject>() {
                @Override
                public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                    if(response.isSuccessful())
                    {

                        Log.e("결과", "성공");
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "잘못된 파일입니다.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseObject> call, Throwable t) {
                    Log.e("결과", "실패");
                    t.printStackTrace();
                }
            });


        }
    }


    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void downloadMemberCSV(String URL) {

        Log.d("URL", URL);
        request = new DownloadManager.Request(Uri.parse(URL))
                .setDescription("Downloaind")
                .setTitle("members.csv")
                .setVisibleInDownloadsUi(true)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "members.csv");
        Log.d("adf", "enas");
        downloadQueueID = downloadManager.enqueue(request);



    }

    private Call<FileObject> getMemberCSV(int clubID) {
        RetroService retroService = retrofit.create(RetroService.class);
        return retroService.getMemberCSV(clubID);

    }

    private Call<UserListObject> getMemberList(int clubID)
    {
        RetroService retroService = retrofit.create(RetroService.class);
        return retroService.getMemberList(clubID);
    }


    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/all_downloads"), Long.valueOf(id));
                Log.d("", contentUri.getPath());
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, null, selection, selectionArgs,
                    null);
            Log.d("", "cursor");
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                Log.d("", "cursor : " + cursor.getString(column_index));
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}
