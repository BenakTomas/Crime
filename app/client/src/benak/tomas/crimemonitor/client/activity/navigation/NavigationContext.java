package benak.tomas.crimemonitor.client.activity.navigation;

import java.util.Properties;

import android.content.Intent;
import benak.tomas.crimemonitor.R;
import benak.tomas.crimemonitor.client.exception.IllegalActivityIdException;
import benak.tomas.crimemonitor.library.exception.QueryConfigInternalErrorException;
import benak.tomas.crimemonitor.library.query.config.QueryConfigBase;
import benak.tomas.crimemonitor.library.service.CrimeMonitorServiceProvider;

public class NavigationContext extends QueryConfigBase
{
	private static final String CRIME_ACTION_SUMMARY = "benak.tomas.crimemonitor.client.action.summary";
	private static final String CRIME_ACTION_LIST = "benak.tomas.crimemonitor.client.action.list";
	private static final String CRIME_ACTION_DETAIL = "benak.tomas.crimemonitor.client.action.detail";
	private static final String CRIME_ACTION_UTVAR = "benak.tomas.crimemonitor.client.action.utvar";
	private static final String CRIME_CATEGORY = "android.intent.category.DEFAULT";

	public NavigationContext(Properties configuration)
			throws QueryConfigInternalErrorException
	{
		super(configuration);
	}

	@Override
	protected void loadConfiguration(Properties configuration)
			throws QueryConfigInternalErrorException
	{
	}

	public String getActivityActionString(final int activityId) throws IllegalActivityIdException
	{
		switch (activityId)
		{
		case R.id.crime_action_summary:
			return CRIME_ACTION_SUMMARY;
		case R.id.crime_action_list:
			return CRIME_ACTION_LIST;
		case R.id.crime_action_detail:
			return CRIME_ACTION_DETAIL;
		case R.id.crime_action_utvar_detail:
			return CRIME_ACTION_UTVAR;
		
		default:
			throw new IllegalActivityIdException();
		}
	}

	public String[] getActivityCategories(int activityId)
	{
		return new String[] { CRIME_CATEGORY };
	}

}
