package com.cliknfit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cliknfit.R;
import com.cliknfit.customviews.RegulerText;
import com.cliknfit.interfaces.ApiResponse;
import com.cliknfit.pojo.CommonStatusResultObj;
import com.cliknfit.util.Alerts;
import com.cliknfit.util.AppPreference;
import com.cliknfit.util.CommonAsyncTask;
import com.cliknfit.util.Constants;
import com.cliknfit.util.MyAppWebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrivacyPolicy extends AppCompatActivity implements ApiResponse{

    WebView webview;
    TextView accept;
    private RelativeLayout header,footer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!getIntent().hasExtra("no")){
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_privacy_policy);
        webview = (WebView) findViewById(R.id.webView);
        accept = (TextView) findViewById(R.id.accept);
        header = (RelativeLayout) findViewById(R.id.header);
        footer = (RelativeLayout) findViewById(R.id.footer);

        WebSettings webSettings = webview.getSettings();
        webSettings.setSupportZoom(true);
        String url = null;
        if(getIntent().hasExtra("no")){
            setTitle("ClikNFit");

            header.setVisibility(View.GONE);
            footer.setVisibility(View.GONE);
            url="https://www.cliknfit.com/";
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }else {
            url = "https://www.cliknfit.com/terms/";
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    acceptMethod();
                }
            });
        }
        webview.loadUrl(url);
        webview.setWebViewClient(new MyAppWebViewClient(this));

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void acceptMethod() {
        CommonAsyncTask ca = new CommonAsyncTask(this);
        ca.agreeTncTask("accept");
    }

    @Override
    public void getResponse(Object response, String service) {
        CommonStatusResultObj parentModel = (CommonStatusResultObj) response;
        if (parentModel.isError())
            Alerts.okAlert(PrivacyPolicy.this, parentModel.getMessage(), "");
        else {
            AppPreference.setPreference(PrivacyPolicy.this, Constants.AGREE, "1");
            startActivity(new Intent(getApplicationContext(), QNA.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }
    }
}
