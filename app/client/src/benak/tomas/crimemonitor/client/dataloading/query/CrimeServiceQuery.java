package benak.tomas.crimemonitor.client.dataloading.query;

import org.ksoap2.serialization.SoapObject;

import benak.tomas.crimemonitor.client.dataloading.binding.binder.SoapRequestBinder;
import benak.tomas.crimemonitor.client.dataloading.handler.DataLoadingListener;
import benak.tomas.crimemonitor.client.dataloading.handler.CrimeServiceQueryContext;
import benak.tomas.crimemonitor.client.dataloading.task.SoapCallAsyncTask;
import benak.tomas.crimemonitor.client.dataloading.task.handler.AsyncTaskHandler;
import benak.tomas.crimemonitor.library.binding.binder.TypedParameterBinder;
import benak.tomas.crimemonitor.library.exception.BindingException;
import benak.tomas.crimemonitor.library.exception.TypedQueryInternalErrorException;
import benak.tomas.crimemonitor.library.query.TypedQueryAsync;
import benak.tomas.crimemonitor.library.query.parameter.QueryParameters;
import benak.tomas.crimemonitor.library.query.resultProjector.QueryResultProjector;

public abstract class CrimeServiceQuery<TResult> extends
		TypedQueryAsync<CrimeServiceQueryContext, TResult, SoapObject, String>
{
	private SoapCallAsyncTask mTask;

	public CrimeServiceQuery(CrimeServiceQueryContext config)
			throws TypedQueryInternalErrorException
	{
		super(config);
	}

	protected Exception mException;

	protected abstract String getMethodName();

	@Override
	protected TypedParameterBinder<SoapObject> createParameterBinder()
	{
		return new SoapRequestBinder();
	}

	@Override
	protected abstract QueryResultProjector<String, TResult> createResultProjector();

	@Override
	protected SoapObject createStatement()
	{
		return new SoapObject(mConfig.getTargetNamespace(),
				this.getMethodName());
	}

	public void execute(final DataLoadingListener<TResult> dataLoadingListener,
			final QueryParameters... params)
			throws TypedQueryInternalErrorException
	{
		final SoapObject soapObject = this.createStatement();

		try
		{
			this.bindParametersToQueryString(soapObject, params);
		}
		catch (BindingException e)
		{
			e.printStackTrace();
			throw new TypedQueryInternalErrorException(
					"An error occurred when trying to bind the given parameters to the query statement",
					e);
		}

		mTask = new SoapCallAsyncTask(soapObject, mConfig.getServiceUrl(),
				mConfig.getTargetNamespace(), this.getMethodName(),
				new AsyncTaskHandler<String>()
				{
					@Override
					public void reportOnPreExecute()
					{
						dataLoadingListener.onTaskStarted();
					}

					@Override
					public void reportResult(String result)
					{
						dataLoadingListener.onDataLoaded(
								mResultProjector.projectResult(result), params);
					}

					@Override
					public void reportError(Exception exception)
					{
						dataLoadingListener.onDataLoadingError(exception,
								params);
					}

					@Override
					public void reportEmptyResult()
					{
						dataLoadingListener.onEmptyDataLoaded(params);
					}

					@Override
					public void reportOnCancelled(String result)
					{
						dataLoadingListener.onDataLoadingCancelled(params);
					}
				});

		mTask.execute();
	}

	public void cancel()
	{
		mTask.cancel(true);
	}
}
