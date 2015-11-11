package benak.tomas.crimemonitor.client.dataloadings;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import benak.tomas.crimemonitor.client.dataloading.handler.DataLoadingListener;
import benak.tomas.crimemonitor.client.dataloading.handler.CrimeServiceQueryContext;
import benak.tomas.crimemonitor.client.dataloading.query.CrimeServiceQuery;
import benak.tomas.crimemonitor.client.exception.DataLoadingTaskException;
import benak.tomas.crimemonitor.library.exception.TypedQueryInternalErrorException;
import benak.tomas.crimemonitor.library.query.parameter.QueryParameters;
import benak.tomas.crimemonitor.library.query.resultProjector.QueryResultProjector;

public abstract class DataLoadingFragment<TData> extends Fragment
implements DataLoadingListener<TData>
{
	private boolean mTaskRunningOrPending;
	private CrimeServiceQueryContext mQueryContext;
	protected CrimeServiceQuery<TData> mQuery;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// nastav fragment jako retained, takze zustava jedina instance
		// pro aktivitu, dokud ta zije, jen se napr. meni orientace obrazovky
		this.setRetainInstance(true);
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		
		
	}
	
	public boolean isTaskRunningOrPending()
	{
		return mTaskRunningOrPending;
	}

	private void setIsTaskRunningFlag(boolean isRunning)
	{
		mTaskRunningOrPending = isRunning;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		// zde by se daly stopnout tasky
		// Asi neni garantovani, ze se zavola, ale co.
		this.cancelTask();
	}

	public void cancelTask()
	{
		if(this.isTaskRunningOrPending())
		{
			mQuery.cancel();
		}
	}

	protected abstract String getServiceMethodName();
	protected abstract QueryResultProjector<String, TData> getResultProjector();
	
	public void loadData(QueryParameters ... params) throws DataLoadingTaskException
	{
		if (this.isTaskRunningOrPending())
			return;

		try
		{
			mQuery = new CrimeServiceQuery<TData>(mQueryContext)
			{
				@Override
				protected String getMethodName()
				{
					return DataLoadingFragment.this.getServiceMethodName();
				}

				@Override
				protected QueryResultProjector<String, TData> createResultProjector()
				{
					return DataLoadingFragment.this.getResultProjector();
				}
			};
			
			this.onTaskCreated();
			
			mQuery.execute(this, params);
		}
		catch (TypedQueryInternalErrorException e)
		{
			e.printStackTrace();
			
			this.setIsTaskRunningFlag(false);
			
			throw new DataLoadingTaskException("Error occured when trying to execute the query", e);
		}
	}
	
	@Override
	public void onTaskCreated()
	{
		this.setIsTaskRunningFlag(true);
	}
	
	@Override
	public void onTaskStarted()
	{
	}
	
	@Override
	public void onDataLoaded(TData result, QueryParameters... forParams)
	{
		this.setIsTaskRunningFlag(false);
	}
	
	@Override
	public void onEmptyDataLoaded(QueryParameters... forParams)
	{
		this.setIsTaskRunningFlag(false);
	}
	
	@Override
	public void onDataLoadingCancelled(QueryParameters... forParams)
	{
		this.setIsTaskRunningFlag(false);
	}
	
	@Override
	public void onDataLoadingError(Exception e, QueryParameters... forParams)
	{
		this.setIsTaskRunningFlag(false);
	}
}
