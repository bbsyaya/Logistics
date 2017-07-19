package com.jintoufs.logstics.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;

public class AsynHttpClientSslHelper {
	private static final String KEY_STORE_TYPE_BKS = "bks";

	private static final String KEY_STORE_TYPE_P12 = "PKCS12";

	private static final String KEY_STORE_CLIENT_PATH = "mykey.cer";

	private static final String KEY_STORE_TRUST_PATH = "tomcat.cer";

	private static final String KEY_STORE_PASSWORD = "jintoufs";

	private static final String KEY_STORE_TRUST_PASSWORD = "jintoufs";

	private static KeyStore keyStore;

	private static KeyStore trustStore;

	private static AsyncHttpClient client;

	public static AsyncHttpClient getAsyncHttpsClient(Context pContext) {
		if (null == client) {
			try {

				// 服务器端需要验证的客户端证书

				keyStore = KeyStore.getInstance(KEY_STORE_TYPE_P12);

				// 客户端信任的服务器端证书

				trustStore = KeyStore.getInstance(KEY_STORE_TYPE_BKS);

				InputStream ksIn = pContext.getResources().getAssets().open(KEY_STORE_CLIENT_PATH);

				InputStream tsIn = pContext.getResources().getAssets().open(KEY_STORE_TRUST_PATH);

				try {

					keyStore.load(ksIn, KEY_STORE_PASSWORD.toCharArray());

					trustStore.load(tsIn, KEY_STORE_TRUST_PASSWORD.toCharArray());

				} catch (Exception e) {

					e.printStackTrace();

				} finally {

					try {

						ksIn.close();

					} catch (Exception ignore) {

					}

					try {

						tsIn.close();

					} catch (Exception ignore) {

					}

				}

				SSLSocketFactory socketFactory = new SSLSocketFactory(keyStore, KEY_STORE_PASSWORD, trustStore);

				SchemeRegistry schReg = new SchemeRegistry();

				schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

				schReg.register(new Scheme("https", socketFactory, 443));

				client = new AsyncHttpClient(schReg);

			} catch (KeyManagementException e) {

				e.printStackTrace();

			} catch (UnrecoverableKeyException e) {

				e.printStackTrace();

			} catch (KeyStoreException e) {

				e.printStackTrace();

			} catch (FileNotFoundException e) {

				e.printStackTrace();

			} catch (NoSuchAlgorithmException e) {

				e.printStackTrace();

			} catch (ClientProtocolException e) {

				e.printStackTrace();

			} catch (IOException e) {

				e.printStackTrace();

			}
		}
		return client;

	}

}
