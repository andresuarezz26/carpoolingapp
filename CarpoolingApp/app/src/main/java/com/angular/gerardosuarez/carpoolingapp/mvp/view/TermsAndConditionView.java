package com.angular.gerardosuarez.carpoolingapp.mvp.view;

import android.net.http.SslError;
import android.support.design.widget.BottomNavigationView;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.activity.BaseActivity;
import com.angular.gerardosuarez.carpoolingapp.fragment.TermsAndConditionFragment;
import com.angular.gerardosuarez.carpoolingapp.mvp.base.FragmentView;

import butterknife.ButterKnife;

public class TermsAndConditionView extends FragmentView<TermsAndConditionFragment, Void> {

    private static final String TERMS_AND_CONDITION_URL = "https://www.miscupos.com/terminosycondiciones-tratdedatos";

    private BottomNavigationView bottomMenu;

    public TermsAndConditionView(TermsAndConditionFragment fragment) {
        super(fragment);
        if (fragment.getView() != null) {
            ButterKnife.bind(this, fragment.getView());
            if (getActivity() != null)
                bottomMenu = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        }
    }

    public void init() {
        final BaseActivity activity = getActivity();
        if (activity != null) {
            WebView webView = (WebView) activity.findViewById(R.id.webview);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.setWebViewClient(new SSLTolerentWebViewClient());
            webView.loadUrl(TERMS_AND_CONDITION_URL);
            hideMenu();
        }
    }

    private void hideMenu() {
        bottomMenu.setVisibility(View.GONE);
    }

    public void showMenu() {
        bottomMenu.setVisibility(View.VISIBLE);
    }

    private class SSLTolerentWebViewClient extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // Ignore SSL certificate errors
        }

    }
}
