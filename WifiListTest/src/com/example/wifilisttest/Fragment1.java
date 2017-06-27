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
 * @描述 在Fragment中要使用ListView，就要用ListFragment 
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
			// 表明消息是由该程序发送的
			if (msg.what == 0x111)
			{
				if(startflag == true && ret != 0)   //检测到wifi设备,ret为个数
				{
					Toast.makeText(getActivity(), "there is "+ret+" wifi device found", Toast.LENGTH_LONG).show();
				}
				if(ret == 0)  //检测为0个设备，开始报警
				{
					Toast.makeText(getActivity(), "no wifi device found , give an alarm!!!!!!!!", Toast.LENGTH_LONG).show();
					getActivity().startService(new Intent(getActivity(),Music.class));
				}
				else
				{
					getActivity().stopService(new Intent(getActivity(),Music.class)); //停止报警
				}
				ret = 0 ;
				wifiManager.startScan();
		        wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
		        listb = wifiManager.getScanResults();
		        //数组初始化要注意
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
		    //    刷新wifi列表
		        adapter = new SimpleAdapter(getActivity(), getData(list0), R.layout.listtest, new String[]{"title"}, new int[]{R.id.title});
		        setListAdapter(adapter);  
		        adapter.notifyDataSetChanged();
		        
				//Toast.makeText(getActivity(),"wifi已开启" , Toast.LENGTH_LONG).show();
				 System.out.println("wifi handler scan"); 	 
			}
		}
	};	
	/** 
     * @描述 在onCreateView中加载布局 
     * */  
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        View view = inflater.inflate(R.layout.fragment1, container,false);  
        list = (ListView) view.findViewById(android.R.id.list);  
        Tv=(TextView)view.findViewById(R.id.tv);
        Tv.setText("wifi自动检测v2.0");
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
        //数组初始化要注意
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

        //设置Fragment1对应R.layout.listtest这个布局文件
        adapter = new SimpleAdapter(getActivity(), getData(list0), R.layout.listtest, new String[]{"title"}, new int[]{R.id.title});  
        setListAdapter(adapter);           
    }  
      
    @Override  
    public void onListItemClick(ListView l, View v, int position, long id) {  //点击wifi列表回调
        super.onListItemClick(l, v, position, id);  
        ScanResult scanResult = listb.get(position);
        if(wifiManager.setWifiEnabled(true))
        {
        //	 Toast.makeText(getActivity(),scanResult.SSID+position , Toast.LENGTH_LONG).show();
        	//Toast.makeText(getActivity(),"wifi已开启" , Toast.LENGTH_LONG).show();
        }
        else{
        	Toast.makeText(getActivity(),"wifi没打开" , Toast.LENGTH_LONG).show();
        }
        System.out.println(l.getChildAt(position));  
        HashMap<String, Object> view= (HashMap<String, Object>) l.getItemAtPosition(position);  
        System.out.println(view.get("title").toString()+"+++++++++title");  
     //   Toast.makeText(getActivity(), TAG+l.getItemIdAtPosition(position), Toast.LENGTH_LONG).show();  
        System.out.println(v);         
        System.out.println(position);         
    }  
      
	public int doWork()  //增加一个耗时操作
	{
		// 为数组元素赋值
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
      
  public int scanwifi()  //实时扫描wifi，并将wifi SSID加入listk中
  {
      listb = wifiManager.getScanResults();
      int ret = 0;
      //数组初始化要注意
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
          	if(listk[i].contains("AI-THINKER")||listk[i].contains("wugangnan") )  //这里添加想要检测的wifi名称
          	{
          		ret++;
          //		System.out.println(listk[i]+"wugangnan");
          	}
          }
      }
	  return ret;
  }
    private List<? extends Map<String, ?>> getData(String[] strs) {     //数据字符串转换
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
			public void onClick(View v) { //按钮点击事件
				time++;
				time = time%2;
				if(time==0)     //start scan wifi
				{
					startflag = false;
					button.setText("start scan wifi");
					Toast.makeText(getActivity(), "停止扫描wifi", Toast.LENGTH_LONG).show();
					getActivity().stopService(new Intent(getActivity(),Music.class));
				}
				else	//stop scan wifi
				{
					startflag = true;
					button.setText("stop scan wifi");
					Toast.makeText(getActivity(), "现在开始扫描wifi", Toast.LENGTH_LONG).show();
				}
				
			 	new Thread()   //建立扫描线程，40ms更新一次wifi列表，将刷新任务交于handler处理
				{
					public void run()
					{
						int value = 0;
						while (startflag)
						{
							// 获取耗时操作的完成百分比
							status = doWork();
							ret = scanwifi();
							if (ret!=0){
								System.out.println("there is "+ret+"  wifi found suceessful!");
							}
							value++;
							// 发送消息
							if (value==10){
							//	Toast.makeText(getActivity(), "wugangnan", Toast.LENGTH_LONG).show();  
							mHandler.sendEmptyMessage(0x111);  //发消息给handleMessage处理
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

