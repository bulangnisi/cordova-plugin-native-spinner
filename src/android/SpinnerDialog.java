// cordova-plugin-native-spinner
package com.greybax.spinnerdialog;

import java.util.Stack;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.res.Resources;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.widget.ProgressBar;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.view.ViewGroup;
import android.app.AlertDialog;
import android.view.View;
import android.webkit.WebView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.webkit.WebSettings.*;

public class SpinnerDialog extends CordovaPlugin {

  public Stack<ProgressDialog> spinnerDialogStack = new Stack<ProgressDialog>();

  public SpinnerDialog() {
    
  }

  WebView loaderView = null;

  public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {

    if (action.equals("show")) {

      final String title = "null".equals(args.getString(0)) ? null : args.getString(0);
      final String message = "null".equals(args.getString(1)) ? null : args.getString(1);
      final boolean isFixed = args.getBoolean(2);
                
      final CordovaInterface cordova = this.cordova;


      Runnable runnable = new Runnable() {
        public void run() {
          
          DialogInterface.OnCancelListener onCancelListener = new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
              if (!isFixed) {
                while (!SpinnerDialog.this.spinnerDialogStack.empty()) {
                  SpinnerDialog.this.spinnerDialogStack.pop().dismiss();
                  callbackContext.success();
                }
              }
            }
          };
          
          if(loaderView == null){
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            loaderView = new WebView(cordova.getActivity());
            String gifFilePath = "file:///android_asset/img_loading.webp";
            loaderView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
            String data = "<HTML><Div align=\"center\" margin=\"0px\" style=\"width:100%; height:100%; display:flex; align-items:center; justify-content:center;\"><IMG style=\"width:120px;\" src=\"" + gifFilePath + "\" margin=\"0px\"/></Div>";//设置图片位于webview的中间位置
            loaderView.loadDataWithBaseURL(gifFilePath, data, "text/html", "utf-8", null);
            loaderView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)); 
            loaderView.setBackgroundColor(0x61000000);
            cordova.getActivity().addContentView(loaderView, p);  
          }else{
            loaderView.setVisibility(View.VISIBLE);
          }
        }
      };


      this.cordova.getActivity().runOnUiThread(runnable);

    } else if (action.equals("hide")) {
      Runnable runnable = new Runnable() {
        public void run() {
          if (!SpinnerDialog.this.spinnerDialogStack.empty()) {
            SpinnerDialog.this.spinnerDialogStack.pop().dismiss();
          }
          loaderView.setVisibility(View.GONE);
        }
      };
      this.cordova.getActivity().runOnUiThread(runnable);
    }
    
    return true;
  }
}
