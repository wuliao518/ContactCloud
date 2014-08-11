package wuliao.in.contact.net;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import wuliao.in.contact.config.Config;
import android.os.AsyncTask;
import android.widget.Toast;

public class NetConnection {
	public NetConnection(String url,final String model,final String action,final String method,final SuccessCallback successCallback,final FailCallback failCallback,final String ...args){
		
		final String requestUrl=url+File.separator+model+File.separator+action;
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... param) {
				try {
					List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>(); 
					BasicHttpParams httpParams = new BasicHttpParams();  
				    HttpConnectionParams.setConnectionTimeout(httpParams, Config.REQUEST_TIMEOUT);  
				    HttpConnectionParams.setSoTimeout(httpParams, Config.SO_TIMEOUT);  
					HttpClient httpClient=new DefaultHttpClient(httpParams);
					if(method=="POST"){
						for(int i = 0;i<args.length;i+=2){
							params.add(new BasicNameValuePair(args[i], args[i+1]));
						}
						HttpPost httpPost=new HttpPost(requestUrl);
						httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
						HttpResponse response=httpClient.execute(httpPost);
						if(response.getStatusLine().getStatusCode()==200){
							return EntityUtils.toString(response.getEntity(), "utf-8");
						}
						return null;
					}else{
						StringBuilder builder=new StringBuilder();
						if((args!=null)&&(args.length>0)){
							for(int i = 0;i<args.length;i+=2){
								builder.append(args[i]+"="+args[i+1]+"&");
							}	
						}else{
							builder.append("&");
						}
						builder.deleteCharAt(builder.length()-1);
						HttpGet httpGet=new HttpGet(requestUrl+"?"+builder);
						HttpResponse response=httpClient.execute(httpGet);
						if(response.getStatusLine().getStatusCode()==200){
							return EntityUtils.toString(response.getEntity(), "utf-8");
						}
						return null;
					}													
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				if(result==null){
					failCallback.onFail();
				}else{
					successCallback.onSuccess(result);
				}
				super.onPostExecute(result);
			}					
		}.execute();	
	}
	public interface SuccessCallback{
		void onSuccess(String str);
	}
	public interface FailCallback{
		void onFail();
	}
}
