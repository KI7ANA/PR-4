<div align="center">

# Отчёт

</div>

<div align="center">

## Практическая работа №4

</div>

<div align="center">

## Работа с встроенной базой данных SQLite

</div>

**Выполнил:**  
Ткачев Сергей Юрьевич  
**Курс:** 2  
**Группа:** ИНС-б-о-24-2  
**Направление:** ИПИНЖ (Институт перспективной инженерии)  
**Профиль:** Информационные системы и технологии  

---

### Цель работы

Изучить основы работы с СУБД SQLite в Android-приложениях. Научиться создавать базу данных, таблицы, выполнять основные операции CRUD (Create, Read, Update, Delete) с использованием класса `SQLiteOpenHelper` и отображать данные на экране.

### Ход работы

#### Задание 1: Создание класса-помощника `SQLHelper`

1. В Android Studio был создан новый проект с шаблоном **Empty Views Activity**. Проекту было дано имя `SQLiteLab`.
2. В папке `java/com.ncfu.pw_4` был создан новый Java-класс с именем `SQLHelper`.
3. Класс `SQLHelper` был унаследован от `SQLiteOpenHelper`.
4. В классе были добавлены константы для названия базы данных, версии базы и таблицы, а также реализованы методы `onCreate()` и `onUpgrade()`.

#### Листинг 1. Код класса `SQLHelper`

```java
package com.ncfu.pw_4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "university.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "students";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AGE = "age";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAME + " TEXT NOT NULL, "
            + COLUMN_AGE + " INTEGER);";

    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
```

---

#### Задание 2: Создание модели данных `Person`

1. Был создан новый Java-класс с именем `Person`.
2. В класс были добавлены поля, конструктор, геттеры и сеттеры.

#### Листинг 2. Код класса `Person`

```java
package com.ncfu.pw_4;

public class Person {
    private int id;
    private String name;
    private int age;

    public Person(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
```

---

#### Задание 3: Реализация методов для работы с данными в `SQLHelper`

В класс `SQLHelper` были добавлены методы для выполнения основных CRUD-операций: добавление записи, получение всех записей, обновление записи и удаление записи.  
Для безопасного получения индексов столбцов в `Cursor` использовался метод `getColumnIndexOrThrow()`, позволяющий избежать ошибок при неверном имени столбца.

#### Листинг 3. Методы CRUD в `SQLHelper`

```java
// Добавление записи (Create)
public long addStudent(String name, int age) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(COLUMN_NAME, name);
    values.put(COLUMN_AGE, age);
    long id = db.insert(TABLE_NAME, null, values);
    db.close();
    return id;
}

// Получение всех записей (Read)
public ArrayList<Person> getAllStudents() {
    ArrayList<Person> studentList = new ArrayList<>();
    String selectQuery = "SELECT * FROM " + TABLE_NAME;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);

    if (cursor.moveToFirst()) {
        do {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            int age = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE));
            studentList.add(new Person(id, name, age));
        } while (cursor.moveToNext());
    }

    cursor.close();
    db.close();
    return studentList;
}

// Обновление записи (Update)
public int updateStudent(Person person) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(COLUMN_NAME, person.getName());
    values.put(COLUMN_AGE, person.getAge());

    return db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
            new String[]{String.valueOf(person.getId())});
}

// Удаление записи (Delete)
public void deleteStudent(int id) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(TABLE_NAME, COLUMN_ID + " = ?",
            new String[]{String.valueOf(id)});
    db.close();
}
```

---

#### Задание 4: Работа с базой данных в `MainActivity`

В файле `activity_main.xml` был создан простой пользовательский интерфейс с двумя кнопками: для добавления записи и для отображения всех записей. Для вывода данных был использован вертикальный `LinearLayout`.

#### Листинг 4. Код файла `activity_main.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp">

    <Button
        android:id="@+id/btnAdd"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Добавить студента" />

    <Button
        android:id="@+id/btnShow"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Показать всех"
        android:layout_marginTop="8dp" />

    <LinearLayout
        android:id="@+id/container"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        android:layout_marginTop="16dp" />

