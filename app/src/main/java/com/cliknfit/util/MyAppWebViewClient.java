package com.cliknfit.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.support.v7.app.AlertDialog;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Prince on 7/12/2016.
 */
public class MyAppWebViewClient extends WebViewClient {
    private final ProgressDialog progress;
    private Context context;

    public MyAppWebViewClient(Context context) {
        this.context = context;
        progress = new ProgressDialog(context);
        progress.setMessage("Please Wait ...");
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
       progress.show();
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
      progress.dismiss();
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }


    @Override
    public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {

        String message="";
        switch (error.getPrimaryError()) {
            case SslError.SSL_DATE_INVALID:
            //    message = context.getResources().getString(R.string.ssl_cert_error_date_invalid);
                break;
            case SslError.SSL_EXPIRED:
            //    message = context.getResources().getString(R.string.ssl_cert_error_expired);
                break;
            case SslError.SSL_IDMISMATCH:
            //    message = context.getResources().getString(R.string.ssl_cert_error_idmismatch);
                break;
            case SslError.SSL_INVALID:
             //   message = context.getResources().getString(R.string.ssl_cert_error_invalid);
                break;
            case SslError.SSL_NOTYETVALID:
             //   message = context.getResources().getString(R.string.ssl_cert_error_not_yet_valid);
                break;
            case SslError.SSL_UNTRUSTED:
             //   message = context.getResources().getString(R.string.ssl_cert_error_untrusted);
                break;
            default:
             //   message = context.getResources().getString(R.string.ssl_cert_error_cert_invalid);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("SSL Certificate Error");
        alertDialog.setMessage(message);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.proceed();
            }
        });

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.cancel();
            }
        });
        alertDialog.show();
    }

}
