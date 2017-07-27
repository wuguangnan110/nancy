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
	static int WifiListNum = 10; //�����б����� 
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
		// ����Fragment1��ӦR.layout.listtest��������ļ�
		wifiAdapter = new WifiAdapter(this, listb);
//		adapter = new SimpleAdapter(this, getData(list0), R.layout.listtest, new String[] { "title" },
//				new int[] { R.id.title });

		list = (ListView) findViewById(android.R.id.list);
		Tv = (TextView) findViewById(R.id.tv);
		Tv.setText("wifi�Զ����v4.0");
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
			public boolean onItemLongClick(AdapterView adapter,View view,int position,long id) {  //�����¼�
				// TODO Auto-generated method stub
				// When clicked, show a toast with the TextView text
				ScanResult scanResult = listb.get(position);
				
				for (int i = 0; i < WifiListNum; i++) {
					if (listw[i] == scanResult.SSID) {
						mToast.setText("�����ظ���� " + listw[i] + " �豸");
						mToast.show();	
						return true;
					}	
				}
				value = inputtime+1;
				if (inputtime < WifiListNum )
				{
					listw[inputtime] = scanResult.SSID;
					mToast.setText("wifi�豸 " + listw[inputtime] + " �Ѽ�����,Ŀǰ�����"+value+"̨�豸");
					mToast.show();
					inputtime++;
				}
				else 
				{
					mToast.setText("wifi�����б�������������������");
					mToast.show();
				}
				return true;//������trueʱ,���ᴥ���̰��¼�
			}
		}); 
		button = (Button) findViewById(R.id.button);
		// final TextView textView = (TextView)
		// getActivity().findViewById(R.id.textview1);
		stopService(new Intent(this, Music.class));
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { // ��ť����¼�
				time++;
				time = time % 2;
				mToast.setGravity(Gravity.CENTER, 0, 0);
				if (time == 0) // start scan wifi
				{
					startflag = false;
					button.setText("��ʼɨ��");
					// Toast.makeText(getActivity(), "ֹͣɨ��wifi",
					// Toast.LENGTH_LONG).show();
					mToast.setText("ֹͣɨ��wifi");
					mToast.show();
					stopService(new Intent(MainActivity.this, Music.class));
				}
				else // stop scan wifi
				{
					startflag = true;
					button.setText("ֹͣɨ��");
					// Toast.makeText(getActivity(), "���ڿ�ʼɨ��wifi",
					// Toast.LENGTH_LONG).show();
					mToast.setText("���ڿ�ʼɨ��wifi");
					mToast.show();
				}

				new Thread() // ����ɨ���̣߳�40ms����һ��wifi�б���ˢ��������handler����
				{
					public void run() {
						int value = 0;
						while (startflag) {
							// ��ȡ��ʱ��������ɰٷֱ�
							ret = scanwifi();
							if (ret != 0) {
							//	System.out.println("Ŀǰɨ�跢�� " + ret + "  ̨wifi�豸!");
							}
							value++;
							// ������Ϣ
							if (value == 8) {
								mHandler.sendEmptyMessage(0x111); // ����Ϣ��handleMessage����
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
			// ������Ϣ���ɸó����͵�
			if (msg.what == 0x111) {
				
				if (startflag == true && ret != 0) // ��⵽wifi�豸,retΪ����
				{
					if (inputtime == ret)
					{
						mToast.setText("Ŀǰ�ɹ����� " + ret + " ̨wifi�豸,���豸��ʧ");
						mToast.show();
						stopService(new Intent(MainActivity.this, Music.class)); // ֹͣ����
					}
					else
					{
						mToast.setText("Ŀǰ�ɹ����� " + ret + " ̨wifi�豸, "+(inputtime - ret) +" ̨�豸��ʧ");
						mToast.show();
						startService(new Intent(MainActivity.this, Music.class));
					}
				}

				if (ret == 0) // ���Ϊ0���豸����ʼ����
				{
					mToast.setText("Ŀǰû�з����κ�wifi�豸�����飡��");
					mToast.show();
					startService(new Intent(MainActivity.this, Music.class));
				} 
				else 
				{
				//	stopService(new Intent(MainActivity.this, Music.class)); // ֹͣ����
				}
				ret = 0;
				wifiManager.startScan();
				listb = wifiManager.getScanResults();
				// �����ʼ��Ҫע��
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

//	private List<? extends Map<String, ?>> getData(String[] strs) { // �����ַ���ת��
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
						mToast.setText("�����ظ���� " + listw[i] + " �豸");
						mToast.show();	
						return;
					}	
				}
				value = inputtime+1;
				if (inputtime < WifiListNum )
				{
					listw[inputtime] = m_editText.getText().toString();
					mToast.setText("wifi�豸 " + listw[inputtime] + " �Ѽ�����,Ŀǰ�����"+value+"̨�豸");
					mToast.show();
					inputtime++;
				}
				else 
				{
					mToast.setText("wifi�����б�������������������");
					mToast.show();
				}
			}
			else
			{
				mToast.setText("��������ȷwifi����");
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
			// Toast.makeText(getActivity(), "�������wifi�����б�",
			// Toast.LENGTH_LONG).show();
			mToast.setText("�������wifi�����б�");
			mToast.show();
			m_editText.setText(null);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> l, View v, int position, long id) {
		ScanResult scanResult = listb.get(position);
		if (wifiManager.setWifiEnabled(true))
		{
		//	mToast.setText(scanResult.SSID+" �ǿ���״̬");
		//	mToast.show();
		}
		else 
		{
			//mToast.setText(scanResult.SSID+" �ǹر�״̬");
			//mToast.show();
		}
	}

	public int doWork() // ����һ����ʱ����
	{
		// Ϊ����Ԫ�ظ�ֵ
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

	public int scanwifi() // ʵʱɨ��wifi������wifi SSID����listk��
	{
		listb = wifiManager.getScanResults();
		int ret = 0;
		// �����ʼ��Ҫע��
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
						|| listk[i].contains(listw[9])) // �����Ǽ���wifi
				{
					ret++;
					// System.out.println(listk[i]+"wugangnan");
				}
			}
		}
		return ret;
	}

}
