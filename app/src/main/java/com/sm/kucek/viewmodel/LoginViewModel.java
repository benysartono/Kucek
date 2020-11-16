package com.sm.kucek.viewmodel;

import android.content.Context;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.sm.kucek.model.ModelUser;
import com.sm.kucek.view.activity.MainActivity;
import com.sm.kucek.view.activity.RegisterActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by tofin on 21/04/16.
 */
public class LoginViewModel implements ViewModel {
    private Context context;
    private String strRespUser;
    public ObservableField<String> loginMessage;
    public ObservableInt loginMessageVisibility;
    private String editTextUsernameValue = "";
    private String editTextPasswordValue = "";
    private URL urlObj;
    private HttpURLConnection conn;
    private InputStreamReader isReader;
    private OutputStreamWriter isWriter;
    private BufferedReader bufReader;
    private BufferedWriter buffWriter;
    private StringBuffer readTextBuf = new StringBuffer();
    private String url;
    private String urlParam;
    private ModelUser user;
    private JSONObject jsonObject;

    public LoginViewModel(Context context) throws UnsupportedEncodingException {
        this.context = context;
        this.loginMessage = new ObservableField<>("");
        this.loginMessageVisibility = new ObservableInt(View.INVISIBLE);
        if ( !(ModelUser.getRegisteredUser(context).equals("")) && !(ModelUser.getRegisteredPass(context).equals(""))) {
            url = "https://otokebon.com/laundry/webservice/login.php";
            try {
                jsonObject = new JSONObject();
                jsonObject.put("iduser",ModelUser.getRegisteredUser(context));
                jsonObject.put("pwd",sha256(ModelUser.getRegisteredPass(context)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            execLogin();
        }
    }

    private void execLogin() {
        loginMessage.set("");
        loginMessageVisibility.set(View.INVISIBLE);
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    urlObj = new URL(url);
                    conn = (HttpURLConnection) urlObj.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    // Get input stream from web url connection.
                    DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
                    // Create input stream reader based on url connection input stream.
                    outputStream.write(jsonObject.toString().getBytes("UTF-8"));
                    outputStream.flush();
                    outputStream.close();
                    int responseCode=conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in=new BufferedReader( new InputStreamReader(conn.getInputStream()));
                        StringBuffer sb = new StringBuffer("");
                        String line="";
                        while((line = in.readLine()) != null) {
                            Log.d("Line-nya: ", line);
                            sb.append(line);
                            break;
                        }
                        in.close();
                        strRespUser = sb.toString();
                        try {
                            jsonObject = new JSONObject(strRespUser);
                        } catch(Exception e){
                            loginMessage.set("Wrong Username or Password");
                            loginMessageVisibility.set(View.VISIBLE);
                        }
                        if (!(strRespUser == null) && !(strRespUser.equals(""))) {
                            if (!(jsonObject.getString("nmuser").equals("")) && !(jsonObject.getString("iduser").equals(""))) {
                                user = new ModelUser(context, jsonObject.getString("iduser"), editTextPasswordValue);
                            }
                            /*
                            if (!(ModelUser.getRegisteredUser(context).equals("")) && !(ModelUser.getRegisteredPass(context).equals(""))) {
                                user = new ModelUser(context, ModelUser.getRegisteredUser(context), ModelUser.getRegisteredPass(context));
                            }

                             */
                            context.startActivity(MainActivity.newIntent(context, user));
                        } else {
                            loginMessage.set("Wrong Username or Password");
                            loginMessageVisibility.set(View.VISIBLE);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void registerSubmit(View view){
        context.startActivity(RegisterActivity.newIntent(context));
    }

    public void loginAuthentication(View view) {
        if ( !(editTextUsernameValue.equals("")) && !(editTextPasswordValue.equals(""))) {
            url = "https://otokebon.com/laundry/webservice/login.php";
            //url = "https://otokebon.com/laundry/webservice/login.php?iduser=" + editTextUsernameValue + "&pwd=" + editTextPasswordValue;
            try {
                jsonObject = new JSONObject();
                jsonObject.put("iduser",editTextUsernameValue);
                jsonObject.put("pwd",sha256(editTextPasswordValue));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            execLogin();
        }
        else if ( (editTextUsernameValue.equals("")) || (editTextPasswordValue.equals(""))) {
            loginMessage.set("Username or Password can't be empty!");
            loginMessageVisibility.set(View.VISIBLE);
        }
        else {
            loginMessage.set("Wrong username or password");
            loginMessageVisibility.set(View.VISIBLE);
        }
    }

    public TextWatcher getUsernameUpdate(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editTextUsernameValue = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
    }

    public TextWatcher getPasswordUpdate(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editTextPasswordValue= s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
    }

    @Override
    public void destroy() {

    }

    public String sha256(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            String salt = "1234";
            s += salt;
            digest.update(s.getBytes("UTF-8"));
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(String.format("%02X", messageDigest[i]));

            return hexString.toString();
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
