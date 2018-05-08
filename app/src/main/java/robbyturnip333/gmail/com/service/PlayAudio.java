package robbyturnip333.gmail.com.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by robby on 18/04/18.
 */

public class PlayAudio extends Service {
    MediaPlayer musik;

    public void onCreate(){
        super.onCreate();
        musik=MediaPlayer.create(this,R.raw.twinkle);
    }

    public int onStartCommand(Intent baru, int bendera, int id){
        musik.start();
        if(musik.isLooping()!=true){
            Log.d("TAG","GAGAL PUTAR");
        }
        return 1;
    }

    public  void onStop(){
        musik.stop();
        musik.release();
    }
    public  void onPause(){
        musik.stop();
        musik.release();
    }
    public  void onDestroy(){
        musik.stop();
        musik.release();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
