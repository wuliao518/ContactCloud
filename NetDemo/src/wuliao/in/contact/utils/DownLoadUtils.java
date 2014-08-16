package wuliao.in.contact.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;


public class DownLoadUtils {
	public static void downLoad(String url,String file,ProgressDialog progressDialog) throws Exception{
			String path=url;
			
						
			File myFile=new File(file);
			if(!myFile.exists()){
				myFile.createNewFile();
			}	
			int len=0;
			int process = 0;
			byte[] buffer=new byte[1024];
			FileOutputStream out=new FileOutputStream(myFile);
			URL myUrl = new URL(path);
			System.out.println(myUrl.toString());
			HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();
			conn.setConnectTimeout(2000);
			conn.setRequestMethod("GET");
			if(conn.getResponseCode()==200){
				int total = conn.getContentLength();
				progressDialog.setMax(total);
				InputStream in=conn.getInputStream();
				while((len=in.read(buffer))>=0){
					process=process+len;
					progressDialog.setProgress(process);
					out.write(buffer, 0, len);
					out.flush();
				}
				in.close();
			}
			out.close();
		
	}
}