</LinearLayout>
```

#### Листинг 5. Код файла `MainActivity.java`

```java
package com.ncfu.pw_4;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SQLHelper dbHelper;
    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new SQLHelper(this);
        container = findViewById(R.id.container);

        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnShow = findViewById(R.id.btnShow);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = dbHelper.addStudent("Иванов Иван", 20);
                if (id != -1) {
                    Toast.makeText(MainActivity.this,
                            "Студент добавлен с ID: " + id,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this,
                            "Ошибка добавления",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAllStudents();
            }
        });
    }

    private void displayAllStudents() {
        ArrayList<Person> students = dbHelper.getAllStudents();
        container.removeAllViews();

        if (students.isEmpty()) {
            TextView emptyView = new TextView(this);
            emptyView.setText("Список студентов пуст");
            container.addView(emptyView);
            return;
        }

        for (Person student : students) {
            TextView textView = new TextView(this);
            textView.setText(student.getId() + ": " + student.getName() + ", возраст " + student.getAge());
            textView.setTextSize(16);
            textView.setPadding(8, 8, 8, 8);
            container.addView(textView);
        }
    }
}
```

При запуске приложения кнопка **«Добавить студента»** добавляла тестовую запись в базу данных, а кнопка **«Показать всех»** выводила список добавленных студентов на экран.

<div align="center">

<img width="427" height="796" alt="image" src="https://github.com/user-attachments/assets/249d1f34-dd0a-4d83-9253-31cfd2241866" />

*Рисунок 1. Результат выполнения задания 4*

</div>

---

#### Задания для самостоятельного выполнения

**Вариант 5: Поликлиника.**  
Требовалось реализовать сущность **Пациент** со следующими полями:
- ФИО;
- дата рождения;
- номер полиса;
- диагноз.

В рамках самостоятельного задания была спроектирована структура таблицы, реализован отдельный класс-помощник `SQLHelper2`, создана модель данных `Patient`, добавлены методы для CRUD-операций и реализован простой интерфейс для работы с записями пациентов.

---

#### Задание 5. Создание модели данных `Patient`

Для хранения информации о пациентах был создан отдельный Java-класс `Patient`, содержащий поля `id`, `fullName`, `birthDate`, `policyNumber` и `diagnosis`, а также конструктор, геттеры и сеттеры.

#### Листинг 6. Код класса `Patient`

```java
package com.ncfu.pw_4;

public class Patient {
    private int id;
    private String fullName;
    private String birthDate;
    private String policyNumber;
    private String diagnosis;

    public Patient(int id, String fullName, String birthDate, String policyNumber, String diagnosis) {
        this.id = id;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.policyNumber = policyNumber;
        this.diagnosis = diagnosis;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
}
```

---

#### Задание 6. Создание класса `SQLHelper2` для базы пациентов

Для работы с таблицей пациентов был создан отдельный класс `SQLHelper2`, наследуемый от `SQLiteOpenHelper`. В нём была создана база данных `clinic.db` и таблица `patients`.

#### Листинг 7. Код класса `SQLHelper2`

```java
package com.ncfu.pw_4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SQLHelper2 extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "clinic.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "patients";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FULL_NAME = "full_name";
    public static final String COLUMN_BIRTH_DATE = "birth_date";
    public static final String COLUMN_POLICY = "policy_number";
    public static final String COLUMN_DIAGNOSIS = "diagnosis";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_FULL_NAME + " TEXT NOT NULL, "
            + COLUMN_BIRTH_DATE + " TEXT NOT NULL, "
            + COLUMN_POLICY + " TEXT NOT NULL, "
            + COLUMN_DIAGNOSIS + " TEXT NOT NULL);";

