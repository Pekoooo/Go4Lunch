package com.example.go4lunch.workmanager;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.WorkerParameters;

import com.example.go4lunch.R;
import com.example.go4lunch.model.AppModel.User;
import com.example.go4lunch.usecase.GetCurrentUserFromDBUseCase;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class WorkerSendNotification extends androidx.work.Worker  {

    private static final String CHANNEL_ID = "notification";
    private static final String TAG = "MyWorkerNotif";
    private static final String TAG_WORK_MANAGER = "MyWorkManager";
    private static final String COLLECTION_NAME = "users";
    private static final int NOTIFICATION_ID = 1;
    private final Context context;
    private final List<User> coworkers;
    private final User user;

    public WorkerSendNotification(
            @NonNull Context context,
            @NonNull WorkerParameters workerParams) throws ExecutionException, InterruptedException {
        super(context, workerParams);

        Log.d(TAG, "WorkerSendNotification: is called");

        this.context = context;
        user = Tasks.await(GetCurrentUserFromDBUseCase.invoke());
        coworkers = fetchAllCoworkers();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG_WORK_MANAGER, "doWork: is called");
        List<String> coworkersComing = getCoworkersComing(user, coworkers);
        createNotification(coworkersComing);
        return Result.success();
    }

    private void createNotification(List<String> coworkersComing) {
        Log.d(TAG_WORK_MANAGER, "createNotification: creating notification");

        String notification;
        StringBuilder stringBuilderWorkmates = new StringBuilder();

        if(user.getRestaurantChoiceId() != null){
            if (!coworkersComing.isEmpty()) {

                for (int i = 0; i < coworkersComing.size(); i++) {
                    stringBuilderWorkmates.append(coworkersComing.get(i));
                    if (i < coworkersComing.size() - 2)
                        stringBuilderWorkmates.append(", ");
                    else if (i == coworkersComing.size() - 2)
                        stringBuilderWorkmates.append(" ").append(context.getString(R.string.and)).append(" ");
                }

                notification =
                        context.getString(R.string.time_to_eat_at)
                                + user.getRestaurantChoiceName()
                                + ", "
                                + user.getRestaurantChoiceAddress()
                                + context.getString(R.string.with) + stringBuilderWorkmates + ".";

            } else {
                notification =
                        context.getString(R.string.time_to_eat_at)
                                + user.getRestaurantChoiceName()
                                + " "
                                + user.getRestaurantChoiceAddress();
            }

        } else {
            notification = context.getString(R.string.no_choice_notification_message);
        }
        sendNotification(notification);
    }

    private void sendNotification(String notification) {
        Log.d(TAG_WORK_MANAGER, "sendNotification: sending notification");

        NotificationCompat.Style style = new NotificationCompat.BigTextStyle().bigText(notification);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notif_icon)
                .setContentTitle(context.getString(R.string.time_to_eat))
                .setStyle(style)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public static List<String> getCoworkersComing(User user, List<User> allCoworkers) {
        List<String> coworkersComing = new ArrayList<>();
        if(user.getRestaurantChoiceId() != null){
            for (int i = 0; i < allCoworkers.size(); i++) {

                User currentCoworker = allCoworkers.get(i);
                if(!currentCoworker.getUid().equals(user.getUid()) && currentCoworker.getRestaurantChoiceId().equals(user.getRestaurantChoiceId())){
                        coworkersComing.add(currentCoworker.getUserName());
                }
            }
        }
        return coworkersComing;
    }

    private CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public List<User> fetchAllCoworkers() throws ExecutionException, InterruptedException {
        List<User> allCoworkers = new ArrayList<>();
        Tasks.await(getUsersCollection().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot documents : task.getResult()) {
                    allCoworkers.add(documents.toObject(User.class));
                }
            }
        }));

        return allCoworkers;
    }
}
