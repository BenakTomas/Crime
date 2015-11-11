package benak.tomas.crimemonitor.service.dto;

public final class IndexAObjasnenost
{
	private final float mIndex;
	private final float mObjasnenost;

	public IndexAObjasnenost(float index, float objasnenost)
	{
		mIndex = index;
		mObjasnenost = objasnenost;
	}

	public float getIndex()
	{
		return mIndex;
	}

	public float getObjasnenost()
	{
		return mObjasnenost;
	}
}
