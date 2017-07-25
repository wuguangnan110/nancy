package com.example.wifilisttest;

import java.util.ArrayList;  
import android.text.TextUtils;
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
import android.widget.EditText;
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
	String[] listw=new String[10];
	int time = 0;
	boolean  startflag = false;
	private Button        m_button;
	private Button        m_button1;
	private EditText    m_editText;
	static  int inputtime= 0;
	private Toast mToast;
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
					//Toast.makeText(getActivity(), "Ŀǰ�ɹ����� "+ret+" ̨wifi�豸", Toast.LENGTH_LONG).show();
					mToast.setText("Ŀǰ�ɹ����� "+ret+" ̨wifi�豸");
			        mToast.show();
				}
				if(ret == 0)  //���Ϊ0���豸����ʼ����
				{
					//Toast.makeText(getActivity(), "no wifi device found , give an alarm!!!!!!!!", Toast.LENGTH_LONG).show();
					mToast.setText("Ŀǰû�з��� �κ�wifi�豸�����飡��");
			        mToast.show();
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
        Tv.setText("wifi�Զ����v3.0");
        Log.i(TAG, "--------onCreateView");  
        m_button = (Button)view.findViewById(R.id.editBtn1);
        m_button1 = (Button)view.findViewById(R.id.editBtn2);
        m_editText = (EditText)view.findViewById(R.id.editText);
        m_editText.selectAll();
        m_button.setOnClickListener(new ButtonListener());
        m_button1.setOnClickListener(new ButtonListener1());
        for( int i=0;i<10;i++){
        	listw[i] = "no wifi";
        }
        mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
        return view;  
    }  
    class ButtonListener implements OnClickListener {

        public void onClick(View v) {
        	
            // TODO Auto-generated method stub
            Log.v("EditText", m_editText.getText().toString());
            if( !TextUtils.isEmpty(m_editText.getText()) && !m_editText.getText().toString().trim().isEmpty()) {
	            listw[inputtime]=m_editText.getText().toString();
//	            Toast.makeText(getActivity(), "wifi���� "+listw[inputtime]+" �Ѽ�����", Toast.LENGTH_LONG).show();
	            mToast.setText("wifi���� "+listw[inputtime]+" �Ѽ�����");
            	mToast.show();
	            inputtime++;
            }
            else 
            {
            	mToast.setText("��������ȷwifi����");
            	mToast.show();
//            	Toast.makeText(getActivity(), "��������ȷwifi����", Toast.LENGTH_LONG).show();
            }
            if(inputtime > 9)
            	inputtime = 0;
        }
    }
    class ButtonListener1 implements OnClickListener {

        public void onClick(View v) {
            // TODO Auto-generated method stub
            Log.v("EditText", m_editText.getText().toString());
            inputtime = 0;
            for( int i=0;i<10;i++){
            	listw[i] = "no wifi";
            }
         //   Toast.makeText(getActivity(), "�������wifi�����б�", Toast.LENGTH_LONG).show();
            mToast.setText("�������wifi�����б�");
        	mToast.show();
        }
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
          	if(listk[i].contains(listw[0])||listk[i].contains(listw[1])||listk[i].contains(listw[2])|| 
          	   listk[i].contains(listw[3])||listk[i].contains(listw[4])||listk[i].contains(listw[5])||
          	   listk[i].contains(listw[6])||listk[i].contains(listw[7])||listk[i].contains(listw[8])||
          	   listk[i].contains(listw[9]))  //���������Ҫ����wifi����
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
		//final TextView textView = (TextView) getActivity().findViewById(R.id.textview1);
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
					//Toast.makeText(getActivity(), "ֹͣɨ��wifi", Toast.LENGTH_LONG).show();
					mToast.setText("ֹͣɨ��wifi");
			        mToast.show();
					getActivity().stopService(new Intent(getActivity(),Music.class));
				}
				else	//stop scan wifi
				{
					startflag = true;
					button.setText("stop scan wifi");
					//Toast.makeText(getActivity(), "���ڿ�ʼɨ��wifi", Toast.LENGTH_LONG).show();
					mToast.setText("���ڿ�ʼɨ��wifi");
			        mToast.show();
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
								System.out.println("Ŀǰɨ�跢�� "+ret+"  ̨wifi�豸!");
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

