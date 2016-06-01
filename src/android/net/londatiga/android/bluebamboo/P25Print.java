package net.londatiga.android.bluebamboo;

//import net.londatiga.android.bluebamboo.R;
import net.londatiga.android.bluebamboo.pockdata.PocketPos;

import net.londatiga.android.bluebamboo.util.DateUtil;
import net.londatiga.android.bluebamboo.util.FontDefine;
import net.londatiga.android.bluebamboo.util.Printer;
import net.londatiga.android.bluebamboo.util.StringUtil;
import net.londatiga.android.bluebamboo.util.Util;
import net.londatiga.android.bluebamboo.util.DataConstants;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;

import android.os.Build;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;


import android.content.Context;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.apache.cordova.LOG;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Demo Blue Bamboo P25 Thermal Printer.
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 *
 */
public class P25Print extends CordovaPlugin  {
	//private Button mConnectBtn;
	//private Button mEnableBtn;
	//private Button mPrintDemoBtn;
	//private Button mPrintBarcodeBtn;
	//private Button mPrintImageBtn;
	//private Button mPrintReceiptBtn;
	//private Button mPrintTextBtn;
	//private Spinner mDeviceSp;	
	
	//private ProgressDialog mProgressDlg;
	//private ProgressDialog mConnectingDlg;
	
	private BluetoothAdapter mBluetoothAdapter;
	
	private P25Connector mConnector;
	
	private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
    
	
	

	//@Override
	//public boolean onCreateOptionsMenu(Menu menu) {
	//	getMenuInflater().inflate(R.menu.main, menu);
		
//		return true;
//	}
	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		if (item.getItemId() == R.id.action_scan) {
//			mBluetoothAdapter.startDiscovery();
//		}
		
//		return super.onOptionsItemSelected(item);
//	}

//	@Override
//	public void onPause() {
//		if (mBluetoothAdapter != null) {
//			if (mBluetoothAdapter.isDiscovering()) {
//				mBluetoothAdapter.cancelDiscovery();
//			}			
//		}
		
//		if (mConnector != null) {
//			try {
//				mConnector.disconnect();
//			} catch (P25ConnectionException e) {
//				e.printStackTrace();
//			}
//		}
		
//		super.onPause();
//	}
	
//	@Override
	//public void onDestroy() {
	//	unregisterReceiver(mReceiver);
		
	//	super.onDestroy();
	//}
	
	private String[] getArray(ArrayList<BluetoothDevice> data) {
		String[] list = new String[0];
		
		if (data == null) return list;
		
		int size	= data.size();
		list		= new String[size];
		
		for (int i = 0; i < size; i++) {
			list[i] = data.get(i).getName();
		}
		
		return list;	
	}
	
	private void showToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}
	
	//private void updateDeviceList() {
	//	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, getArray(mDeviceList));
		
	//	adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
		
	//	mDeviceSp.setAdapter(adapter);
	//	mDeviceSp.setSelection(0);
	//}
	
	//private void showDisabled() {
	//	showToast("Bluetooth disabled");
		
		//mEnableBtn.setVisibility(View.VISIBLE);		
		//mConnectBtn.setVisibility(View.GONE);
		//mDeviceSp.setVisibility(View.GONE);
	//}
	
	//private void showEnabled() {		
	//	showToast("Bluetooth enabled");
		
	//	mEnableBtn.setVisibility(View.GONE);		
	//	mConnectBtn.setVisibility(View.VISIBLE);
	//	mDeviceSp.setVisibility(View.VISIBLE);
	//}
	
	private void showUnsupported() {
	showToast("Bluetooth is unsupported by this device");

	}

   
   
	//private void showConnected() {
		//showToast("Connected");
		
		//mConnectBtn.setText("Disconnect");
		
		//mPrintDemoBtn.setEnabled(true);
		//mPrintBarcodeBtn.setEnabled(true);
		//mPrintImageBtn.setEnabled(true);
		//mPrintReceiptBtn.setEnabled(true);
		//mPrintTextBtn.setEnabled(true);
		
		//mDeviceSp.setEnabled(false);
//	}
	
	private void showDisonnected() {
	showToast("Disconnected");

	}
	
	private void connect(CordovaArgs args) {
		
        String macAddress = args.getString(0);
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(macAddress);;
		
		if (device.getBondState() == BluetoothDevice.BOND_NONE) {
			try {
				createBond(device);
			} catch (Exception e) {
				showToast("Failed to pair device");
				
				return;
			}
		}
		
		try {
			if (!mConnector.isConnected()) {
				mConnector.connect(device);
			} else {
				mConnector.disconnect();
				
				showDisonnected();
			}
		} catch (P25ConnectionException e) {
			e.printStackTrace();
		}
	}
	
	private void createBond(BluetoothDevice device) throws Exception { 
        
        try {
            Class<?> cl 	= Class.forName("android.bluetooth.BluetoothDevice");
            Class<?>[] par 	= {};
            
            Method method 	= cl.getMethod("createBond", par);
            
            method.invoke(device);
            
        } catch (Exception e) {
            e.printStackTrace();
            
            throw e;
        }
    }
	
	private void sendData(byte[] bytes) {
		try {
			mConnector.sendData(bytes);
		} catch (P25ConnectionException e) {
			e.printStackTrace();
		}
	}
	
	//private void printDemoContent(){
		   
