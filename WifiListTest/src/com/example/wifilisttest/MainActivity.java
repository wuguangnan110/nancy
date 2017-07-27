package com.example.wifilisttest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener {

	private String TAG = "MainActivity";
	private ListView list;
	private TextView Tv;
	private Button m_button;
	private Button m_button1;
	private EditText m_editText;
	static int WifiListNum = 10; //记忆列表总数 
	String[] listw = new String[WifiListNum];
	String[] listd = new String[WifiListNum];
	int value = 0;
	private Toast mToast;
	private WifiManager wifiManager;
	int status = 0;
	int ret = 0;
//	String[] list0 = new String[20];
	List<ScanResult> listb;
	int time = 0;
	private Button button;
	static int inputtime = 0;
	boolean startflag = false;
	int devicenum = 0;
	private int[] data = new int[100];
	int hasData = 0;
	private WifiAdapter wifiAdapter;
	 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment1);

		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		listb = wifiManager.getScanResults();
		if(listb==null){
			listb = new ArrayList<ScanResult>();
		}
		for (int i = 0; i < WifiListNum; i++) {
			listw[i] = "no wifi";
			listd[i] = "no wifi";
		}
		// 设置Fragment1对应R.layout.listtest这个布局文件
		wifiAdapter = new WifiAdapter(this, listb);
//		adapter = new SimpleAdapter(this, getData(list0), R.layout.listtest, new String[] { "title" },
//				new int[] { R.id.title });

		list = (ListView) findViewById(android.R.id.list);
		Tv = (TextView) findViewById(R.id.tv);
		Tv.setText("wifi自动检测v4.0");
		Log.i(TAG, "--------onCreateView");
		m_button = (Button) findViewById(R.id.editBtn1);
		m_button1 = (Button) findViewById(R.id.editBtn2);
		m_editText = (EditText) findViewById(R.id.edit_text);
		m_editText.selectAll();
		m_button.setOnClickListener(new ButtonListener());
		m_button1.setOnClickListener(new ButtonListener1());
		list.setAdapter(wifiAdapter);
		list.setOnItemClickListener(this);
		list.setOnItemLongClickListener(new OnItemLongClickListener(){
			@Override
			public boolean onItemLongClick(AdapterView adapter,View view,int position,long id) {  //长按事件
				// TODO Auto-generated method stub
				// When clicked, show a toast with the TextView text
				ScanResult scanResult = listb.get(position);
				
				for (int i = 0; i < WifiListNum; i++) {
					if (listw[i] == scanResult.SSID) {
						mToast.setText("请勿重复添加 " + listw[i] + " 设备");
						mToast.show();	
						return true;
					}	
				}
				value = inputtime+1;
				if (inputtime < WifiListNum )
				{
					listw[inputtime] = scanResult.SSID;
					mToast.setText("wifi设备 " + listw[inputtime] + " 已加入检测,目前共检测"+value+"台设备");
					mToast.show();
					inputtime++;
				}
				else 
				{
					mToast.setText("wifi记忆列表已满！如再添加请清除");
					mToast.show();
				}
				return true;//当返回true时,不会触发短按事件
			}
		}); 
		button = (Button) findViewById(R.id.button);
		// final TextView textView = (TextView)
		// getActivity().findViewById(R.id.textview1);
		stopService(new Intent(this, Music.class));
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { // 按钮点击事件
				time++;
				time = time % 2;
				mToast.setGravity(Gravity.CENTER, 0, 0);
				if (time == 0) // start scan wifi
				{
					startflag = false;
					button.setText("开始扫描");
					// Toast.makeText(getActivity(), "停止扫描wifi",
					// Toast.LENGTH_LONG).show();
					mToast.setText("停止扫描wifi");
					mToast.show();
					stopService(new Intent(MainActivity.this, Music.class));
				}
				else // stop scan wifi
				{
					startflag = true;
					button.setText("停止扫描");
					// Toast.makeText(getActivity(), "现在开始扫描wifi",
					// Toast.LENGTH_LONG).show();
					mToast.setText("现在开始扫描wifi");
					mToast.show();
				}

				new Thread() // 建立扫描线程，40ms更新一次wifi列表，将刷新任务交于handler处理
				{
					public void run() {
						int value = 0;
						while (startflag) {
							// 获取耗时操作的完成百分比
							ret = scanwifi();
							if (ret != 0) {
							//	System.out.println("目前扫描发现 " + ret + "  台wifi设备!");
							}
							value++;
							// 发送消息
							if (value == 8) {
								mHandler.sendEmptyMessage(0x111); // 发消息给handleMessage处理
								value = 0;
							}
						status = doWork();
						}
					}
				}.start();

			}
		});
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 表明消息是由该程序发送的
			if (msg.what == 0x111) {
				
				if (startflag == true && ret != 0) // 检测到wifi设备,ret为个数
				{
					if (inputtime == ret)
					{
						mToast.setText("目前成功发现 " + ret + " 台wifi设备,无设备丢失");
						mToast.show();
						stopService(new Intent(MainActivity.this, Music.class)); // 停止报警
					}
					else
					{
						mToast.setText("目前成功发现 " + ret + " 台wifi设备, "+(inputtime - ret) +" 台设备丢失");
						mToast.show();
						startService(new Intent(MainActivity.this, Music.class));
					}
				}

				if (ret == 0) // 检测为0个设备，开始报警
				{
					mToast.setText("目前没有发现任何wifi设备，请检查！！");
					mToast.show();
					startService(new Intent(MainActivity.this, Music.class));
				} 
				else 
				{
				//	stopService(new Intent(MainActivity.this, Music.class)); // 停止报警
				}
				ret = 0;
				wifiManager.startScan();
				listb = wifiManager.getScanResults();
				// 数组初始化要注意
				List<ScanResult> newResult = new ArrayList<ScanResult>();
				for (int j = 0; j < listb.size(); j++) {
					String ssid = listb.get(j).SSID;
					if(ssid!=null&&ssid.length()!=0){
						newResult.add(listb.get(j));
					}
				}
				wifiAdapter.setInfoList(newResult);
				wifiAdapter.notifyDataSetChanged();

				System.out.println("wifi handler scan");
			}
		}
	};

