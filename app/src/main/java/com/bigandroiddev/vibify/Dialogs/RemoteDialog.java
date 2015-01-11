package com.bigandroiddev.vibify.Dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.bigandroiddev.vibify.R;

/**
 * Created by spiros on 11/30/14.
 */
public class RemoteDialog extends Activity {

    private WebView webView;
    private Button button;
    private Activity currentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentActivity = this;
        setContentView(R.layout.remote_dialog);

        webView = (WebView) findViewById(R.id.remote_dialog_webview);
        button = (Button) findViewById(R.id.remote_dialog_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentActivity.finish();
            }
        });

        webView.loadData(getIntent().getExtras().getString("remote_content"), "text/html", "UTF-8");
    }
}