		/*********** print head*******/
	/* 	String receiptHead = "************************" 
				+ "   P25/M Test Print"+"\n"
				+ "************************"
				+ "\n";
		
		long milis		= System.currentTimeMillis();
		
		String date		= DateUtil.timeMilisToString(milis, "MMM dd, yyyy");
		String time		= DateUtil.timeMilisToString(milis, "hh:mm a");
		
		String hwDevice	= Build.MANUFACTURER;
		String hwModel	= Build.MODEL;
		String osVer	= Build.VERSION.RELEASE;
		String sdkVer	= String.valueOf(Build.VERSION.SDK_INT);
		
		StringBuffer receiptHeadBuffer = new StringBuffer(100);
		
		receiptHeadBuffer.append(receiptHead);
		receiptHeadBuffer.append(Util.nameLeftValueRightJustify(date, time, DataConstants.RECEIPT_WIDTH) + "\n");
		
		receiptHeadBuffer.append(Util.nameLeftValueRightJustify("Device:", hwDevice, DataConstants.RECEIPT_WIDTH) + "\n");
		
		receiptHeadBuffer.append(Util.nameLeftValueRightJustify("Model:",  hwModel, DataConstants.RECEIPT_WIDTH) + "\n");
		receiptHeadBuffer.append(Util.nameLeftValueRightJustify("OS ver:", osVer, DataConstants.RECEIPT_WIDTH) + "\n");
		receiptHeadBuffer.append(Util.nameLeftValueRightJustify("SDK:", sdkVer, DataConstants.RECEIPT_WIDTH));
		receiptHead = receiptHeadBuffer.toString();
		
		byte[] header = Printer.printfont(receiptHead + "\n", FontDefine.FONT_32PX,FontDefine.Align_CENTER,(byte)0x1A,PocketPos.LANGUAGE_ENGLISH);
		
			
		*/ /*********** print English text*******/
		/* StringBuffer sb = new StringBuffer();
		for(int i=1; i<128; i++)
			sb.append((char)i);
		String content = sb.toString().trim();
		
		byte[] englishchartext24 			= Printer.printfont(content + "\n",FontDefine.FONT_24PX,FontDefine.Align_CENTER,(byte)0x1A,PocketPos.LANGUAGE_ENGLISH);
		byte[] englishchartext32			= Printer.printfont(content + "\n",FontDefine.FONT_32PX,FontDefine.Align_CENTER,(byte)0x1A,PocketPos.LANGUAGE_ENGLISH);
		byte[] englishchartext24underline	= Printer.printfont(content + "\n",FontDefine.FONT_24PX_UNDERLINE,FontDefine.Align_CENTER,(byte)0x1A,PocketPos.LANGUAGE_ENGLISH);
		
		
		
		*/ /*********** print Tail*******/
		/* String receiptTail =  "Test Completed" + "\n"
				+ "************************" + "\n";
		
		String receiptWeb =  "\n\n\n";
		
		byte[] foot = Printer.printfont(receiptTail,FontDefine.FONT_32PX,FontDefine.Align_CENTER,(byte)0x1A,PocketPos.LANGUAGE_ENGLISH);
		byte[] web	= Printer.printfont(receiptWeb,FontDefine.FONT_32PX,FontDefine.Align_CENTER,(byte)0x1A,PocketPos.LANGUAGE_ENGLISH);
		
		byte[] totladata =  new byte[header.length + englishchartext24.length + englishchartext32.length + englishchartext24underline.length + foot.length + web.length
		                             ];
	 	int offset = 0;
		System.arraycopy(header, 0, totladata, offset, header.length);
		offset += header.length;
		
		System.arraycopy(englishchartext24, 0, totladata, offset, englishchartext24.length);
		offset+= englishchartext24.length;
		
		System.arraycopy(englishchartext32, 0, totladata, offset, englishchartext32.length);
		offset+=englishchartext32.length;
		
		System.arraycopy(englishchartext24underline, 0, totladata, offset, englishchartext24underline.length);
		offset+=englishchartext24underline.length;
		
		

		System.arraycopy(foot, 0, totladata, offset, foot.length);
		offset+=foot.length;
		
		System.arraycopy(web, 0, totladata, offset, web.length);
		offset+=web.length;
		
		byte[] senddata = PocketPos.FramePack(PocketPos.FRAME_TOF_PRINT, totladata, 0, totladata.length);

		sendData(senddata);		
	} */
	
	private void printText(String text) {
		byte[] line 	= Printer.printfont(text + "\n\n", FontDefine.FONT_32PX, FontDefine.Align_CENTER, (byte) 0x1A, 
							PocketPos.LANGUAGE_ENGLISH);
		byte[] senddata = PocketPos.FramePack(PocketPos.FRAME_TOF_PRINT, line, 0, line.length);

		sendData(senddata);		
	}

	
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	    public void onReceive(Context context, Intent intent) {	    	
	        String action = intent.getAction();
	        
	        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
	        	final int state 	= intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
	        	
	        	if (state == BluetoothAdapter.STATE_ON) {
	        		showEnabled();
	        	} else if (state == BluetoothAdapter.STATE_OFF) {
		        	showDisabled();
	        	}
	        } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
	        	mDeviceList = new ArrayList<BluetoothDevice>();
				
				mProgressDlg.show();
	        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
	        	mProgressDlg.dismiss();
	        	
	        	updateDeviceList();
	        } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	        	BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		        
	        	mDeviceList.add(device);
	        	
	        	showToast("Found device " + device.getName());
	        } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
	        	 final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
	        	 
	        	 if (state == BluetoothDevice.BOND_BONDED) {
	        		 showToast("Paired");
	        		 
	        		 connect();
	        	 }
	        }
	    }
	};
	
}