package com.sm.kucek.viewmodel;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.fragment.app.FragmentManager;

import com.sm.kucek.model.ModelUser;
import com.sm.kucek.view.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.sm.kucek.viewmodel.MCrypt;

/**
 * Created by tofin on 21/04/16.
 */
public class RegisterViewModel implements ViewModel {
    private Context context;
    private String strRespUser;
    public ObservableField<String> registerMessage;
    public ObservableInt registerMessageVisibility;
    private String editTextUsernameValue = "";
    private String editTextPasswordValue = "";
    private String editTextPassword2Value = "";
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
    private MCrypt mCrypt = new MCrypt();

    public RegisterViewModel(Context context) {
        this.context = context;
        this.registerMessage = new ObservableField<>("");
        this.registerMessageVisibility = new ObservableInt(View.INVISIBLE);
    }

    /*
    public RegisterViewModel(Context context) {
        this.context = context;
    }
    */

    private void execRegister() {
        Log.d("Ya","Ada dalam execRegister");
        registerMessage.set("");
        registerMessageVisibility.set(View.INVISIBLE);
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
                            sb.append(line);
                            break;
                        }
                        in.close();
                        jsonObject = new JSONObject(sb.toString());
                        if (!(sb.toString() == null) && !(sb.toString().equals(""))) {
                            registerMessage.set("Silakan cek email utk konfirmasi.");
                            registerMessageVisibility.set(View.VISIBLE);
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

    public void submitRegistration(View view) {
        Log.i("Ya","Ada dalam submitRegistrationView");
        if (!(editTextPassword2Value.equals(editTextPasswordValue))) {
            Log.i("Ya","Ada dalam Pwd ga sama");
            registerMessage.set("Password doesn't match");
            registerMessageVisibility.set(View.VISIBLE);
        }
        else if ( !(editTextUsernameValue.equals("")) && !(editTextPasswordValue.equals(""))) {
            Log.i("Ya","Ada dalam pwd sama");
            url = "https://otokebon.com/laundry/webservice/register.php";
            //url = "https://otokebon.com/laundry/webservice/login.php?iduser=" + editTextUsernameValue + "&pwd=" + editTextPasswordValue;
            try {
                jsonObject = new JSONObject();
                jsonObject.put("iduser",editTextUsernameValue);
                jsonObject.put("pwd", sha256(editTextPasswordValue));
                //jsonObject.put("pwd", mCrypt.bytesToHex(mCrypt.encrypt(editTextPasswordValue)));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            execRegister();
        }
        else {
            Log.i("Ya","Ada dalam else");
            registerMessage.set("Username or Password can't be empty!");
            registerMessageVisibility.set(View.VISIBLE);
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

    public TextWatcher getPassword2Update(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editTextPassword2Value= s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
    }

    public String sha256(String s) {
        try {
            // Create SHA-256 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            String salt = "1234";
            s += salt;
            digest.update(s.getBytes("UTF-8"));

            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(String.format("%02X", messageDigest[i]));
                // hexString.append(Integer.toHexString(0xFF & messageDigest[i]));

            return hexString.toString();
            //return messageDigest.toString();
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }



    @Override
    public void destroy() {

    }

    /*
    public static String sha256(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("md5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

     */
}
