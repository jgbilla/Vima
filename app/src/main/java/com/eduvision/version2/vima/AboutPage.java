package com.eduvision.version2.vima;

import android.content.ClipData;
import android.os.Bundle;
import android.content.ClipboardManager;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class AboutPage extends AppCompatActivity {

    public void copy(View view){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        String text = "https://drive.google.com/file/d/18OVDD2T4OdoAyztCoe2ZF3AuVzMVVfXT/view?usp=sharing";
        ClipData clip = ClipData.newPlainText("label", text);
        clipboard.setPrimaryClip(clip);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_vima_page);
    }
}
