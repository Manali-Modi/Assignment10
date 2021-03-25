package com.example.assignment10;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

public class AudioFragment extends Fragment {

    private static final int R_CODE = 123;
    ListView lstAudio;
    ArrayList<String> audioList;

    public AudioFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio, container, false);
        lstAudio = view.findViewById(R.id.lst_audio);
        audioList = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            getAudios();
            loadAudios();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, R_CODE);
        }
        return view;
    }

    private void loadAudios() {
        lstAudio.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, audioList));
        /*lstAudio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MediaPlayer mp = MediaPlayer.create(getContext(), );
                mp.start();
            }
        });*/
    }

    private void getAudios() {
        ContentResolver cr = getContext().getContentResolver();
        Cursor cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String audio = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                audioList.add(audio);
            }
            cursor.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == R_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getAudios();
            loadAudios();
        }
    }
}