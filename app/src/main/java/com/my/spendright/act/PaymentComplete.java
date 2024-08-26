package com.my.spendright.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.my.spendright.R;
import com.my.spendright.databinding.ActivityPaymentCompleteBinding;
import com.my.spendright.databinding.DialogFullscreenBinding;
import com.my.spendright.databinding.DialogFullscreenPdfBinding;
import com.my.spendright.utils.SessionManager;

public class PaymentComplete extends AppCompatActivity {

    ActivityPaymentCompleteBinding binding;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_payment_complete);
        sessionManager = new SessionManager(PaymentComplete.this);

        binding.RRComplete.setOnClickListener(v -> {
            startActivity(new Intent(PaymentComplete.this,HomeActivity.class));
            finish();
        });

        binding.RRViewReceipt.setOnClickListener(v -> {
            fullscreenDialog(this);
        });

    }




    public void fullscreenDialog(Context context){
        Dialog dialogFullscreen = new Dialog(context, WindowManager.LayoutParams.MATCH_PARENT);

        DialogFullscreenPdfBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog_fullscreen_pdf, null, false);
        dialogFullscreen.setContentView(dialogBinding.getRoot());

       // WebSettings webSettings = dialogBinding.webView.getSettings();
       // webSettings.setJavaScriptEnabled(true);
     //   webSettings.setBuiltInZoomControls(true);
     //   webSettings.setDisplayZoomControls(false);
        String url =  "https://docs.google.com/viewer?url="+ "https://spendright.ng/webservice/invoice_me?transaction_id="+sessionManager.getTransId();


     //   dialogBinding.webView.loadUrl(url);

        Log.e("pdf url===",url);

        dialogBinding.imgBack.setOnClickListener(view -> dialogFullscreen.dismiss());

        dialogBinding.ivDownload.setOnClickListener(view -> Download_PDF_Internal_Storage( "https://spendright.ng/webservice/invoice_me?transaction_id="+sessionManager.getTransId(),PaymentComplete.this));

        dialogBinding.ivShare.setOnClickListener(view ->{
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Spendright");
                String shareMessage = "https://spendright.ng/webservice/invoice_me?transaction_id="+sessionManager.getTransId();
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "Share"));


            } );


        dialogBinding.webView.getSettings().setJavaScriptEnabled(true);

        dialogBinding.webView.getSettings().setLoadWithOverviewMode(true);
        dialogBinding.webView.getSettings().setDomStorageEnabled(true);
        dialogBinding.webView.getSettings().setBuiltInZoomControls(true);
        dialogBinding.webView.getSettings().setUseWideViewPort(true);
        dialogBinding.webView.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    Log.e("url===",url);
                }
            });
        dialogBinding.webView.loadUrl(url);


        dialogFullscreen.show();

    }

    private void Download_PDF_Internal_Storage(String url,Context context) {
        Uri Download_ = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(Download_);

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle("PDF File Download");
        request.setDescription("Downloading " + "pdf" + " file using Download Manager.");
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "pdf_name.pdf");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);

        Toast.makeText(context, "PDF Download Started", Toast.LENGTH_SHORT).show();

        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                // Do something on download complete
                Toast.makeText(ctxt, "file downloaded...", Toast.LENGTH_SHORT).show();
            }
        };

        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }


    @Override
    public void onBackPressed() {
     //   super.onBackPressed();
    }
}