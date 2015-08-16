package com.demien.notes.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.StreamCorruptedException;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;

public class SettingsHelper {
	
	private final String appId;
	
	private final Context context;
	
	public SettingsHelper(Context context, String appId) {
		this.context=context;
        this.appId=appId;
	}

	public void saveStringToFile(String fileName, String data)
			throws IOException {

		FileOutputStream fos =context.openFileOutput(fileName, Context.MODE_PRIVATE);
		fos.write(data.getBytes());
		fos.close();
	}

	public String loadStringFromFile(String fileName) throws IOException {

		FileInputStream is = context.openFileInput(fileName);

		final char[] buffer = new char[512];
		final StringBuilder out = new StringBuilder();

		final Reader in = new InputStreamReader(is, "UTF-8");
		try {
			for (;;) {
				int rsz = in.read(buffer, 0, buffer.length);
				if (rsz < 0)
					break;
				out.append(buffer, 0, rsz);
			}
		} finally {
			in.close();
		}
		return out.toString();
	}

	public void putValue(String key, String value) {
		SharedPreferences mPrefs =  context.getSharedPreferences(appId, 0);//  getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor ed = mPrefs.edit();
		ed.putString(key, value);
		ed.commit();
	}

	public String getValue(String key) {
		SharedPreferences mPrefs = context.getSharedPreferences(appId, 0);//getPreferences(MODE_PRIVATE);
		return mPrefs.getString(key, null);
	}

	public void putObject(String key, Object object) throws IOException {
		SharedPreferences mPrefs = context.getSharedPreferences(appId, 0);
		SharedPreferences.Editor ed = mPrefs.edit();
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

		ObjectOutputStream objectOutput;
		objectOutput = new ObjectOutputStream(arrayOutputStream);
		objectOutput.writeObject(object);
		byte[] data = arrayOutputStream.toByteArray();
		objectOutput.close();
		arrayOutputStream.close();

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Base64OutputStream b64 = new Base64OutputStream(out, Base64.DEFAULT);
		b64.write(data);
		b64.close();
		out.close();

		ed.putString(key, new String(out.toByteArray()));

		ed.commit();
	}

	public Object getObject(String key) throws StreamCorruptedException,
			IOException, ClassNotFoundException {
		SharedPreferences mPrefs = context.getSharedPreferences(appId, 0);
		byte[] bytes = mPrefs.getString(key, "{}").getBytes();
		if (bytes.length == 0) {
			return null;
		}
		ByteArrayInputStream byteArray = new ByteArrayInputStream(bytes);
		Base64InputStream base64InputStream = new Base64InputStream(byteArray,
				Base64.DEFAULT);
		ObjectInputStream in;
		in = new ObjectInputStream(base64InputStream);
		Object result = in.readObject();
		in.close();
		return result;
	}

}
