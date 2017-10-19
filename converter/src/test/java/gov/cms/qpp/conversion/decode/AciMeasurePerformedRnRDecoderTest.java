package gov.cms.qpp.conversion.decode;

import gov.cms.qpp.TestHelper;
import gov.cms.qpp.conversion.Context;
import gov.cms.qpp.conversion.model.Node;
import gov.cms.qpp.conversion.model.TemplateId;
import gov.cms.qpp.conversion.xml.XmlException;
import gov.cms.qpp.conversion.xml.XmlUtils;
import java.io.IOException;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class AciMeasurePerformedRnRDecoderTest {
	private static final String MEASURE_ID = "ACI_INFBLO_1";
	private Node aciMeasurePerformedNode;

	@Before
	public void setUp() throws IOException, XmlException {
		String needsFormattingXml = TestHelper.getFixture("AciMeasurePerformedIsolated.xml");
		String xml = String.format(needsFormattingXml, MEASURE_ID);
		Node wrapperNode = new QppXmlDecoder(new Context()).decode(XmlUtils.stringToDom(xml));
		aciMeasurePerformedNode = wrapperNode.getChildNodes().get(0);
	}


	@Test
	public void testMeasureIsDecoded() throws XmlException, IOException {
		String actualMeasureId = aciMeasurePerformedNode.getValue("measureId");

		assertThat(actualMeasureId).isEqualTo(MEASURE_ID);
	}

	@Test
	public void testSectionOnlyDecodesOneMeasurePerformed() throws XmlException, IOException {
		long measurePerformedCount = aciMeasurePerformedNode.getChildNodes(
				node -> node.getType() == TemplateId.MEASURE_PERFORMED).count();
		
		assertThat(measurePerformedCount).isEqualTo(1L);
	}
}