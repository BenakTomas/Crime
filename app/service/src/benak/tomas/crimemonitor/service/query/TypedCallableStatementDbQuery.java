package benak.tomas.crimemonitor.service.query;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.HashMap;

import benak.tomas.crimemonitor.library.binding.binder.TypedParameterBinder;
import benak.tomas.crimemonitor.library.exception.BindingException;
import benak.tomas.crimemonitor.library.exception.TypedQueryInternalErrorException;
import benak.tomas.crimemonitor.library.query.TypedQuerySync;
import benak.tomas.crimemonitor.library.query.parameter.QueryParameters;
import benak.tomas.crimemonitor.service.query.config.DbConfig;

public abstract class TypedCallableStatementDbQuery<TResult>
		extends
		TypedQueryBase<DbConfig, TResult, CallableStatement, CallableStatement>
{
	private Connection				 mConnection;
	protected HashMap<String, Integer> mParameterNamesToOrdinals = new HashMap<String, Integer>();

	public TypedCallableStatementDbQuery(DbConfig config) throws TypedQueryInternalErrorException
	{
		super(config);
	}

	@Override
	protected void init() throws TypedQueryInternalErrorException
	{
		super.init();

		try
		{
			mConnection = DriverManager.getConnection(mConfig.getJdbcUrl(),
					mConfig.getConnectionProperties());
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new TypedQueryInternalErrorException("Cannot create a connection to the source database.", e);
		}
	}

	@Override
	protected TypedParameterBinder<CallableStatement> createParameterBinder()
	{
		return new TypedParameterBinder<CallableStatement>()
		{

			@Override
			public void bind(String parameterName, Calendar parameterValue)
					throws BindingException
			{
				this.tryBind(parameterName, new java.sql.Date(parameterValue.getTimeInMillis()));
			}

			@Override
			public void bind(String parameterName, String parameterValue)
					throws BindingException
			{
				this.tryBind(parameterName, parameterValue);
			}

			@Override
			public void bind(String parameterName, float parameterValue)
					throws BindingException
			{
				this.tryBind(parameterName, parameterValue);
			}

			@Override
			public void bind(String parameterName, int parameterValue)
					throws BindingException
			{
				this.tryBind(parameterName, parameterValue);
			}
			
			private void tryBind(String parameterName, Object parameterValue) throws BindingException
			{
				try
				{
					mBindingObject
							.setObject(
									TypedCallableStatementDbQuery.this
											.getParameterIndex(parameterName),
									parameterValue);
				}
				catch (SQLException e)
				{
					e.printStackTrace();
					throw new BindingException("An error occurred when trying to bind the parameter value to a statement.", e,
							parameterName, parameterValue);
				}
			}
		};
	}

	@Override
	protected CallableStatement createStatement()
	{
		CallableStatement statement = null;
		try
		{
			statement = mConnection.prepareCall(mQueryText);
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try
		{
			statement.registerOutParameter(4, Types.CHAR);
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return statement;
	}

	@Override
	protected CallableStatement executeStatement(CallableStatement statement)
	{
		try
		{
			statement.execute();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return statement;
	}

	@Override
	protected void bindParametersToQueryString(CallableStatement statement,
			QueryParameters... params) throws BindingException
	{
		super.bindParametersToQueryString(statement, params);

		// bind out parameters
		this.registerOutParameters(statement);
	}

	protected abstract void registerOutParameters(CallableStatement statement)
			throws BindingException;

	protected void registerOutParameter(String name, int sqlType,
			CallableStatement statement) throws BindingException
	{
		try
		{
			statement.registerOutParameter(this.getParameterIndex(name),
					sqlType);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new BindingException("Cannot register out parameter", e,
					name, null);
		}
	}

	public String getStringOutParameterValue(String name,
			CallableStatement statement)
	{
		try
		{
			return statement.getString(this.getParameterIndex(name));
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private int getParameterIndex(final String name)
	{
		return mParameterNamesToOrdinals.get(name);
	}
}
