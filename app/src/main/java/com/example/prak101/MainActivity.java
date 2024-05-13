package com.example.prak101;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.prak101.db.Rat;
import com.example.prak101.db.RatDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    EditText nameEdit;
    EditText ageEdit;
    EditText colorEdit;
    Button saveButton;
    Button updateButton;
    Button deleteButton;
    Button findButton;

    RatDatabase ratDatabase;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameEdit = findViewById(R.id.ratName);
        ageEdit = findViewById(R.id.ratAge);
        colorEdit = findViewById(R.id.ratColor);
        saveButton = findViewById(R.id.saveBtn);
        updateButton = findViewById(R.id.updateBtn);
        deleteButton = findViewById(R.id.deleteBtn);
        findButton = findViewById(R.id.findBtn);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        ratDatabase = Room.databaseBuilder(getApplicationContext(), RatDatabase.class, "RatDB")
                .fallbackToDestructiveMigration().build();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEdit.getText().toString();
                int age = Integer.parseInt(ageEdit.getText().toString());
                String color = colorEdit.getText().toString();

                Rat rat = new Rat(name, age, color);
                addRatInBackground(rat);
                saveUserName(name);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEdit.getText().toString();
                int age = Integer.parseInt(ageEdit.getText().toString());
                updateRatNameInBackground(name, age);
                updateUserName(name);
            }
        });


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String color = colorEdit.getText().toString();
                deleteRatByColorInBackground(color);
                deleteUserName();
            }
        });

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEdit.getText().toString();
                findRatByNameInBackground(name);
                String savedName = getUserName();
                nameEdit.setText(savedName);
            }
        });
    }

    private void findRatByNameInBackground(String name) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Rat rat = ratDatabase.getRatDao().findRatByName(name);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (rat != null) {
                            Toast.makeText(MainActivity.this, "Rat found", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Rat not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


    private void deleteRatByColorInBackground(String color) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                ratDatabase.getRatDao().deleteRatByColor(color);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Rat deleted", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void addRatInBackground(Rat rat) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                ratDatabase.getRatDao().addRat(rat);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Rat added", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void updateRatNameInBackground(String name, int age) { // Изменил тип параметра на строку
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                ratDatabase.getRatDao().updateRatName(name, age); // Передаем имя как строку

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Rat name updated", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void saveUserName(String name) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userName", name);
        editor.apply();
        Toast.makeText(MainActivity.this, "Username saved", Toast.LENGTH_SHORT).show();
    }

    private void updateUserName(String newName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userName", newName);
        editor.apply();
        Toast.makeText(MainActivity.this, "Username updated", Toast.LENGTH_SHORT).show();
    }

    private void deleteUserName() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("userName");
        editor.apply();
        Toast.makeText(MainActivity.this, "Username deleted", Toast.LENGTH_SHORT).show();
    }

    private String getUserName() {
        return sharedPreferences.getString("userName", "");
    }
}
