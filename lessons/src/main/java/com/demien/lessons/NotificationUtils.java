package com.demien.lessons;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import java.util.HashMap;

/**
 * Created by dmitry on 28.06.15.
 */
public class NotificationUtils {


        private static final String TAG = NotificationUtils.class.getSimpleName();

        private static NotificationUtils instance;

        private static Context context;
        private NotificationManager manager; // Системная утилита, упарляющая уведомлениями
        private int lastId = 0; //постоянно увеличивающееся поле, уникальный номер каждого уведомления
        private HashMap<Integer, Notification> notifications; //массив ключ-значение на все отображаемые пользователю уведомления


        //приватный контструктор для Singleton
        private NotificationUtils(Context context){
            this.context = context;
            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notifications = new HashMap<Integer, Notification>();
        }
        /**
         * Получение ссылки на синглтон
         */
        public static NotificationUtils getInstance(Context context){
            if(instance==null){
                instance = new NotificationUtils(context);
            } else{
                instance.context = context;
            }
            return instance;
        }
}
