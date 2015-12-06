package sample.masaibar.saveobjectsample;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MainActivity extends AppCompatActivity {

    private static final String PREF_KEY = "useInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editName = (EditText) findViewById(R.id.editName);
        final EditText editAge = (EditText) findViewById(R.id.editAge);
        final RadioGroup radioGroupSex = (RadioGroup) findViewById(R.id.radioGroupSex);
        final RadioButton radioMale = (RadioButton) findViewById(R.id.radioMale);
        final RadioButton radioFemale = (RadioButton) findViewById(R.id.radioFemale);

        //Save
        findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strName = editName.getText().toString();
                String strAge = editAge.getText().toString();
                int checkedRadioId = radioGroupSex.getCheckedRadioButtonId();

                //簡易バリデーションチェック
                if (TextUtils.isEmpty(strName) ||
                        TextUtils.isEmpty(strAge) ||
                        checkedRadioId == -1) {
                    Toast.makeText(MainActivity.this, "入力が不足しています", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                UserInfo.Sex checkedSex = null;
                switch (checkedRadioId) {
                    case R.id.radioMale:
                        checkedSex = UserInfo.Sex.MALE;
                        break;
                    case R.id.radioFemale:
                        checkedSex = UserInfo.Sex.FEMALE;
                        break;
                    default:
                        break;
                }

                //UserInfoを生成
                UserInfo userInfo = new UserInfo(strName, Integer.parseInt(strAge), checkedSex);

                //Preferenceに保存
                SharedPreferences.Editor editor =
                        PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                editor.putString(PREF_KEY, toBase64(userInfo)).apply();
            }
        });

        //Load
        findViewById(R.id.buttonLoad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs =
                        PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                UserInfo userInfo = toUserInfo(prefs.getString(PREF_KEY, null));

                editName.setText(userInfo.getName());
                editAge.setText(String.valueOf(userInfo.getAge()));
                if (userInfo.getSex() == UserInfo.Sex.MALE) {
                    radioMale.setChecked(true);
                    radioFemale.setChecked(false);
                } else {
                    radioMale.setChecked(false);
                    radioFemale.setChecked(true);
                }
            }
        });

        //Clear
        findViewById(R.id.buttonClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editName.setText("");
                editAge.setText("");
                radioGroupSex.clearCheck();
            }
        });
    }

    public static String toBase64(UserInfo userInfo) {

        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;

        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(userInfo);

            byte[] bytes = byteArrayOutputStream.toByteArray();
            byte[] base64 = Base64.encode(bytes, Base64.NO_WRAP);

            return new String(base64);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static UserInfo toUserInfo(String base64) {

        ByteArrayInputStream byteArrayInputStream = null;
        ObjectInputStream objectInputStream = null;

        byte[] bytes = Base64.decode(base64.getBytes(), Base64.NO_WRAP);

        try {
            byteArrayInputStream = new ByteArrayInputStream(bytes);
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            UserInfo userData = (UserInfo) objectInputStream.readObject();
            return userData;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (byteArrayInputStream != null) {
                    byteArrayInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
