package benak.tomas.crimemonitor.client.activity.navigation;

import android.content.Context;
import android.content.Intent;
import benak.tomas.crimemonitor.client.binding.binder.IntentBundleBinder;
import benak.tomas.crimemonitor.client.exception.IllegalActivityIdException;
import benak.tomas.crimemonitor.library.binding.binder.TypedParameterBinder;
import benak.tomas.crimemonitor.library.exception.TypedQueryInternalErrorException;
import benak.tomas.crimemonitor.library.query.TypedQuerySync;
import benak.tomas.crimemonitor.library.query.parameter.QueryParameters;
import benak.tomas.crimemonitor.library.query.resultProjector.QueryResultProjector;

public class NavigationQuery extends TypedQuerySync<NavigationContext, Void, Intent, Void>
{
	private final Context mContext;
	private final int mActivityId;
	private final String mActionString;
	private final String[] mCategories;
	
	public NavigationQuery(final int activityId, final Context context, NavigationContext config)
			throws TypedQueryInternalErrorException, IllegalActivityIdException
	{
		super(config);
		
		if(context == null)
			throw new IllegalArgumentException("'context' cannot be null");
		
		mContext = context;
		mActivityId = activityId;
		mActionString = mConfig.getActivityActionString(mActivityId);
		mCategories = mConfig.getActivityCategories(mActivityId);
	}

	@Override
	protected TypedParameterBinder<Intent> createParameterBinder()
	{
		return new IntentBundleBinder();
	}

	@Override
	protected QueryResultProjector<Void, Void> createResultProjector()
	{
		return new QueryResultProjector<Void, Void>()
		{
			@Override
			public Void projectResult(Void sourceResult)
			{
				return null;
			}
		};
	}

	@Override
	protected Intent createStatement()
	{
		Intent intent = new Intent(mActionString);
		
		for(String category : mCategories)
		{
			intent.addCategory(category);
		}
		
		return intent;
	}

	@Override
	protected Void executeStatement(Intent statement)
	{
		mContext.startActivity(statement);
		return null;
	}
	
	public static void navigate(int activityId, Context context, NavigationContext config, QueryParameters ...params) throws TypedQueryInternalErrorException, IllegalActivityIdException
	{
		new NavigationQuery(activityId, context, config).execute(params);
	}
}
