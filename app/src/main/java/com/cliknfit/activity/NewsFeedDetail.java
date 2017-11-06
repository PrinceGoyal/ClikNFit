package com.cliknfit.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cliknfit.R;
import com.cliknfit.util.Constants;
import com.squareup.picasso.Picasso;

public class NewsFeedDetail extends AppCompatActivity {

    private ImageView banner;
    private TextView title,description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed_detail);
        initViews();
    }

    private void initViews() {
        banner=(ImageView)findViewById(R.id.banner);
        title=(TextView) findViewById(R.id.title);
        description=(TextView) findViewById(R.id.description);

        title.setText(getIntent().getStringExtra("title"));
        String imageid = Constants.PICBASE_URL + "" + getIntent().getStringExtra("image");
        Picasso.with(this)
                .load(imageid)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(banner);

        String htmlAsString = getIntent().getStringExtra("description");      // used by WebView
        Spanned htmlAsSpanned = Html.fromHtml(htmlAsString);
        description.setText(htmlAsSpanned);

       /* WebView webview=(WebView)findViewById(R.id.webView);
        webview.loadDataWithBaseURL(null, getIntent().getStringExtra("description"), "text/html", "utf-8", null);*/

    }
}
