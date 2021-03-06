package gov.cms.qpp.conversion.encode;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gov.cms.qpp.conversion.Context;
import gov.cms.qpp.conversion.model.Node;
import gov.cms.qpp.conversion.model.TemplateId;
import gov.cms.qpp.conversion.model.validation.SubPopulationLabel;

class QualityMeasureIdEncoderTest {

	private Node qualityMeasureId;
	private Node populationNode;
	private Node denomExclusionNode;
	private Node denominatorExceptionNode;
	private Node numeratorNode;
	private Node denominatorNode;
	private Node aggregateCountNode;
	private JsonWrapper wrapper;
	private QualityMeasureIdEncoder encoder;
	private String type = "type";
	private static final String ELIGIBLE_POPULATION = "eligiblePopulation";

	@BeforeEach
	void setUp() {
		qualityMeasureId = new Node(TemplateId.MEASURE_REFERENCE_RESULTS_CMS_V2);
		qualityMeasureId.putValue("measureId", "40280382-5abd-fa46-015b-49abb28d38b2");

		aggregateCountNode = new Node(TemplateId.PI_AGGREGATE_COUNT);
		aggregateCountNode.putValue("aggregateCount", "600");

		Node paymentNode = new Node(TemplateId.PAYER_SUPPLEMENTAL_DATA_ELEMENT_CMS_V2);
		paymentNode.putValue("place", "holder");

		populationNode = new Node(TemplateId.MEASURE_DATA_CMS_V2);
		populationNode.putValue(type, SubPopulationLabel.IPOP.name());
		populationNode.addChildNode(aggregateCountNode);

		denomExclusionNode = new Node(TemplateId.MEASURE_DATA_CMS_V2);
		denomExclusionNode.putValue(type, SubPopulationLabel.DENEX.name());
		denomExclusionNode.addChildNode(aggregateCountNode);

		denominatorExceptionNode = new Node(TemplateId.MEASURE_DATA_CMS_V2);
		denominatorExceptionNode.putValue(type, SubPopulationLabel.DENEXCEP.name());
		denominatorExceptionNode.addChildNode(aggregateCountNode);

		numeratorNode = new Node(TemplateId.MEASURE_DATA_CMS_V2);
		numeratorNode.putValue(type, SubPopulationLabel.NUMER.name());
		numeratorNode.addChildNode(aggregateCountNode);
		numeratorNode.addChildNode(paymentNode);

		denominatorNode = new Node(TemplateId.MEASURE_DATA_CMS_V2);
		denominatorNode.putValue(type, SubPopulationLabel.DENOM.name());
		denominatorNode.addChildNode(paymentNode);
		denominatorNode.addChildNode(aggregateCountNode);

		encoder = new QualityMeasureIdEncoder(new Context());
		wrapper = new JsonWrapper();
	}

	@Test
	void testMeasureIdIsEncoded() {
		executeInternalEncode();

		assertThat(wrapper.getString("measureId"))
				.isEqualTo("236");
	}

	@Test
	void testEndToEndReportedIsEncoded() {
		executeInternalEncode();
		JsonWrapper childValues = getChildValues();

		assertThat(childValues.getBoolean("isEndToEndReported"))
				.isTrue();
	}

	@Test
	void testPopulationTotalIsEncoded() {
		executeInternalEncode();
		JsonWrapper childValues = getChildValues();


		assertThat(childValues.getInteger(ELIGIBLE_POPULATION))
				.isEqualTo(600);
	}

	@Test
	void testPopulationAltTotalIsEncoded() {
		populationNode = new Node(TemplateId.MEASURE_DATA_CMS_V2);
		populationNode.putValue(type, "IPP");
		populationNode.addChildNode(aggregateCountNode);
		executeInternalEncode();
		JsonWrapper childValues = getChildValues();

		assertThat(childValues.getInteger(ELIGIBLE_POPULATION))
				.isEqualTo(600);
	}

	@Test
	void testPerformanceMetIsEncoded() {
		executeInternalEncode();
		JsonWrapper childValues = getChildValues();
		assertThat(childValues.getInteger("performanceMet"))
				.isEqualTo(600);
	}

	@Test
	void testPerformanceExclusionIsEncoded() {
		executeInternalEncode();
		JsonWrapper childValues = getChildValues();

		assertThat(childValues.getInteger("eligiblePopulationExclusion"))
				.isEqualTo(600);
	}

	@Test
	void testPerformanceNotMetIsEncoded() {
		executeInternalEncode();
		JsonWrapper childValues = getChildValues();

		assertThat(childValues.getInteger("performanceNotMet"))
				.isEqualTo(-1200);
	}

	@Test
	void testMeasure438EncodingEndToEndEncoded() {
		qualityMeasureId.putValue("measureId", "40280382-5b4d-eebc-015b-8245e0fa06b7");
		executeInternalEncode();
		JsonWrapper childValues = getChildValues();

		assertThat(childValues.getBoolean("isEndToEndReported"))
			.isTrue();
	}

	@Test
	void testMeasure438EncodingEligiblePopulation() {
		qualityMeasureId.putValue("measureId", "40280382-5b4d-eebc-015b-8245e0fa06b7");
		executeInternalEncode();
		JsonWrapper childValues = getChildValues();

		assertThat(childValues.getInteger(ELIGIBLE_POPULATION))
			.isEqualTo(600);
	}

	@Test
	void testMeasure438EncodingPerformanceMet() {
		qualityMeasureId.putValue("measureId", "40280382-5b4d-eebc-015b-8245e0fa06b7");
		executeInternalEncode();
		JsonWrapper childValues = getChildValues();

		assertThat(childValues.getInteger("performanceMet"))
			.isEqualTo(600);
	}

	@Test
	void testMeasure438EncodingEligiblePopulationExclusion() {
		qualityMeasureId.putValue("measureId", "40280382-5b4d-eebc-015b-8245e0fa06b7");
		executeInternalEncode();
		JsonWrapper childValues = getChildValues();

		assertThat(childValues.getInteger("eligiblePopulationExclusion"))
			.isEqualTo(600);
	}

	@Test
	void testMeasure438EncodingEligiblePopulationException() {
		qualityMeasureId.putValue("measureId", "40280382-5b4d-eebc-015b-8245e0fa06b7");
		executeInternalEncode();
		JsonWrapper childValues = getChildValues();

		assertThat(childValues.getInteger("eligiblePopulationException"))
			.isEqualTo(600);
	}

	@Test
	void testMeasure438EncodingPerformanceNotMet() {
		qualityMeasureId.putValue("measureId", "40280382-5b4d-eebc-015b-8245e0fa06b7");
		executeInternalEncode();
		JsonWrapper childValues = getChildValues();

		assertThat(childValues.getInteger("performanceNotMet"))
			.isEqualTo(-1200);
	}

	@Test
	void testIgnoresNonMeasureDataNodes() {
		qualityMeasureId.addChildNode(aggregateCountNode);
		executeInternalEncode();
		JsonWrapper childValues = getChildValues();

		assertThat(childValues.getInteger("aggregateCount")).isNull();
	}

	private void executeInternalEncode() {
		qualityMeasureId.addChildNodes(populationNode, denomExclusionNode, numeratorNode, denominatorNode,
			denominatorExceptionNode);
		try {
			encoder.internalEncode(wrapper, qualityMeasureId);
		} catch (EncodeException e) {
			Assertions.fail("Failure to encode: " + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	private JsonWrapper getChildValues() {
		return wrapper.get("value");
	}
}