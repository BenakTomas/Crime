package benak.tomas.crimemonitor.library.query.resultProjector;

public interface QueryResultProjector<TSourceResult, TResult>
{
	TResult projectResult(TSourceResult sourceResult); 
}