//	private List<? extends Map<String, ?>> getData(String[] strs) { // 数据字符串转换
//		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//
//		for (int i = 0; i < strs.length; i++) {
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("title", strs[i]);
//			list.add(map);
//		}
//		return list;
//	}

	class ButtonListener implements OnClickListener {

		public void onClick(View v) {

			// TODO Auto-generated method stub
			Log.v("EditText", m_editText.getText().toString());
		
			
			if (!TextUtils.isEmpty(m_editText.getText()) && !m_editText.getText().toString().trim().isEmpty()) {
				
				for (int i = 0; i < WifiListNum; i++) {
					if (listw[i].contains( m_editText.getText().toString())) {
						mToast.setText("请勿重复添加 " + listw[i] + " 设备");
						mToast.show();	
						return;
					}	
				}
				value = inputtime+1;
				if (inputtime < WifiListNum )
				{
					listw[inputtime] = m_editText.getText().toString();
					mToast.setText("wifi设备 " + listw[inputtime] + " 已加入检测,目前共检测"+value+"台设备");
					mToast.show();
					inputtime++;
				}
				else 
				{
					mToast.setText("wifi记忆列表已满！如再添加请清除");
					mToast.show();
				}
			}
			else
			{
				mToast.setText("请输入正确wifi名称");
				mToast.show();
			}
		}
	}

	class ButtonListener1 implements OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.v("EditText", m_editText.getText().toString());
			inputtime = 0;
			for (int i = 0; i < WifiListNum; i++) {
				listw[i] = "no wifi";
			}
			// Toast.makeText(getActivity(), "清除所有wifi记忆列表",
			// Toast.LENGTH_LONG).show();
			mToast.setText("清除所有wifi记忆列表");
			mToast.show();
			m_editText.setText(null);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> l, View v, int position, long id) {
		ScanResult scanResult = listb.get(position);
		if (wifiManager.setWifiEnabled(true))
		{
		//	mToast.setText(scanResult.SSID+" 是开启状态");
		//	mToast.show();
		}
		else 
		{
			//mToast.setText(scanResult.SSID+" 是关闭状态");
			//mToast.show();
		}
	}

	public int doWork() // 增加一个耗时操作
	{
		// 为数组元素赋值
		data[hasData++] = (int) (Math.random() * 100);
		if (hasData == 99)
			hasData = 0;
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return hasData;
	}

	public int scanwifi() // 实时扫描wifi，并将wifi SSID加入listk中
	{
		listb = wifiManager.getScanResults();
		int ret = 0;
		// 数组初始化要注意
		String[] listk = new String[listb.size()];
		if (listb != null) {
			for (int i = 0; i < listb.size(); i++) {
				ScanResult scanResult = listb.get(i);
				listk[i] = scanResult.SSID;
			}
		}

		String[] list0 = new String[listb.size()];
		if (listb == null) {
			list0[0] = "NoWiFi";
		} else {
			list0 = listk;
		}

		if (listb != null) {
			for (int i = 0; i < listb.size(); i++) {
				ScanResult scanResult = listb.get(i);
				listk[i] = scanResult.SSID;
				// System.out.println(listk[i]+" ");
				if (listk[i].contains(listw[0]) || listk[i].contains(listw[1]) || listk[i].contains(listw[2])
						|| listk[i].contains(listw[3]) || listk[i].contains(listw[4]) || listk[i].contains(listw[5])
						|| listk[i].contains(listw[6]) || listk[i].contains(listw[7]) || listk[i].contains(listw[8])
						|| listk[i].contains(listw[9])) // 这里是检测的wifi
				{
					ret++;
					// System.out.println(listk[i]+"wugangnan");
				}
			}
		}
		return ret;
	}

}
