package com.example.user.trucksales.Negocio;

import java.io.FileOutputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

@SuppressLint("NewApi")
public class PathtoRight {
private FileOutputStream os;
	//дlog�ļ����ڴ濨
	//��ȡ·��
	public static String filePath(Uri fileUrl,Context context){
		String fileName="";
		   Uri filePathUri = fileUrl;
		if (fileUrl != null)
	       {
	           if (fileUrl.getScheme().toString().compareTo("content") == 0)
	           {
	               // content://��ͷ��uri
	              Cursor cursor =context.getContentResolver().query(fileUrl, null, null, null, null);
	              if (cursor != null && cursor.moveToFirst())
	              {
	                  int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	                  fileName = cursor.getString(column_index); // ȡ���ļ�·��
	 
	                  // Android 4.1 ������SD��Ŀ¼��sdcardӳ�䵽/storage/sdcard0
	                  if (!fileName.startsWith("/storage") && !fileName.startsWith("/mnt"))
	                  {
	                     // ����Ƿ��С�/mnt��ǰ׺
	                     fileName = "/mnt" + fileName;
	                  }
	                  cursor.close();
	              }
	           }
	           else if (fileUrl.getScheme().compareTo("file") == 0) // file:///��ͷ��uri
	           {
	              fileName = filePathUri.toString().replace("file://", "");
	              int index = fileName.indexOf("/sdcard");
	              fileName  = index == -1 ? fileName : fileName.substring(index);
	              if (!fileName.startsWith("/mnt"))
	              {
	                  // ����"/mnt"ͷ
	                  fileName += "/mnt";
	              }
	           }
	       }
		return fileName;
	}

}		
	

