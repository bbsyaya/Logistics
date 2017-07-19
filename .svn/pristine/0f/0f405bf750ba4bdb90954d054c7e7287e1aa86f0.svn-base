package com.jintoufs.logstics.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;

import android.content.res.AssetManager;

import com.jintoufs.logstics.AppContext;
import com.loopj.android.http.AsyncHttpClient;

public class ApiClientHelper {
	private static AsyncHttpClient client;

	/**
	 * 获得请求的服务端数据的userAgent
	 * 
	 * @param appContext
	 * @return
	 */
	public static String getUserAgent(AppContext appContext) {
		StringBuilder ua = new StringBuilder("hontai");
		ua.append('/' + appContext.getPackageInfo().versionName + '_' + appContext.getPackageInfo().versionCode);// app版本信息
		ua.append("/Android");// 手机系统平台
		ua.append("/" + android.os.Build.VERSION.RELEASE);// 手机系统版本
		ua.append("/" + android.os.Build.MODEL); // 手机型号
		ua.append("/" + appContext.getAppId());// 客户端唯一标识
		return ua.toString();
	}

	/**
	 * 初始化HttpClient对象
	 * 
	 * @param params
	 * @return
	 */
	public static synchronized AsyncHttpClient getAsyncHttpClient(AppContext context) {
		if (null == client) {
			AssetManager am = context.getAssets();
			InputStream ins = null;
			try {
				ins = am.open("server.cer");
				// 读取证书
				CertificateFactory cerFactory = CertificateFactory.getInstance("X.509"); // 问1
				// 创建一个证书库，并将证书导入证书库
				KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC"); // 问2
				keyStore.load(null, null);
				Certificate cer = cerFactory.generateCertificate(ins);
				keyStore.setCertificateEntry("server", cer);
				SSLSocketFactory sf = new SSLSocketFactoryEx(keyStore);
				sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER); // 允许所有主机的验证

				// 设置http https支持

				SchemeRegistry schReg = new SchemeRegistry();

				schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

				schReg.register(new Scheme("https", sf, 443));

				client = new AsyncHttpClient(schReg);

			} catch (Exception e) {

				e.printStackTrace();

				return new AsyncHttpClient();

			} finally {
				try {
					if (ins != null) {
						ins.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		return client;

	}
}

class SSLSocketFactoryEx extends SSLSocketFactory {

	SSLContext sslContext = SSLContext.getInstance("TLS");

	public SSLSocketFactoryEx(KeyStore truststore)

	throws NoSuchAlgorithmException, KeyManagementException,

	KeyStoreException, UnrecoverableKeyException {

		super(truststore);

		TrustManager tm = new X509TrustManager() {

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {

				return null;

			}

			@Override
			public void checkClientTrusted(

			java.security.cert.X509Certificate[] chain, String authType)

			throws java.security.cert.CertificateException {

			}

			@Override
			public void checkServerTrusted(

			java.security.cert.X509Certificate[] chain, String authType)

			throws java.security.cert.CertificateException {

			}

		};

		sslContext.init(null, new TrustManager[] { tm }, null);

	}

	@Override
	public Socket createSocket(Socket socket, String host, int port,

	boolean autoClose) throws IOException, UnknownHostException {

		return sslContext.getSocketFactory().createSocket(socket, host, port,

		autoClose);

	}

	@Override
	public Socket createSocket() throws IOException {

		return sslContext.getSocketFactory().createSocket();

	}

}
