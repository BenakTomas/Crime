package benak.tomas.crimemonitor.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import benak.tomas.crimemonitor.service.exception.CrimeServiceConfigurationErrorException;
import benak.tomas.crimemonitor.service.exception.CrimeServiceInternalErrorException;
import benak.tomas.crimemonitor.service.exception.TypedSparqlQueryException;

@WebService(name = "CrimeServiceJaxWS", targetNamespace = "http://service.crimemonitor.tomas.benak")
@SOAPBinding(style = Style.RPC)
public interface CrimeService
{
	@WebMethod String getCrimeSummary(
			@WebParam(partName = "kodUtvaru") String kodUtvaru,
			@WebParam(partName = "startYear") int startYear,
			@WebParam(partName = "startMonth") int startMonth,
			@WebParam(partName = "endYear") int endYear,
			@WebParam(partName = "endMonth") int endMonth)
			throws CrimeServiceConfigurationErrorException,
			CrimeServiceInternalErrorException;
	@WebMethod String getCrimeDetail(
			@WebParam(partName = "tsk") int tsk,
			@WebParam(partName = "kodUtvaru") String kodUtvaru,
			@WebParam(partName = "startYear") int startYear,
			@WebParam(partName = "startMonth") int startMonth,
			@WebParam(partName = "endYear") int endYear,
			@WebParam(partName = "endMonth") int endMonth)
			throws CrimeServiceInternalErrorException,
			CrimeServiceConfigurationErrorException;
	@WebMethod String getCrimeList(
			@WebParam(partName = "kodUtvaru") String kodUtvaru,
			@WebParam(partName = "startYear") int startYear,
			@WebParam(partName = "startMonth") int startMonth,
			@WebParam(partName = "endYear") int endYear,
			@WebParam(partName = "endMonth") int endMonth)
			throws CrimeServiceInternalErrorException,
			CrimeServiceConfigurationErrorException;
	@WebMethod String getUtvarDetail(
			@WebParam(partName = "kodUtvaru") String kodUtvaru,
			@WebParam(partName = "startYear") int startYear,
			@WebParam(partName = "startMonth") int startMonth,
			@WebParam(partName = "endYear") int endYear,
			@WebParam(partName = "endMonth") int endMonth)
			throws CrimeServiceInternalErrorException,
			CrimeServiceConfigurationErrorException;
	@WebMethod String getKodANazevUtvaruForPosition(
			@WebParam(partName = "longitude") float longitude,
			@WebParam(partName = "latitude") float latitude)
			throws CrimeServiceInternalErrorException,
			CrimeServiceConfigurationErrorException;
}
