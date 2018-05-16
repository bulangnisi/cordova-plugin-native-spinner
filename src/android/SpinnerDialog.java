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

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

public class SpinnerDialog extends CordovaPlugin {

  public Stack<ProgressDialog> spinnerDialogStack = new Stack<ProgressDialog>();

  public SpinnerDialog() {
  }

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
          
          ProgressDialog dialog;
          if (isFixed) {
            //If there is a progressDialog yet change the text
            if (!SpinnerDialog.this.spinnerDialogStack.empty()) {
              dialog = SpinnerDialog.this.spinnerDialogStack.peek(); 
              if (title != null) {
                dialog.setTitle(title);	
              }
              if (message!=null) {
                dialog.setMessage(message);	
              }
            }
            else{
              dialog = CallbackProgressDialog.show(cordova.getActivity(), title, message, true, false, null, callbackContext);
              SpinnerDialog.this.spinnerDialogStack.push(dialog);
            }
          } else {
            //If there is a progressDialog yet change the text
            if (!SpinnerDialog.this.spinnerDialogStack.empty()) {
              dialog = SpinnerDialog.this.spinnerDialogStack.peek(); 
              if (title != null) {
                dialog.setTitle(title);	
              }
              if (message!=null) {
                dialog.setMessage(message);	
              }	
            }
            else{
              dialog = ProgressDialog.show(cordova.getActivity(), title, message, true, true, onCancelListener);
              SpinnerDialog.this.spinnerDialogStack.push(dialog);
            }
          }
          
          if (title == null && message == null) {
            dialog.setContentView(new ProgressBar(cordova.getActivity()));
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
          }

          // add button
          // try {
          //   LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(      
          //       LinearLayout.LayoutParams.FILL_PARENT,      
          //       LinearLayout.LayoutParams.WRAP_CONTENT      
          //     );
          //   Button btn = new Button(cordova.getActivity());
          //   cordova.getActivity().addContentView(btn, bp);
          // } catch (Exception e) {
          //   AlertDialog alert1 = new AlertDialog.Builder(cordova.getActivity())
          //       .setTitle("标题")
          //       .setMessage(e.toString())
          //       .setNegativeButton("取消", new DialogInterface.OnClickListener() {
          //           @Override
          //           public void onClick(DialogInterface dialog, int which) {
          //           }
          //       })
          //       .setPositiveButton("确定", new DialogInterface.OnClickListener() {
          //           @Override
          //           public void onClick(DialogInterface dialog, int which) {
          //           }
          //       })
          //       .create();
          //       alert1.show();
          // }

          // add new 
          LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(      
            200,      
            200      
          );
    
          Resources r = cordova.getContext().getResources();
          int imgId = r.getIdentifier("img_loading", "drawable", cordova.getContext().getPackageName());
          ImageView loaderView = new ImageView(cordova.getActivity());
          
          loaderView.setImageResource(imgId);

          // ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(200,200);
          // Toast.makeText(cordova.getActivity(), "你好!", Toast.LENGTH_LONG).show();
          try {
            Toast.makeText(cordova.getActivity(), imgId, Toast.LENGTH_LONG).show();
            loaderView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)); 
            loaderView.setBackgroundColor(Color.RED);
            cordova.getActivity().addContentView(loaderView, p);  
          } catch (Exception e) {
            // Toast.makeText(cordova.getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            AlertDialog alert = new AlertDialog.Builder(cordova.getActivity())
            .setTitle("标题")
            .setMessage(e.toString())
            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            })
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            })
            .create();
            alert.show();
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
        }
      };
      this.cordova.getActivity().runOnUiThread(runnable);
    }
    
    return true;
  }
}
