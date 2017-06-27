package com.example.wifilisttest;

import java.util.ArrayList;  
import java.util.HashMap;  
import java.util.List;  
import java.util.Map;  

import com.example.wifilisttest.Music;

import android.widget.Button; 
import android.view.View.OnClickListener;
import android.app.Activity;  
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;  
import android.util.Log;  
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.ViewGroup;  
import android.widget.ListView;  
import android.widget.SimpleAdapter;  
import android.widget.TextView;  
import android.widget.Toast;  
  
/** 
 * @���� ��Fragment��Ҫʹ��ListView����Ҫ��ListFragment 
 * */  
public class Fragment1 extends ListFragment   {  
  
    private String TAG = Fragment1.class.getName();  
    private ListView list ;  
    private SimpleAdapter adapter;  
    List<ScanResult> llll=null;
    String[] Flist;
	private TextView Tv;
	private WifiManager wifiManager;
    List<ScanResult> listb;
    int status = 0;	
    int ret = 0;
	private int[] data = new int[100];
	int hasData = 0;
	String[] list0=new String[20];
	int time = 0;
	boolean  startflag = false;
	Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			// ������Ϣ���ɸó����͵�
			if (msg.what == 0x111)
			{
				if(startflag == true && ret != 0)   //��⵽wifi�豸,retΪ����
				{
					Toast.makeText(getActivity(), "there is "+ret+" wifi device found", Toast.LENGTH_LONG).show();
				}
				if(ret == 0)  //���Ϊ0���豸����ʼ����
				{
					Toast.makeText(getActivity(), "no wifi device found , give an alarm!!!!!!!!", Toast.LENGTH_LONG).show();
					getActivity().startService(new Intent(getActivity(),Music.class));
				}
				else
				{
					getActivity().stopService(new Intent(getActivity(),Music.class)); //ֹͣ����
				}
				ret = 0 ;
				wifiManager.startScan();
		        wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
		        listb = wifiManager.getScanResults();
		        //�����ʼ��Ҫע��
		        String[] listk=new String[listb.size()];
		        
		        if(listb==null){list0[0]="NoWiFi";}
		        else{
		        	list0=listk;
		        }
		        if(listb!=null){
		        	for( int i=0;i<listb.size();i++){
		            	ScanResult scanResult = listb.get(i);
		            	listk[i]=scanResult.SSID;
		            	if(listk[i].isEmpty())
		            	list0[i]=listk[i];
		            	 System.out.println(list0[i]+"   ");
		            }
		        }  
		    //    ˢ��wifi�б�
		        adapter = new SimpleAdapter(getActivity(), getData(list0), R.layout.listtest, new String[]{"title"}, new int[]{R.id.title});
		        setListAdapter(adapter);  
		        adapter.notifyDataSetChanged();
		        
				//Toast.makeText(getActivity(),"wifi�ѿ���" , Toast.LENGTH_LONG).show();
				 System.out.println("wifi handler scan"); 	 
			}
		}
	};	
	/** 
     * @���� ��onCreateView�м��ز��� 
     * */  
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        View view = inflater.inflate(R.layout.fragment1, container,false);  
        list = (ListView) view.findViewById(android.R.id.list);  
        Tv=(TextView)view.findViewById(R.id.tv);
        Tv.setText("wifi�Զ����v2.0");
        Log.i(TAG, "--------onCreateView");  
        return view;  
    }  
  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
       super.onCreate(savedInstanceState);  
       Bundle b = savedInstanceState;  
        Log.i(TAG, "--------onCreate");  
        wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
        listb = wifiManager.getScanResults();
        //�����ʼ��Ҫע��
        String[] listk=new String[listb.size()];
        if(listb!=null){
        	for( int i=0;i<listb.size();i++){
            	ScanResult scanResult = listb.get(i);
            	listk[i]=scanResult.SSID;
            }
        }    
      //  String[] list0=new String[listb.size()];
        if(listb==null)
        {
        	list0[0]="NoWiFi";
        }
        else
        {
        	list0=listk;
        }

        //����Fragment1��ӦR.layout.listtest��������ļ�
        adapter = new SimpleAdapter(getActivity(), getData(list0), R.layout.listtest, new String[]{"title"}, new int[]{R.id.title});  
        setListAdapter(adapter);           
    }  
      
    @Override  
    public void onListItemClick(ListView l, View v, int position, long id) {  //���wifi�б�ص�
        super.onListItemClick(l, v, position, id);  
        ScanResult scanResult = listb.get(position);
        if(wifiManager.setWifiEnabled(true))
        {
        //	 Toast.makeText(getActivity(),scanResult.SSID+position , Toast.LENGTH_LONG).show();
        	//Toast.makeText(getActivity(),"wifi�ѿ���" , Toast.LENGTH_LONG).show();
        }
        else{
        	Toast.makeText(getActivity(),"wifiû��" , Toast.LENGTH_LONG).show();
        }
        System.out.println(l.getChildAt(position));  
        HashMap<String, Object> view= (HashMap<String, Object>) l.getItemAtPosition(position);  
        System.out.println(view.get("title").toString()+"+++++++++title");  
     //   Toast.makeText(getActivity(), TAG+l.getItemIdAtPosition(position), Toast.LENGTH_LONG).show();  
        System.out.println(v);         
        System.out.println(position);         
    }  
      
	public int doWork()  //����һ����ʱ����
	{
		// Ϊ����Ԫ�ظ�ֵ
		data[hasData++] = (int) (Math.random() * 100);
		if(hasData == 99)
			hasData = 0;
		try
		{
			Thread.sleep(370);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		return hasData;
	}
      
  public int scanwifi()  //ʵʱɨ��wifi������wifi SSID����listk��
  {
      listb = wifiManager.getScanResults();
      int ret = 0;
      //�����ʼ��Ҫע��
      String[] listk=new String[listb.size()];
      if(listb!=null){
      	for( int i=0;i<listb.size();i++){
          	ScanResult scanResult = listb.get(i);
          	listk[i]=scanResult.SSID;
          }
      }
           
      String[] list0=new String[listb.size()];
      if(listb==null){list0[0]="NoWiFi";}
      else{
      	list0=listk;
      }
      
      if(listb!=null){
      	for( int i=0;i<listb.size();i++){
          	ScanResult scanResult = listb.get(i);
          	listk[i]=scanResult.SSID;
          //	System.out.println(listk[i]+" ");
          	if(listk[i].contains("AI-THINKER")||listk[i].contains("wugangnan") )  //���������Ҫ����wifi����
          	{
          		ret++;
          //		System.out.println(listk[i]+"wugangnan");
          	}
          }
      }
	  return ret;
  }
    private List<? extends Map<String, ?>> getData(String[] strs) {     //�����ַ���ת��
        List<Map<String ,Object>> list = new ArrayList<Map<String,Object>>();  
          
        for (int i = 0; i < strs.length; i++) {  
            Map<String, Object> map = new HashMap<String, Object>();  
            map.put("title", strs[i]);  
            list.add(map);       
        }  
        return list;  
    }  
  
    @Override  
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
		final Button button = (Button) getActivity().findViewById(R.id.button);
		final TextView textView = (TextView) getActivity().findViewById(R.id.textview1);
		getActivity().stopService(new Intent(getActivity(),Music.class));
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { //��ť����¼�
				time++;
				time = time%2;
				if(time==0)     //start scan wifi
				{
					startflag = false;
					button.setText("start scan wifi");
					Toast.makeText(getActivity(), "ֹͣɨ��wifi", Toast.LENGTH_LONG).show();
					getActivity().stopService(new Intent(getActivity(),Music.class));
				}
				else	//stop scan wifi
				{
					startflag = true;
					button.setText("stop scan wifi");
					Toast.makeText(getActivity(), "���ڿ�ʼɨ��wifi", Toast.LENGTH_LONG).show();
				}
				
			 	new Thread()   //����ɨ���̣߳�40ms����һ��wifi�б���ˢ��������handler����
				{
					public void run()
					{
						int value = 0;
						while (startflag)
						{
							// ��ȡ��ʱ��������ɰٷֱ�
							status = doWork();
							ret = scanwifi();
							if (ret!=0){
								System.out.println("there is "+ret+"  wifi found suceessful!");
							}
							value++;
							// ������Ϣ
							if (value==10){
							//	Toast.makeText(getActivity(), "wugangnan", Toast.LENGTH_LONG).show();  
							mHandler.sendEmptyMessage(0x111);  //����Ϣ��handleMessage����
							value = 0;
							}
						}
					}
				}.start();

			}
		});
        Log.i(TAG, "--------onActivityCreated");  
    }  
      
    @Override  
    public void onAttach(Activity activity) {  
        super.onAttach(activity);  
        Log.i(TAG, "----------onAttach");  
    }  
}  

