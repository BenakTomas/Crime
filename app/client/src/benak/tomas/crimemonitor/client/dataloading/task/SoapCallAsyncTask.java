package benak.tomas.crimemonitor.client.dataloading.task;

import java.net.SocketTimeoutException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import benak.tomas.crimemonitor.client.dataloading.task.handler.AsyncTaskHandler;
import benak.tomas.crimemonitor.client.exception.DataLoadingTaskTimeoutExceededException;
import android.os.AsyncTask;

public class SoapCallAsyncTask extends AsyncTask<Void, Void, String>
{
	private final SoapObject mSoapObject;
	private final String mServiceUrl;
	private final String mTargetNamespace;
	private final String mMethodName;
	private final String mQualifiedMethodName;
	private final AsyncTaskHandler<String> mTaskHandler;
	private Exception mException;
	
	
	public SoapCallAsyncTask(SoapObject soapObject, String serviceUrl, String targetNamespace, String methodName, AsyncTaskHandler<String> taskHandler)
	{
		if(soapObject == null)
			throw new IllegalArgumentException("'soapObject' cannot be null");
		if(serviceUrl == null)
			throw new IllegalArgumentException("'serviceUrl' cannot be null");
		if(targetNamespace == null)
			throw new IllegalArgumentException("'targetNamespace' cannot be null");
		if(methodName == null)
			throw new IllegalArgumentException("'methodName' cannot be null");
		if(taskHandler == null)
			throw new IllegalArgumentException("'taskHandler' cannot be null");
		
		mSoapObject = soapObject;
		mServiceUrl = serviceUrl;
		mTargetNamespace = targetNamespace;
		mMethodName = methodName;
		mTaskHandler = taskHandler;
		
		mQualifiedMethodName = String.format("%s/%s", mTargetNamespace, mMethodName);
	}

	@Override
	protected String doInBackground(Void... params)
	{
		try
		{
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			// set up marshalling
			MarshalFloat floatMarshaller = new MarshalFloat();
			floatMarshaller.register(envelope);

			envelope.setOutputSoapObject(mSoapObject);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					mQualifiedMethodName);

			androidHttpTransport.call(mServiceUrl, envelope);

			SoapPrimitive resultsRequestSOAP = (SoapPrimitive) envelope
					.getResponse();

			return resultsRequestSOAP.toString();
		}
		// encapsulating the original timeout exception
		catch (SocketTimeoutException e)
		{
			mException = new DataLoadingTaskTimeoutExceededException(e);
			// mException = new
			// DataLoadingTaskException("Connect or read timeout occurred.", e);
		}
		// If any other exception was caught, simply store it.
		catch (Exception e)
		{
			mException = e;
		}

		// the default is to return null
		return null;
	}

	/**
	 * Standard {@code AsyncTask#onPreExecute} override.
	 * <p>
	 * It only calls the current async task handler, if it is not null.
	 */
	@Override
	protected void onPreExecute()
	{
		if (mTaskHandler != null)
			mTaskHandler.reportOnPreExecute();
	}

	/**
	 * Standard {@code AsyncTask#onPostExecute} override.
	 * <p>
	 * It only calls the current async task handler, if it is not null.
	 */
	@Override
	protected void onPostExecute(String result)
	{
		if (mException != null)
			mTaskHandler.reportError(mException);
		else if (result == null)
			mTaskHandler.reportEmptyResult();
		else
			mTaskHandler.reportResult(result);
	}

	/**
	 * Standard {@code AsyncTask#onCancelled(Object)} override.
	 * <p>
	 * It only calls the current async task handler, if it is not null.
	 */
	@Override
	protected void onCancelled(String result)
	{
		if (mTaskHandler != null)
			mTaskHandler.reportOnCancelled(result);
	}
}