package com.example.appuva;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Modifica o título da top bar
        getSupportActionBar().setTitle("AppUni");

        // Mostra a progress bar enquanto a página carrega
        final ProgressBar progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        // Abre a página e extende para que se mantenha na WebView
        WebView webView = findViewById(R.id.webview);

        // Habilita cache da página para melhor performance
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        webView.loadUrl("https://portalaluno.uva.br/LoginMobile");
        webView.getSettings().setJavaScriptEnabled(true);


        // Durante o carregamento da página, a progressbar é vista para dar um feedback de loading
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            // E após carregar, ela é escondida
            @Override
            public void onPageFinished(WebView view, String url) {

                // Classe com injeção do JS para estética usando Thread do Java
                JavaScriptInjection javaScriptInjection = new JavaScriptInjection();

                javaScriptInjection.start();

                javaScriptInjection.pseudoDarkTheme(view);
                javaScriptInjection.beautyTools(view);
                javaScriptInjection.removeElement(view);

                progressBar.setVisibility(View.INVISIBLE);
            }

            // Caso o usuário esteja offline, indica que é preciso ter uma conexão de internet ativa
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                // webView.loadUrl("about:blank");
                webView.loadUrl("file:///android_asset/errorView.html");
                Toast.makeText(MainActivity.this, "Error de conexão. É necessário estar conectado a internet para usar este aplicativo!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Caso seja possível retornar a página anterior, o botão de retornar terá essa ação
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}