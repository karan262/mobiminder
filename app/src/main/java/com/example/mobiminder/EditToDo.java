package com.example.mobiminder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditToDo extends AppCompatActivity {
    int u_id=0;
    String t_id;
    final Calendar myCalendar= Calendar.getInstance();
    ImageView imageView;
    TextView textView;
    EditText T_title,T_description;
    Button Todo_save;
    public static String URL_EditTODO=IP_Manager.getIp()+"UpdateToDo.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_to_do);
        getSupportActionBar().hide();
        imageView=findViewById(R.id.Edit_T_date);
        textView=findViewById(R.id.EditSelectedDate);
        T_title=findViewById(R.id.Edit_Title_todo);
        T_description=findViewById(R.id.Edit_T_Desc);
        Todo_save=findViewById(R.id.Edit_Todo_Save);
        textView.setText(getIntent().getStringExtra("t_date"));
        T_title.setText(getIntent().getStringExtra("t_title"));
        T_description.setText(getIntent().getStringExtra("t_desc"));
        t_id=getIntent().getStringExtra("t_id");
        SharedPreferences sharedPreferences=getSharedPreferences("userData",MODE_PRIVATE);
        u_id=sharedPreferences.getInt("id",0);

        Toast.makeText(this, ""+u_id+" : "+t_id+getIntent().getStringExtra("t_date"), Toast.LENGTH_SHORT).show();

        final DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(EditToDo.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Todo_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTodo();
            }
        });

    }

    private void updateLabel(){
        String myFormat="yyyy-MM-dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        textView.setText(dateFormat.format(myCalendar.getTime()));

    }
    private void updateTodo(){
        if(textView.getText().toString().isEmpty()){
            Toast.makeText(this, "Please Select date ", Toast.LENGTH_SHORT).show();
        }
        else if(T_title.getText().toString().isEmpty()){
            T_title.setError("Enter Todo title to proceed");
        }
        else if(T_description.getText().toString().isEmpty()){
            T_title.setError("Enter Todo title to proceed");
        }

        else{
            final String t_title,t_desc;
            t_title=T_title.getText().toString().trim();
            t_desc=T_description.getText().toString().trim();
            StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_EditTODO,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("JSON",response);
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");

                                if (success.equals("1")) {
                                    Toast.makeText(EditToDo.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(EditToDo.this, "Something Went Wrong" + e.toString(), Toast.LENGTH_SHORT).show();
                                // reg.setVisibility(View.GONE);
                            }
                        }
                    }, new Response.ErrorListener() {


                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(EditToDo.this, "Something Went Wrong" + error.toString(), Toast.LENGTH_SHORT).show();
                    // reg.setVisibility(View.GONE);
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params=new HashMap<>();
                    params.put("U_id",String.valueOf(u_id));
                    params.put("T_id",String.valueOf(t_id));
                    params.put("T_title",t_title);
                    params.put("T_desc",t_desc);
                    params.put("Date",textView.getText().toString());
                    return params;
                }
            };
            RequestQueue requestQueue= Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }

    }

}