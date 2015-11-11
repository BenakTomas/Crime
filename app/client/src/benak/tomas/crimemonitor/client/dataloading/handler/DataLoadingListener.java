package benak.tomas.crimemonitor.client.dataloading.handler;

import benak.tomas.crimemonitor.library.query.parameter.QueryParameters;

public interface DataLoadingListener<TResult>
{
	void onTaskCreated();
	void onTaskStarted();
	void onDataLoaded(TResult result, QueryParameters ...forParams);
	void onEmptyDataLoaded(QueryParameters ... forParams);
	void onDataLoadingCancelled(QueryParameters ... forParams);
	void onDataLoadingError(Exception e, QueryParameters ... forParams);
}
