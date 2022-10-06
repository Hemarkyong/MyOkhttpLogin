package com.example.myokhttplogin;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    EditText usernameText;
    EditText paswordText;
    TextView resultTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        usernameText=findViewById(R.id.usernameText);
        paswordText = findViewById(R.id.passwordText);
        resultTextView = findViewById(R.id.resultTextView);
        Button getLoginBtn = findViewById(R.id.getLoginBtn);
        Button postLoginBtn = findViewById(R.id.postLoginBtn);
        getLoginBtn.setOnClickListener(this);
        postLoginBtn.setOnClickListener(this);
    }
    Callback callback = new Callback() {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            Log.e("onFailure",e.getMessage());
        }

        // android.view.ViewRootImpl$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            String responseStr = response.body().string();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Gson gson = new Gson();
                    ResutlResponse result = gson.fromJson(responseStr,ResutlResponse.class);
                    if(result.isResult())
                    {
                        resultTextView.setText(result.getMessage()+":登录成功");
                    }else{
                        resultTextView.setText(result.getMessage()+":登录失败");
                    }
                }
            });

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        String url="http://192.168.0.105:7556/okhttp/";

        String username = usernameText.getText().toString();
        String passwrod = paswordText.getText().toString();

        switch (v.getId())
        {
            case R.id.getLoginBtn:
                String u = url+"getLogin?username="+username+"&password="+passwrod;
                OkhttpUtil.getLogin(u, callback);
                break;
            case R.id.postLoginBtn:
                u = url+"postLogin";
                OkhttpUtil.postLogin(u,username,passwrod, callback);
                break;
        }
    }
}