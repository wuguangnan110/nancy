package com.example.wifilisttest;




import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
 
public class Music extends Service {
	private MediaPlayer mp;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		mp=MediaPlayer.create(this,R.raw.baojing1);
 
	}
    @Override  
    public void onStart(Intent intent, int startId) {
    	super.onStart(intent, startId);
        mp.start(); 
    } 
	@Override
	public void onDestroy() {
		super.onDestroy();
		mp.stop();
	}
 
}