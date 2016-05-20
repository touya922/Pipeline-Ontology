package semCal;

import java.util.ArrayList;
import java.util.List;


import com.hp.hpl.jena.ontology.*;

public class DescriSemi {
	// public static Double dDescriSemi = 0.0;
	public static Double dDescriSemi = 0.0;
	public static String concept1, concept2;// 待比较的两个概念
	public static OntClass ontClass1, ontClass2;// 两个概念对应本体库中的类
	public static OntOperation ontOP;

	private static String preUrl = "http://www.semanticweb.org/tyt/ontologies/RQ#";

	public DescriSemi() {

	}

	public static Double DescriSemiCal(ZHPoint pZhPoint, ZYPoint pZyPoint) {
		
		dDescriSemi = Math.random();

		List<List<OntProperty>> lstZHResultProps = new ArrayList<List<OntProperty>>();// 综合管点概念属性集
		List<List<OntProperty>> lstZYResultProps = new ArrayList<List<OntProperty>>();// 专业管点概念属性集

		Individual indZH;// 综合管点实例化对象
		Individual indZY;// 专业管点实例化对象

		ontOP = new OntOperation();

		concept1 = pZhPoint.PointName;
		concept2 = pZyPoint.PointName;

		ontClass1 = ontOP.getClassByName(preUrl + concept1);
		ontClass2 = ontOP.getClassByName(preUrl + concept2);

		// 获取综合和专业管点概念的属性集list[0]=objProperties,list[1]=datatypeProperties
		lstZHResultProps = getClassPropety(ontClass1, "综合管点" + concept1 + pZhPoint.ObjectID);
		lstZYResultProps = getClassPropety(ontClass2, "专业管点" + concept2 + pZyPoint.ObjectID);

		// 根据管点属性信息实例化对应的本体概念
		// indZH = ontOP.createIndividual(ontClass1, lstZHResultProps,
		// pZhPoint);
		// indZY = ontOP.createIndividual(ontClass2, lstZYResultProps,
		// pZyPoint);

		return dDescriSemi;

	}

	public static List<List<OntProperty>> getClassPropety(OntClass ontcls, String concept) {

		List<OntProperty> lstProps = new ArrayList<OntProperty>();// 类的属性列表
		List<OntProperty> lstObjProps = new ArrayList<OntProperty>();// 类的对象型属性列表
		List<OntProperty> lstDataProps = new ArrayList<OntProperty>();// 类的数值型属性列表
		List<List<OntProperty>> lstResultProps = new ArrayList<List<OntProperty>>();// 类的数值型属性列表

		String strObjPros = "";// 记录对象型属性
		String strDataPros = "";// 记录数值型属性

		try {
			// 获取类的所有属性
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

			// System.out.println(concept + " 的对象型属性有：" + strObjPros);

			// System.out.println(concept + " 的数值型属性有：" + strDataPros);

		} catch (Exception e) {
			System.out.println(e);
		}
		lstResultProps.add(lstObjProps);
		lstResultProps.add(lstDataProps);

		return lstResultProps;

	}

}
