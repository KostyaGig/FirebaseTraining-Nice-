package com.kostya_zinoviev.trainingfirebasedb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    private TextView mainText;
    private EditText edName;
    private Button addBtn, fireStoreBtn,storageBtn;
    private Uri imageUri;
    public static final int REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        edName = findViewById(R.id.edName);

        addBtn = findViewById(R.id.addBtn);
        fireStoreBtn = findViewById(R.id.firestore);
        storageBtn = findViewById(R.id.storage);

        mainText = findViewById(R.id.mainText);

        //REALTIME DB
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edName.getText().toString();
                addUser(name);
            }
        });

        //FIRESTORE DB
        fireStoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fireStore();
            }
        });

        //STORAGE DB
        storageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startImageIntent();

            }
        });

    }

    private void startImageIntent() {

        //Создаем интент с помощь которого мы сможем добавить фото и затем получить ее uri и отправить на базу данных

        Intent imageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageIntent.setType("image/");
        startActivityForResult(imageIntent,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null){
            //Если все хорошо,то получаем uri выбранной картинки
            imageUri = data.getData();

            putDataInStorage();
            //И запускаем метод,который отправляет картинку на базу данных и там ее сохраняет
        }
    }

    private void putDataInStorage() {
        final StorageReference firebaseStorage = FirebaseStorage.getInstance().getReference().child("images").child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
        if (imageUri != null){
        firebaseStorage.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                firebaseStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //Получаем url картинки,которая лежит по адресу FirebaseStorage.getInstance().getReference().child("images")
                        String url = uri.toString();

                        Log.i("url image", "url image: " + url);
                    }
                });
            }
        });
        } else {
            Toast.makeText(this, "Image uri is null", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri) {
        //Нужно для создание символов или url,точно не знаю
        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mtm = MimeTypeMap.getSingleton();

        return mtm.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void fireStore() {
        //Работа с FireStore

        //По дефолту получаем инстанция с помощью FirebaseFirestore.getInstance()
//        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        //Создадим мапу данных,которую мы хотим помещать

//        HashMap<String,Object> dataMap = new HashMap<>();
//
//        dataMap.put("Name","Kostya");
//        dataMap.put("Age","15");
//        dataMap.put("Country","Russia");
//
//        //Короче смотри ниже
//        firestore.collection("MyPath").document("MyDocument").set(dataMap)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()){
//                            Toast.makeText(HomeActivity.this, "Data was added", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });

        //Так же мы можем добавить к нашей мапе еще значение,допустим,если
        //мы ее случайно удалили или нужно обновить данные из дургого места

        //Создаем новую мапу
//        HashMap<String,Object> twoDataMap = new HashMap<>();
//
//        dataMap.put("SecondName","Zinoview");
//        dataMap.put("IQ","300");
//        dataMap.put("Хромосомы","46");


        //После этого почти тоже самое,но код немного другой чекай внимательно!!!
        //Кстати .colliction и .document должны совпадать,чтобы обновить данные
//        firestore.collection("MyPath").document("MyDocument").set(dataMap,StopOptions.merge())
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()){
//                            Toast.makeText(HomeActivity.this, "Data was updated", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });

        //Для обновления существующих данных

//        DocumentReference ref = FirebaseFirestore.getInstance().collection("MyPath").document("MyDocument");
//        //Это обновление по полю,в данном случаем поле с именем Name примет знаечние Polina вместо Kostya
//        ref.update("Name","Polina").addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()){
//                    Toast.makeText(HomeActivity.this, "Field Name was updated", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        //Так же можно обновлять данные по mapе,но этот пример здесь я не  буду показывать,там все просто
//        //Создаем мапу и ключи которые мы хотиим обновить обновляеи в ref.update(youMapa) вставляешь свою мапу
//    }

        //GET DATA WITH FIRESTORE

//        DocumentReference getDocData = FirebaseFirestore.getInstance().collection("MyPath").document("MyDocument");
//        getDocData.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()){
//                    DocumentSnapshot doc = task.getResult();
//                    if (doc.exists()){
//                        Log.i("info", doc.getData().toString());
//                    } else {
//                        Log.i("info","No data");
//                    }
//                }
//            }
//        });

    }

    private void addUser(final String name) {
//        //По онклику передаем name
//        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User").child(name);
//
//        HashMap<String, Object> map = new HashMap<>();
//
//        //С помощью mapы мы отправляем данные на Firebase,заметим,что ссылка ведет к "User".child(name)
//        map.put("name", name);
//        map.put("phone", "89082869025");
//        //updateChildren(обязательно)
//        reference.updateChildren(map);
//
//        //нужен для заполнения нашей модели
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                //User - наша модель
//
//                User user = dataSnapshot.getValue(User.class);
//                mainText.setText(user.getName());
//                //Проверка на работоспособность
//                //Таеким образом мы можем регать наших юзеров и через модель проверять ее на валидность
//                //!!! Создаем модель таким образом,что если мы помещаем name в map как первое значение
//                //То и в модели user обязательно должна быть создана СНАЧАЛА переменная с именем name или не name,но в эту переменную запишется значение из мапы
//                //НЕ ПУТАЙСЯ,КОСТЯН!
//
//                if (user.getPhone().equals("89082869025")) {
//                    Toast.makeText(HomeActivity.this, "YEEEE", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }
}
//++++REALTIME DATABASE+++++
//Этот код также можно юзать с RealtimeDatabase!
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User");
//
//
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot data: dataSnapshot.getChildren()){
//                        mainText.append(data.getValue().toString());
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }