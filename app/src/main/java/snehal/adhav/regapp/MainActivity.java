package snehal.adhav.regapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText name,email,password,c_password;
    private Button btn_reg;
    private ProgressBar loading;
    private static String URL_REG="https://referal123.000webhostapp.com/register.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //URL_REG.replaceAll("","%20");
        loading=findViewById(R.id.loading);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        c_password=findViewById(R.id.c_password);
        btn_reg=findViewById(R.id.reg);
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });
    }
    private void Register(){
        loading.setVisibility(View.VISIBLE);
        btn_reg.setVisibility(View.GONE);
        final String str_name = this.name.getText().toString().trim();
        final String str_email = this.email.getText().toString().trim();
        final String str_password = this.password.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response).getJSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1")) {

                        Toast.makeText(MainActivity.this, "Register Successful", Toast.LENGTH_SHORT).show();

                        loading.setVisibility(View.GONE);
                        btn_reg.setVisibility(View.VISIBLE);
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));

                    } else if(success.equals("already_registred")){
                        email.setError("email already registred");

                        email.requestFocus();
                        loading.setVisibility(View.GONE);
                        btn_reg.setVisibility(View.VISIBLE);
                    }else
                    {

                        Toast.makeText(MainActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error ! " + e.toString(), Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    btn_reg.setVisibility(View.VISIBLE);


                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error ! " + error.toString(), Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        btn_reg.setVisibility(View.VISIBLE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("name", str_name);
                params.put("email", str_email);
                params.put("password", str_password);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}