    public SQLHelper2(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addPatient(String fullName, String birthDate, String policyNumber, String diagnosis) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULL_NAME, fullName);
        values.put(COLUMN_BIRTH_DATE, birthDate);
        values.put(COLUMN_POLICY, policyNumber);
        values.put(COLUMN_DIAGNOSIS, diagnosis);
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public ArrayList<Patient> getAllPatients() {
        ArrayList<Patient> patientList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String fullName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULL_NAME));
                String birthDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIRTH_DATE));
                String policy = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POLICY));
                String diagnosis = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSIS));

                patientList.add(new Patient(id, fullName, birthDate, policy, diagnosis));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return patientList;
    }

    public int updatePatient(Patient patient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULL_NAME, patient.getFullName());
        values.put(COLUMN_BIRTH_DATE, patient.getBirthDate());
        values.put(COLUMN_POLICY, patient.getPolicyNumber());
        values.put(COLUMN_DIAGNOSIS, patient.getDiagnosis());

        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(patient.getId())});
    }

    public void deletePatient(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
}
```

---

#### Задание 7. Реализация интерфейса для работы с пациентами

Для демонстрации работы с таблицей пациентов был реализован простой интерфейс с кнопками:
- **Добавить пациента**
- **Показать всех пациентов**

При нажатии на кнопку добавления в базу вносилась тестовая запись пациента. При нажатии на кнопку отображения на экран выводился список всех записей из таблицы.

#### Листинг 8. Пример `activity_main.xml` для самостоятельного задания

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp">

    <Button
        android:id="@+id/btnAddPatient"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Добавить пациента" />

    <Button
        android:id="@+id/btnShowPatients"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Показать всех пациентов"
        android:layout_marginTop="8dp" />

    <LinearLayout
        android:id="@+id/patientContainer"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        android:layout_marginTop="16dp" />

</LinearLayout>
```

#### Листинг 9. Пример кода `MainActivity.java` для работы с пациентами

```java
package com.ncfu.pw_4;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SQLHelper2 dbHelper2;
    private LinearLayout patientContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper2 = new SQLHelper2(this);
        patientContainer = findViewById(R.id.patientContainer);

        Button btnAddPatient = findViewById(R.id.btnAddPatient);
        Button btnShowPatients = findViewById(R.id.btnShowPatients);

        btnAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = dbHelper2.addPatient(
                        "Ткачев Сергей Юрьевич",
                        "22.11.2002",
                        "1234567890",
                        "ОРВИ"
                );

                if (id != -1) {
                    Toast.makeText(MainActivity.this,
                            "Пациент добавлен с ID: " + id,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this,
                            "Ошибка добавления пациента",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnShowPatients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAllPatients();
            }
        });
    }

    private void displayAllPatients() {
        ArrayList<Patient> patients = dbHelper2.getAllPatients();
        patientContainer.removeAllViews();

        if (patients.isEmpty()) {
            TextView emptyView = new TextView(this);
            emptyView.setText("Список пациентов пуст");
            patientContainer.addView(emptyView);
            return;
        }

        for (Patient patient : patients) {
            TextView textView = new TextView(this);
            textView.setText(
                    patient.getId() + ": "
                            + patient.getFullName()
                            + ", дата рождения: " + patient.getBirthDate()
                            + ", полис: " + patient.getPolicyNumber()
                            + ", диагноз: " + patient.getDiagnosis()
            );
            textView.setTextSize(16);
            textView.setPadding(8, 8, 8, 8);
            patientContainer.addView(textView);
        }
    }
}
```

В результате выполнения самостоятельного задания был получен рабочий экран, позволяющий добавлять пациентов в базу данных и просматривать список всех добавленных записей.

<div align="center">

![Рисунок 2 - Результат задания для самостоятельного выполнения](images/image_2.png)

*Рисунок 2. Результат выполнения самостоятельного задания (вариант 5)*

</div>

### Вывод

В результате выполнения практической работы были изучены основы работы с встроенной базой данных SQLite в Android-приложениях. В ходе работы были освоены принципы создания базы данных, таблиц и реализации основных CRUD-операций с использованием класса `SQLiteOpenHelper`. Также были получены практические навыки по созданию моделей данных, работе с `Cursor`, `ContentValues` и отображению результатов запросов на экране. В индивидуальной части был реализован пример базы данных поликлиники для хранения информации о пациентах. Таким образом, цель практической работы была достигнута.

### Ответы на контрольные вопросы

**1. Какие типы данных поддерживает SQLite? Как в SQLite можно хранить логические значения и даты?**  

SQLite поддерживает следующие основные типы данных: `NULL`, `INTEGER`, `REAL`, `TEXT`, `BLOB` и `NUMERIC`.  

Логические значения в SQLite обычно хранятся как целые числа типа `INTEGER`, где `1` соответствует `true`, а `0` — `false`.  

