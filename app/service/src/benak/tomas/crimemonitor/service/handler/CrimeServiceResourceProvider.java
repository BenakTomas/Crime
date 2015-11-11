package benak.tomas.crimemonitor.service.handler;

import java.nio.charset.Charset;

import benak.tomas.crimemonitor.library.service.CrimeMonitorServiceProvider;
import benak.tomas.crimemonitor.service.exception.CrimeResourceProviderInternalErrorException;
import benak.tomas.crimemonitor.service.exception.InvalidCrimeServiceResourcePathException;

public interface CrimeServiceResourceProvider
{
	String getResourceAsString(String path, Charset charset) throws InvalidCrimeServiceResourcePathException, CrimeResourceProviderInternalErrorException;
}
