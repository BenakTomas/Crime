package benak.tomas.crimemonitor.client.dataloading.task.handler;

/**
 * AsyncTaskHandler is an interface used for a class that acts as an {@code AsyncTask} wrapper.
 * 
 * <p>
 * An implementing class acts as a notifier, listening for a certain subset of
 * a wrapped {@code AsyncTask} lifecycle events, performing additional logic and
 * reporting the events to the async task handler user.
 * 
 * <p>
 * The aforementioned subset of {@code AsyncTask} API, that is to be wrapped, is:
 * <ul>
 * <li>{@code AsyncTask#onPreExecute}
 * <li>{@code AsyncTask#onPostExecute}
 * <li>{@code AsyncTask#onCancelled(Object)}
 * 
 * <p>
 * The reasons for such added intermediate logic an implementing class represents may vary and
 * are implementation specific.
 * 
 * @author Tom
 *
 * @param <TResult> the data type that a wrapped {@code AsyncTask} returns
 */
public interface AsyncTaskHandler<TResult>
{
	/**
	 * Called to report the wrapped async task {@code AsyncTask#onPreExecute} event.
	 */
	void reportOnPreExecute();
	/**
	 * Called to report the wrapped async task {@code AsyncTask#onPostExecute} event.
	 * 
	 * @param result the reported result of the wrapped async task
	 */
	void reportResult(TResult result);
	void reportEmptyResult();
	void reportError(Exception exception);
	/**
	 * Called to report the wrapped async task {@code AsyncTask#onCancelled} event.
	 * 
	 * @param result the reported result of the wrapped async task in the case the async task has been cancelled
	 */
	void reportOnCancelled(TResult result);
}

 