Даты в SQLite можно хранить в нескольких форматах:
- как `TEXT`, например в формате `YYYY-MM-DD`;
- как `INTEGER`, представляющий собой Unix timestamp;
- как `REAL`, представляющий юлианскую дату.

**2. Для чего нужен класс `SQLiteOpenHelper`? Опишите назначение методов `onCreate()` и `onUpgrade()`.**  

Класс `SQLiteOpenHelper` предназначен для создания и управления базой данных SQLite в Android-приложении. Он упрощает работу с созданием базы, обновлением её структуры и открытием соединения с ней.  

Метод `onCreate(SQLiteDatabase db)` вызывается один раз при первом создании базы данных. В нём обычно выполняются SQL-запросы для создания таблиц.  

Метод `onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)` вызывается при изменении версии базы данных. Он используется для обновления структуры базы: удаления старых таблиц, создания новых или выполнения миграции данных.

**3. В чём разница между методами `getWritableDatabase()` и `getReadableDatabase()`? В каких ситуациях может возникнуть ошибка при вызове `getWritableDatabase()`?**  

Метод `getWritableDatabase()` открывает базу данных в режиме чтения и записи. Его используют, когда необходимо добавлять, изменять или удалять записи.  

Метод `getReadableDatabase()` открывает базу данных только для чтения. Его используют в ситуациях, когда требуется только получать данные из таблиц.  

Ошибка при вызове `getWritableDatabase()` может возникнуть, если:
- на устройстве недостаточно свободного места;
- база данных повреждена;
- отсутствует возможность записи в файл базы данных;
- база заблокирована другим процессом.

**4. Что такое `Cursor`? Как правильно перемещаться по его элементам и почему важно закрывать его после использования?**  

`Cursor` — это объект, предоставляющий доступ к строкам результата SQL-запроса. Он позволяет последовательно считывать данные, возвращённые запросом к базе данных.  

Обычно работа с `Cursor` организуется следующим образом:
1. проверить, есть ли записи с помощью `moveToFirst()`;
2. считывать данные текущей строки;
3. переходить к следующей строке через `moveToNext()` до конца выборки.  

После использования `Cursor` обязательно нужно закрывать вызовом `close()`, так как он использует системные ресурсы. Если не закрыть `Cursor`, могут возникнуть утечки памяти и проблемы с производительностью приложения.

**5. Что такое `ContentValues` и для каких операций он применяется?**  

`ContentValues` — это специальный контейнер в Android для хранения пар «имя столбца — значение». Он используется при выполнении операций вставки и обновления данных в SQLite.  

С помощью `ContentValues` можно удобно передавать значения в методы `insert()` и `update()`.

Пример использования:

```java
ContentValues values = new ContentValues();
values.put("name", "Иванов Иван");
values.put("age", 20);
```

**6. В чём отличие методов `query()` и `rawQuery()`? Приведите пример использования `rawQuery()` с параметром-плейсхолдером (`?`).**  

Метод `query()` предоставляет более удобный и структурированный способ формирования SQL-запроса через параметры метода. Он подходит для стандартных выборок из таблицы.  

Метод `rawQuery()` позволяет выполнять произвольный SQL-запрос, записанный вручную строкой. Он удобен, если требуется более сложный запрос или полный контроль над SQL-кодом.  

Пример использования `rawQuery()` с параметром-плейсхолдером:

```java
Cursor cursor = db.rawQuery(
        "SELECT * FROM students WHERE age > ?",
        new String[]{"18"}
);
```

**7. Как обработать ситуацию, когда таблица уже существует, но её структура была изменена (например, добавлено новое поле)?**  

Для обработки такой ситуации обычно увеличивают номер версии базы данных `DATABASE_VERSION`. После этого при следующем запуске будет вызван метод `onUpgrade()`.  

В простейшем варианте в `onUpgrade()` можно удалить старую таблицу и создать новую заново:

```java
@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    onCreate(db);
}
```

Такой способ подходит для учебных проектов, но приводит к потере старых данных.  
В более сложных приложениях обычно используют SQL-команду `ALTER TABLE` или полноценные миграции, чтобы сохранить уже существующие записи.
