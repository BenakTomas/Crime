package benak.tomas.crimemonitor.client.dataloading.params;

public class KodUtvaruParams extends
		benak.tomas.crimemonitor.library.query.parameter.KodUtvaruParams
{
	private static final String KOD_UTVARU_PARAMETER_NAME = "mKodUtvaru";
	private final String mKodUtvaru;

	public KodUtvaruParams(final String kodUtvaru)
	{
		super(KOD_UTVARU_PARAMETER_NAME);

		if (kodUtvaru == null)
			throw new IllegalArgumentException("'kodUtvaru' cannot be null");

		mKodUtvaru = kodUtvaru;
	}

	@Override
	protected String getKodUtvaru()
	{
		return mKodUtvaru;
	}
	
	public static KodUtvaruParams getInstance(final String kodUtvaru)
	{
		return new KodUtvaruParams(kodUtvaru);
	}
}
