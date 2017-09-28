package com.angular.gerardosuarez.carpoolingapp.mvp.view;

import android.support.design.widget.BottomNavigationView;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.activity.BaseActivity;
import com.angular.gerardosuarez.carpoolingapp.fragment.TermsAndConditionFragment;
import com.angular.gerardosuarez.carpoolingapp.mvp.base.FragmentView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TermsAndConditionView extends FragmentView<TermsAndConditionFragment, Void> {

    private static final String TERMS_AND_CONDITION_URL = "https://www.miscupos.com/terminosycondiciones-tratdedatos";

    private BottomNavigationView bottomMenu;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.btn_continue)
    Button btnContinue;
    @BindView(R.id.radio_button_container)
    LinearLayout radioButtonContainer;

    private WebView webView;


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
            progressBar.setVisibility(View.VISIBLE);
            btnContinue.setVisibility(View.GONE);
            radioButtonContainer.setVisibility(View.GONE);
            webView = (WebView) activity.findViewById(R.id.webview);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.setWebViewClient(new SSLTolerentWebViewClient());
            webView.loadUrl(TERMS_AND_CONDITION_URL);
            webView.setVisibility(View.GONE);
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
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            btnContinue.setVisibility(View.VISIBLE);
            radioButtonContainer.setVisibility(View.VISIBLE);
            webView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), "No se puede conectar ahora. Intente más tarde", Toast.LENGTH_LONG).show();
        }
    }
}
