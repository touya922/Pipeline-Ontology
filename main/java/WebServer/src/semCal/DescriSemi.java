package semCal;

import java.util.ArrayList;
import java.util.List;


import com.hp.hpl.jena.ontology.*;

public class DescriSemi {
	// public static Double dDescriSemi = 0.0;
	public static Double dDescriSemi = 0.0;
	public static String concept1, concept2;// ���Ƚϵ���������
	public static OntClass ontClass1, ontClass2;// ���������Ӧ������е���
	public static OntOperation ontOP;

	private static String preUrl = "http://www.semanticweb.org/tyt/ontologies/RQ#";

	public DescriSemi() {

	}

	public static Double DescriSemiCal(ZHPoint pZhPoint, ZYPoint pZyPoint) {
		
		dDescriSemi = Math.random();

		List<List<OntProperty>> lstZHResultProps = new ArrayList<List<OntProperty>>();// �ۺϹܵ�������Լ�
		List<List<OntProperty>> lstZYResultProps = new ArrayList<List<OntProperty>>();// רҵ�ܵ�������Լ�

		Individual indZH;// �ۺϹܵ�ʵ��������
		Individual indZY;// רҵ�ܵ�ʵ��������

		ontOP = new OntOperation();

		concept1 = pZhPoint.PointName;
		concept2 = pZyPoint.PointName;

		ontClass1 = ontOP.getClassByName(preUrl + concept1);
		ontClass2 = ontOP.getClassByName(preUrl + concept2);

		// ��ȡ�ۺϺ�רҵ�ܵ��������Լ�list[0]=objProperties,list[1]=datatypeProperties
		lstZHResultProps = getClassPropety(ontClass1, "�ۺϹܵ�" + concept1 + pZhPoint.ObjectID);
		lstZYResultProps = getClassPropety(ontClass2, "רҵ�ܵ�" + concept2 + pZyPoint.ObjectID);

		// ���ݹܵ�������Ϣʵ������Ӧ�ı������
		// indZH = ontOP.createIndividual(ontClass1, lstZHResultProps,
		// pZhPoint);
		// indZY = ontOP.createIndividual(ontClass2, lstZYResultProps,
		// pZyPoint);

		return dDescriSemi;

	}

	public static List<List<OntProperty>> getClassPropety(OntClass ontcls, String concept) {

		List<OntProperty> lstProps = new ArrayList<OntProperty>();// ��������б�
		List<OntProperty> lstObjProps = new ArrayList<OntProperty>();// ��Ķ����������б�
		List<OntProperty> lstDataProps = new ArrayList<OntProperty>();// �����ֵ�������б�
		List<List<OntProperty>> lstResultProps = new ArrayList<List<OntProperty>>();// �����ֵ�������б�

		String strObjPros = "";// ��¼����������
		String strDataPros = "";// ��¼��ֵ������

		try {
			// ��ȡ�����������
			lstProps = ontOP.getPropertiesByClass(ontcls);

			for (int i = 0; i < lstProps.size(); i++) {
				OntProperty ontPros = lstProps.get(i);

				switch (ontPros.getRDFType().getLocalName()) {
				case "ObjectProperty":
					lstObjProps.add(ontPros);
					strObjPros += ontPros.getLocalName() + " ";
				case "DatatypeProperty":
					lstDataProps.add(ontPros);
					strDataPros += ontPros.getLocalName() + " ";
					break;
				default:
					break;
				}

			}

			// System.out.println(concept + " �Ķ����������У�" + strObjPros);

			// System.out.println(concept + " ����ֵ�������У�" + strDataPros);

		} catch (Exception e) {
			System.out.println(e);
		}
		lstResultProps.add(lstObjProps);
		lstResultProps.add(lstDataProps);

		return lstResultProps;

	}

}
