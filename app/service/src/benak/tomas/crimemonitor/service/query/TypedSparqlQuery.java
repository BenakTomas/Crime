package benak.tomas.crimemonitor.service.query;

import benak.tomas.crimemonitor.library.binding.binder.TypedParameterBinder;
import benak.tomas.crimemonitor.library.exception.TypedQueryInternalErrorException;
import benak.tomas.crimemonitor.library.query.TypedQuerySync;
import benak.tomas.crimemonitor.library.query.resultProjector.QueryResultProjector;
import benak.tomas.crimemonitor.service.query.config.SparqlConfig;
import benak.tomas.crimemonitor.service.query.parameter.binder.ParametrizedSparqlQueryParameterBinder;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.DatasetFactory;
import com.hp.hpl.jena.query.ParameterizedSparqlString;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.shared.impl.PrefixMappingImpl;

public abstract class TypedSparqlQuery<TResult>
		extends
		TypedQueryBase<SparqlConfig, TResult, ParameterizedSparqlString, ResultSet> {

	public TypedSparqlQuery(SparqlConfig queryConfig) throws TypedQueryInternalErrorException
	{
		super(queryConfig);
	}

	protected PrefixMapping getPrefixMappings() {
		return new PrefixMappingImpl()
				.setNsPrefix("dct", "http://purl.org/dc/terms/")
				.setNsPrefix("void", "http://rdfs.org/ns/void#")
				.setNsPrefix("skos", "http://www.w3.org/2004/02/skos/core#")
				.setNsPrefix("xsd", "http://www.w3.org/2001/XMLSchema#")
				.setNsPrefix("rdfs", "http://www.w3.org/2000/01/rdf-schema#")
				.setNsPrefix("crime",
						"http://linked.opendata.cz/ontology/crime#");
	}

	@Override
	protected TypedParameterBinder<ParameterizedSparqlString> createParameterBinder() {
		return new ParametrizedSparqlQueryParameterBinder();
	}

	@Override
	protected abstract QueryResultProjector<ResultSet, TResult> createResultProjector();

	@Override
	protected ParameterizedSparqlString createStatement() {
		return new ParameterizedSparqlString(mQueryText,
				this.getPrefixMappings());
	}

	@Override
	protected ResultSet executeStatement(ParameterizedSparqlString statement)
	{
		Dataset queryDataset = DatasetFactory.create(mConfig.getDefaultGraphUri());

		QueryExecution queryExecution = QueryExecutionFactory.create(
				statement.asQuery(), queryDataset);
		
		return queryExecution.execSelect();
	}
}
