package com.example.videodownloader;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etUrl;
    private Button btnDownload;
    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUrl = findViewById(R.id.etUrl);
        btnDownload = findViewById(R.id.btnDownload);
        tvStatus = findViewById(R.id.tvStatus);

        btnDownload.setOnClickListener(v -> {
            String url = etUrl.getText().toString().trim();
            if (url.isEmpty()) {
                Toast.makeText(MainActivity.this, "الرجاء إدخال الرابط أولاً", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.WEB_URL.matcher(url).matches()) {
                Toast.makeText(MainActivity.this, "الرابط غير صالح", Toast.LENGTH_SHORT).show();
                return;
            }

            startFileDownload(url);
        });
    }

    private void startFileDownload(String fileUrl) {
        try {
            Uri uri = Uri.parse(fileUrl);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setTitle("جاري تحميل الملف...");
            request.setDescription(fileUrl);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            
            String fileName = URLUtil.guessFileName(fileUrl, null, null);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            if (downloadManager != null) {
                downloadManager.enqueue(request);
                tvStatus.setText("حالة التحميل: بدأ التحميل في مجلد Downloads");
                Toast.makeText(this, "تم بدء التحميل بنجاح!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            tvStatus.setText("خطأ في التحميل: " + e.getMessage());
            Toast.makeText(this, "فشل التحميل: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
