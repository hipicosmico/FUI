package mx.editorfui.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends Activity {
    private WebView webView;
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface public void printPage() {
                runOnUiThread(() -> printLetterPdf());
            }
        }, "AndroidBridge");
        setContentView(webView);
        webView.loadUrl("file:///android_asset/index.html");
    }
    private void printLetterPdf() {
        try {
            PrintManager pm = (PrintManager) getSystemService(Context.PRINT_SERVICE);
            PrintAttributes attrs = new PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.NA_LETTER)
                .setResolution(new android.print.PrintAttributes.Resolution("pdf", "PDF", 300, 300))
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                .build();
            pm.print("Formato_FUI", webView.createPrintDocumentAdapter("Formato_FUI"), attrs);
        } catch (Exception e) {
            Toast.makeText(this, "No se pudo abrir la impresión: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    @Override public void onBackPressed() {
        new android.app.AlertDialog.Builder(this).setMessage("¿Deseas salir del Editor FUI?")
            .setNegativeButton("Cancelar", (d,w)->d.dismiss())
            .setPositiveButton("Salir", (d,w)->finish()).show();
    }
}
