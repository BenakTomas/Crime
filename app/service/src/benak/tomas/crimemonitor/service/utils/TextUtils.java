package benak.tomas.crimemonitor.service.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class TextUtils
{
	public static byte[] readFile(InputStream is) 
			  throws IOException 
	{
		ByteArrayOutputStream outByteArrayStream = new ByteArrayOutputStream();
		
		final int BUFFER_SIZE = 4092;
		byte[] readBuffer = new byte[BUFFER_SIZE];
		int bytesRead;
		
		while((bytesRead = is.read(readBuffer)) != -1)
		{
			outByteArrayStream.write(readBuffer, 0, bytesRead);
		}
		
		outByteArrayStream.flush();
		
		return outByteArrayStream.toByteArray();
	}
}
