package com.app.chaufeur;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class loginActivity extends AppCompatActivity {

    private final Timer _timer = new Timer();
    private final FirebaseDatabase _firebase = FirebaseDatabase.getInstance();

    private HashMap<String, Object> map = new HashMap<>();

    private ScrollView vscroll1;
    private LinearLayout login_layout;
    private LinearLayout signup_layout;
    private EditText edittext1;
    private EditText edittext2;
    private Button button1;
    private EditText edittext5;
    private EditText edittext3;
    private EditText edittext4;
    private Button button2;

    private final Intent N = new Intent();
    private FirebaseAuth Auth;
    private OnCompleteListener<AuthResult> _Auth_create_user_listener;
    private OnCompleteListener<AuthResult> _Auth_sign_in_listener;
    private OnCompleteListener<Void> _Auth_reset_password_listener;
    private SharedPreferences n;
    private DatabaseReference User = _firebase.getReference("User");
    private ChildEventListener _User_child_listener;
    private TimerTask T;
    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_login);
        com.google.firebase.FirebaseApp.initializeApp(this);
        initialize();
        initializeLogic();
        getSupportActionBar().hide();
    }

    private void initialize() {

        vscroll1 = findViewById(R.id.vscroll1);
        login_layout = findViewById(R.id.login_layout);
        signup_layout = findViewById(R.id.signup_layout);
        edittext1 = findViewById(R.id.edittext1);
        edittext2 = findViewById(R.id.edittext2);
        button1 = findViewById(R.id.button1);
        TextView textview3 = findViewById(R.id.textview3);
        TextView textview4 = findViewById(R.id.textview4);
        edittext5 = findViewById(R.id.edittext5);
        edittext3 = findViewById(R.id.edittext3);
        edittext4 = findViewById(R.id.edittext4);
        button2 = findViewById(R.id.button2);
        TextView textview8 = findViewById(R.id.textview8);
        Auth = FirebaseAuth.getInstance();
        n = getSharedPreferences("n", Activity.MODE_PRIVATE);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (edittext1.getText().toString().trim().equals("")) {
                    _createSnackBar("Please enter password");
                }
                else {
                    if (edittext2.getText().toString().trim().equals("")) {
                        _createSnackBar("Please enter email");
                    }
                    else {
                        Auth.signInWithEmailAndPassword(edittext1.getText().toString().trim(), edittext2.getText().toString().trim()).addOnCompleteListener(loginActivity.this, _Auth_sign_in_listener);
                        _ProgresbarShow("Please wait...");
                    }
                }
            }
        });

        textview3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (edittext1.getText().toString().trim().equals("")) {
                    _createSnackBar("Please enter email");
                }
                else {
                    Auth.sendPasswordResetEmail(edittext1.getText().toString().trim()).addOnCompleteListener(_Auth_reset_password_listener);
                    _ProgresbarShow("Please wait...");
                }
            }
        });

        textview4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                login_layout.setVisibility(View.GONE);
                signup_layout.setVisibility(View.VISIBLE);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (edittext5.getText().toString().trim().equals("")) {
                    _createSnackBar("Please username");
                }
                else {
                    if (edittext3.getText().toString().trim().equals("")) {
                        _createSnackBar("Please enter email");
                    }
                    else {
                        if (edittext4.getText().toString().trim().equals("")) {
                            _createSnackBar("Please enter password");
                        }
                        else {
                            Auth.createUserWithEmailAndPassword(edittext3.getText().toString().trim(), edittext4.getText().toString().trim()).addOnCompleteListener(loginActivity.this, _Auth_create_user_listener);
                            _ProgresbarShow("Please wait...");
                        }
                    }
                }
            }
        });

        textview8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                login_layout.setVisibility(View.VISIBLE);
                signup_layout.setVisibility(View.GONE);
            }
        });

        _User_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot _param1, String _param2) {

            }

            @Override
            public void onChildChanged(DataSnapshot _param1, String _param2) {

            }

            @Override
            public void onChildMoved(DataSnapshot _param1, String _param2) {

            }

            @Override
            public void onChildRemoved(DataSnapshot _param1) {

            }

            @Override
            public void onCancelled(DatabaseError _param1) {

            }
        };
        User.addChildEventListener(_User_child_listener);

        _Auth_create_user_listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> _param1) {
                final boolean _success = _param1.isSuccessful();
                final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
                if (_success) {
                    map = new HashMap<>();
                    map.put("name", edittext5.getText().toString().trim());
                    map.put("email", edittext3.getText().toString().trim());
                    map.put("password", edittext4.getText().toString().trim());
                    map.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    User.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(map);
                    map.clear();
                    n.edit().putString("n", "").commit();
                    n.edit().putString("e", "").apply();
                    T = new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    _ProgresbarDimiss();
                                    Myfunction.showMessage(getApplicationContext(), "Account Created");
                                    N.setClass(getApplicationContext(), HomeActivity.class);
                                    startActivity(N);
                                    finish();
                                }
                            });
                        }
                    };
                    _timer.schedule(T, (int)(500));
                }
                else {
                    _ProgresbarDimiss();
                    Myfunction.showMessage(getApplicationContext(), _errorMessage);
                }
            }
        };

        _Auth_sign_in_listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> _param1) {
                final boolean _success = _param1.isSuccessful();
                final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
                if (_success) {
                    N.setClass(getApplicationContext(), HomeActivity.class);
                    N.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    n.edit().putString("e", edittext1.getText().toString()).commit();
                    n.edit().putString("n", edittext2.getText().toString()).apply();
                    startActivity(N);
                    finish();
                }
                else {
                    Myfunction.showMessage(getApplicationContext(), _errorMessage);
                    _ProgresbarDimiss();
                }
            }
        };

        _Auth_reset_password_listener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> _param1) {
                final boolean _success = _param1.isSuccessful();
                if (_success) {
                    Myfunction.showMessage(getApplicationContext(), "Forget mail sent");
                    _ProgresbarDimiss();
                }
                else {
                    Myfunction.showMessage(getApplicationContext(), "Error");
                    _ProgresbarDimiss();
                }
            }
        };
    }
    private void initializeLogic() {
        if ((FirebaseAuth.getInstance().getCurrentUser() != null)) {
            N.setClass(getApplicationContext(), HomeActivity.class);
            N.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(N);
            finish();
        }
        vscroll1.setFillViewport(true);
        vscroll1.setVerticalScrollBarEnabled(false);
        signup_layout.setVisibility(View.GONE);
        _extra();
        if (Build.VERSION.SDK_INT >= 23) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        else {
            _statusbar_color("#9E9E9E");
        }
        _SX_CornerRadius_card(button1, "#DF002F", 12);
        _SX_CornerRadius_card(button2, "#DF002F", 12);
        _SX_CornerRadius_card(edittext1, "#EEEEEE", 12);
        _SX_CornerRadius_card(edittext2, "#EEEEEE", 12);
        _SX_CornerRadius_card(edittext3, "#EEEEEE", 12);
        _SX_CornerRadius_card(edittext4, "#EEEEEE", 12);
        _SX_CornerRadius_card(edittext5, "#EEEEEE", 12);
    }

    @Override
    protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);

        switch (_requestCode) {

            default:
                break;
        }
    }

    private void _statusbar_color (final String _color) {

        Window w = this.getWindow();w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(Color.parseColor(_color));
    }


    private void _createSnackBar (final String _message) {
        ViewGroup parentLayout = (ViewGroup) ((ViewGroup) this .findViewById(android.R.id.content)).getChildAt(0);
        com.google.android.material.snackbar.Snackbar.make(parentLayout, _message, com.google.android.material.snackbar.Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }).show();
    }


    private void _ProgresbarDimiss () {
        if(prog != null)
        {
            prog.dismiss();
        }
    }


    private void _ProgresbarShow (final String _title) {
        prog = new ProgressDialog(loginActivity.this);
        prog.setMax(100);
        prog.setMessage(_title);
        prog.setIndeterminate(true);
        prog.setCancelable(false);
        prog.show();
    }


    private void _SX_CornerRadius_card (final View _view, final String _color, final double _value) {
        android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable();
        gd.setColor(Color.parseColor(_color));
        gd.setCornerRadius((int)_value);
        _view.setBackground(gd);

        if (Build.VERSION.SDK_INT >= 21){
            _view.setElevation(5);
        }
    }


    private void _extra () {
    } private ProgressDialog prog; {
    }


    @Deprecated
    public void showMessage(String _s) {
        Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
    }

    @Deprecated
    public int getLocationX(View _v) {
        int _location[] = new int[2];
        _v.getLocationInWindow(_location);
        return _location[0];
    }

    @Deprecated
    public int getLocationY(View _v) {
        int _location[] = new int[2];
        _v.getLocationInWindow(_location);
        return _location[1];
    }

    @Deprecated
    public int getRandom(int _min, int _max) {
        Random random = new Random();
        return random.nextInt(_max - _min + 1) + _min;
    }

    @Deprecated
    public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
        ArrayList<Double> _result = new ArrayList<Double>();
        SparseBooleanArray _arr = _list.getCheckedItemPositions();
        for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
            if (_arr.valueAt(_iIdx))
                _result.add((double)_arr.keyAt(_iIdx));
        }
        return _result;
    }

    @Deprecated
    public float getDip(int _input){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
    }

    @Deprecated
    public int getDisplayWidthPixels(){
        return getResources().getDisplayMetrics().widthPixels;
    }

    @Deprecated
    public int getDisplayHeightPixels(){
        return getResources().getDisplayMetrics().heightPixels;
    }

}