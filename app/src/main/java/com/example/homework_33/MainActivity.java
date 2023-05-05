package com.example.homework_33;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("UseSwitchCompatOrMaterialCode") // аннотация к полю переключателя повтора воспроизведения
public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

        // создание полей
        private final String DATA_STREAM = "http://ep128.hostingradio.ru:8030/ep128"; // ссылка на аудио поток
        private final String DATA_SD = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + "/music.mp3"; // патч на аудио-файл с SD-карты
        private String nameAudio = ""; // название контента
        public static final int MUSIC = 7;
        private MediaPlayer mediaPlayer; // создание поля медиа-плеера
        private AudioManager audioManager; // создание поля аудио-менеджера
        private Toast toast; // создание поля тоста

        private TextView textOut; // поле вывода информации об аудио-файле
        private Switch switchLoop; // поле переключателя повтора воспроизведения

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                // присваеваем полям соответствующие ID из activity_main
                textOut = findViewById(R.id.textOut);
                switchLoop = findViewById(R.id.switchLoop);

                // получение доступа к аудио-менеджеру
                audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);


                // создание слушателя переключателя повтора
                switchLoop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                                if(mediaPlayer != null)
                                        mediaPlayer.setLooping(isChecked); // включение / выключение повтора аудио
                        }
                });
        }

        // слушатель нажатия радио-кнопок
        public void onClickSource(View view) {

                releaseMediaPlayer(); // метод освобождения используемых проигрывателем ресурсов

                // обработка нажатия кнопок
                switch (view.getId()) {
                        case R.id.btnRaw:
                                // код выполнения кнопки btnRAW
                                toast = Toast.makeText(this, "Запуск 'Metamorphosis' ", Toast.LENGTH_SHORT); // инициализация
                                toast = Toast.makeText(this, "Нажмите 'PLAY' ", Toast.LENGTH_LONG);
                                toast.show(); // демонстрация тоста на экране
                                mediaPlayer = MediaPlayer.create(this, R.raw.metamorphosis); // создание дорожки с имеющимся аудио-файлом

                                nameAudio = "Interworld-Metamorphosis"; // инициализация названия контента
                                break;


                        case R.id.btnRaw2:
                                // код выполнения кнопки btnRAW
                                toast = Toast.makeText(this, "Запуск 'Rapture' ", Toast.LENGTH_SHORT); // инициализация
                                toast = Toast.makeText(this, "Нажмите 'PLAY' ", Toast.LENGTH_LONG);
                                toast.show(); // демонстрация тоста на экране
                                mediaPlayer = MediaPlayer.create(this, R.raw.rapture); // создание дорожки с имеющимся аудио-файлом

                                nameAudio = "Interworld-Rapture"; // инициализация названия контента
                                break;
                }

                if (mediaPlayer == null) return;

                mediaPlayer.setLooping(switchLoop.isChecked()); // включение / выключение повтора
                mediaPlayer.setOnCompletionListener(this); // слушатель окончания проигрывания
        }

        // слушатель управления воспроизведением контента
        public void onClick(View view) {
                if (mediaPlayer == null) return;

                switch (view.getId()) {
                        case R.id.btnResume:

                                if (!mediaPlayer.isPlaying()) {
                                        mediaPlayer.start(); // метод возобновления проигрывания
                                }


                                break;
                        case R.id.btnPause:
                                if (mediaPlayer.isPlaying()) {
                                        mediaPlayer.pause(); // метод паузы
                                }
                                break;
                        case R.id.btnStop:
                                mediaPlayer.stop(); // метод остановки
                                break;
                        case R.id.btnForward:
                                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 5000); // переход к определённой позиции трека
                                // mediaPlayer.getCurrentPosition() - метод получения текущей позиции
                                break;
                        case R.id.btnBack:
                                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 5000); // переход к определённой позиции трека
                                break;
                }
                // информативный вывод информации
                textOut.setText(nameAudio + "\n(проигрывание " + mediaPlayer.isPlaying() + ", время " + mediaPlayer.getCurrentPosition()
                        + ",\nповтор " + mediaPlayer.isLooping() + ", громкость " + audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + ")");
        }


        // onPrepared - метод слушателя OnPreparedListener (вызывается, когда плеер готов к проигрыванию)
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) { // метод подготовки дополнительного потока
                mediaPlayer.start(); // старт медиа-плейера
                toast = Toast.makeText(this, "Старт медиа-плейера", Toast.LENGTH_SHORT); // инициализация тоста
                toast.show(); // демонстрация тоста на экране
        }

        // onCompletion() - метод слушателя OnCompletionListener (вызывается, когда достигнут конец проигрываемого содержимого)
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) { // метод закрытия дополнительного потока
                toast = Toast.makeText(this, "Отключение медиа-плейера", Toast.LENGTH_SHORT); // инициализация тоста
                toast.show(); // демонстрация тоста на экране
        }

        @Override
        protected void onDestroy() {
                super.onDestroy();
                releaseMediaPlayer(); // метод освобождения используемых проигрывателем ресурсов
        }

        // метод освобождения используемых проигрывателем ресурсов
        private void releaseMediaPlayer() {
                if (mediaPlayer != null) {
                        try {
                                mediaPlayer.release();
                                mediaPlayer = null;
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                }
        }
}