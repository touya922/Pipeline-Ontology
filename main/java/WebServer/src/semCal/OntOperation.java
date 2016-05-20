package semCal;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.db.DBConnection;
import com.hp.hpl.jena.db.IDBConnection;
import com.hp.hpl.jena.ontology.*;

public class OntOperation {

	// ���ݿ����Ӳ�������
	public static final String strDriver = "com.mysql.jdbc.Driver"; // path_of_driver_class
	public static final String strURL = "jdbc:mysql://localhost/rqOntDB"; // URL_of_database
	public static final String strUser = "pipeOntUser"; // database user id
	public static final String strPassWord = "orcl"; // database password
	public static final String strDB = "MySQL"; // database type

	/*
	 * ��������˵��
	 * 
	 * ��������ģ�ͣ�Ȼ���ȡ.owl�ļ�������ģ��
	 * 
	 */
	public OntModel createOnt(String owlpath) {

		// ����һ�����ݿ�����
		IDBConnection conn = new DBConnection(strURL, strUser, strPassWord, strDB);
		// �������ݿ������࣬��Ҫ�����쳣
		try {
			Class.forName(strDriver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			// ʹ�����ݿ����Ӳ�������һ��ģ��������
			ModelMaker maker = ModelFactory.createModelRDBMaker(conn);
			// ����һ��Ĭ��ģ��
			Model base = maker.createDefaultModel();
			OntModel ontmodel = ModelFactory.createOntologyModel(getModelSpec(maker), base);
			// ��ȡ�ļ�������ģ��
			// ontmodel.read(new FileInputStream(owlpath), "");

			return ontmodel;
		} catch (Exception e) {
			System.out.println("��ȡʧ��" + e.getMessage());

			return null;
		}

	}

	/*
	 * ��������˵��
	 * 
	 * ��ȡ���ݿ��б���ģ��
	 * 
	 */
	public OntModel getOntModel() {

		// ����һ�����ݿ�����
		IDBConnection conn = new DBConnection(strURL, strUser, strPassWord, strDB);

		try {
			// �������ݿ������࣬��Ҫ�����쳣
			Class.forName(strDriver);

			// ʹ�����ݿ����Ӳ�������һ��ģ��������
			ModelMaker maker = ModelFactory.createModelRDBMaker(conn);
			// ����һ��Ĭ��ģ��
			Model base = maker.createDefaultModel();
			OntModel ontmodel = ModelFactory.createOntologyModel(getModelSpec(maker), base);

			return ontmodel;

		} catch (ClassNotFoundException e) {
			e.printStackTrace();

			return null;
		} catch (Exception e) {
			System.out.println("��ȡʧ��" + e.getMessage());

			return null;
		}

	}

	/*
	 * ��������˵��
	 * 
	 * ��������ȡOntModelSpec
	 * 
	 */
	public static OntModelSpec getModelSpec(ModelMaker maker) {

		OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM);

		spec.setImportModelMaker(maker);

		return spec;
	}

	/*
	 * ��������˵��
	 * 
	 * ͨ����������ȡClass
	 * 
	 */
	public OntClass getClassByName(String owlpath, String conceptName) {

		OntModel ontModel = getOntModel();

		OntClass ontCls;

		// OntModel ontModel = this.createOnt(owlpath);
		String str = "";
		ontCls = ontModel.getOntClass(conceptName);

		// ��ȡ��class����������
		for (Iterator<OntProperty> allPros = ontCls.listDeclaredProperties(); allPros.hasNext();) {

			OntProperty ontPros = (OntProperty) allPros.next();

			// str+=superCls.toString()+"/r/n";
			str += ontPros.getLocalName() + "\r\n--������" + ontPros.getLabel("") + "\r\n--�������ͣ�"
					+ ontPros.getRDFType().getLocalName() + "\r\n";

			// ��ȡ�����ԵĶ�����
			String strDomain = "--������";
			for (Iterator<? extends OntResource> allDomains = ontPros.listDomain(); allDomains.hasNext();) {
				OntResource domain = (OntResource) allDomains.next();
				strDomain += domain.getLocalName() + "��";
			}
			strDomain = strDomain.substring(0, strDomain.length() - 1);
			strDomain += "\r\n";

			// ��ȡ�����Ե�ֵ��
			String strRange = "--ֵ��";
			for (Iterator<? extends OntResource> allRanges = ontPros.listRange(); allRanges.hasNext();) {
				OntResource range = (OntResource) allRanges.next();
				strRange += range.getLocalName() + ",";
			}
			strRange = strRange.substring(0, strRange.length() - 1);
			strRange += "\r\n";

			str += strDomain + strRange;

		}

		System.out.println(ontCls.toString() + "�������У�\r\n" + str);

		str = "";
		// ��ȡ��class�����еȼ���
		for (Iterator<OntClass> ontEqviClses = ontCls.listEquivalentClasses(); ontEqviClses.hasNext();) {
			OntClass ontEqviCls = (OntClass) ontEqviClses.next();

			str += ontEqviCls.getLocalName() + "  ������" + ontEqviCls.getLabel("") + "\r\n";

		}
		System.out.println(ontCls.toString() + "�ĵȼ����У�\r\n" + str);

		str = "";
		// ��ȡ��class�����зǵȼ���
		for (Iterator<OntClass> ontunEqviClses = ontCls.listDisjointWith(); ontunEqviClses.hasNext();) {
			OntClass ontunEqviCls = (OntClass) ontunEqviClses.next();

			str += ontunEqviCls.getLocalName() + "  ������" + ontunEqviCls.getLabel("") + "\r\n";

		}
		System.out.println(ontCls.toString() + "�ķǵȼ����У�\r\n" + str);

		return ontCls;
	}

	/*
	 * ��������˵��
	 * 
	 * ͨ����������ȡClass
	 * 
	 */
	public OntClass getClassByName(String conceptName) {
		OntClass ontCLS = null;
		OntModel ontModel = null;

		try {
			// ��ȡ����ģ��
			ontModel = getOntModel();
			// ͨ���������ƴӱ���ģ���л�ȡ������
			ontCLS = ontModel.getOntClass(conceptName);

		} catch (Exception e) {
			System.out.println(e);

		}

 		return ontCLS;
	}

	/*
	 * ��������˵��
	 * 
	 * ͨ����������ȡClass
	 * 
	 */
	public List<OntProperty> getPropertiesByClass(OntClass ontCLS) {

		List<OntProperty> lstOntProps = new ArrayList<OntProperty>();

		try {
			// ��ȡ��class����������
			for (Iterator<OntProperty> allPros = ontCLS.listDeclaredProperties(); allPros.hasNext();) {

				OntProperty ontPro = (OntProperty) allPros.next();
				lstOntProps.add(ontPro);
			}

		} catch (Exception e) {
			System.out.println(e);
		}

		return lstOntProps;
	}

	/*
	 * ��������˵��
	 * 
	 * ��ȡĳһ���ԵĶ�����
	 * 
	 */
	public List<OntResource> getPropertyDomains(OntProperty ontPros) {

		List<OntResource> lstOntResources = new ArrayList<OntResource>();

		try {
			// ��ȡ�����ԵĶ�����
			String strDomain = "--������";
			for (Iterator<? extends OntResource> allDomains = ontPros.listDomain(); allDomains.hasNext();) {
				OntResource domain = (OntResource) allDomains.next();
				lstOntResources.add(domain);
				
				strDomain += domain.getLocalName() + "��";
			}
			strDomain = strDomain.substring(0, strDomain.length() - 1);
			strDomain += "\r\n";

		} catch (Exception e) {
			System.out.println(e);
		}

		return lstOntResources;
	}

	/*
	 * ��������˵��
	 * 
	 * ��ȡĳһ���Ե�ֵ��
	 * 
	 */
	public List<OntResource> getPropertyRange(OntProperty ontPros) {

		List<OntResource> lstOntResources = new ArrayList<OntResource>();

		try {
			// ��ȡ�����Ե�ֵ��
			String strRange = "--ֵ��";
			for (Iterator<? extends OntResource> allRanges = ontPros.listRange(); allRanges.hasNext();) {
				OntResource range = (OntResource) allRanges.next();
				lstOntResources.add(range);
				
				strRange += range.getLocalName() + ",";
			}
			strRange = strRange.substring(0, strRange.length() - 1);
			strRange += "\r\n";

		} catch (Exception e) {
			System.out.println(e);
		}

		return lstOntResources;
	}

	
	
	public Individual createIndividual(OntClass ontCLS,List<List<OntProperty>> lstOntProps,Object obj){
		Individual ind=null;
		
		try {
			ind=ontCLS.createIndividual();
			
			//ind.set
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return ind;
	}
	
	/*
	 * ��������˵��
	 * 
	 * ͨ����������ȡClass
	 * 
	 */
	public OntClass getClassByName1(String conceptName) {

		OntModel ontModel = getOntModel();

		OntClass ontCls;
		String str = "";
		ontCls = ontModel.getOntClass(conceptName);

		// ��ȡ��class����������
		for (Iterator<OntProperty> allPros = ontCls.listDeclaredProperties(); allPros.hasNext();) {

			OntProperty ontPros = (OntProperty) allPros.next();

			// str+=superCls.toString()+"/r/n";
			str += ontPros.getLocalName() + "\r\n--������" + ontPros.getLabel("") + "\r\n--�������ͣ�"
					+ ontPros.getRDFType().getLocalName() + "\r\n";

			// ��ȡ�����ԵĶ�����
			String strDomain = "--������";
			for (Iterator<? extends OntResource> allDomains = ontPros.listDomain(); allDomains.hasNext();) {
				OntResource domain = (OntResource) allDomains.next();
				strDomain += domain.getLocalName() + "��";
			}
			strDomain = strDomain.substring(0, strDomain.length() - 1);
			strDomain += "\r\n";

			// ��ȡ�����Ե�ֵ��
			String strRange = "--ֵ��";
			for (Iterator<? extends OntResource> allRanges = ontPros.listRange(); allRanges.hasNext();) {
				OntResource range = (OntResource) allRanges.next();
				strRange += range.getLocalName() + ",";
			}
			strRange = strRange.substring(0, strRange.length() - 1);
			strRange += "\r\n";

			str += strDomain + strRange;

		}

		System.out.println(ontCls.toString() + "�������У�\r\n" + str);

		str = "";
		// ��ȡ��class�����еȼ���
		for (Iterator<OntClass> ontEqviClses = ontCls.listEquivalentClasses(); ontEqviClses.hasNext();) {
			OntClass ontEqviCls = (OntClass) ontEqviClses.next();

			str += ontEqviCls.getLocalName() + "  ������" + ontEqviCls.getLabel("") + "\r\n";

		}
		System.out.println(ontCls.toString() + "�ĵȼ����У�\r\n" + str);

		str = "";
		// ��ȡ��class�����зǵȼ���
		for (Iterator<OntClass> ontunEqviClses = ontCls.listDisjointWith(); ontunEqviClses.hasNext();) {
			OntClass ontunEqviCls = (OntClass) ontunEqviClses.next();

			str += ontunEqviCls.getLocalName() + "  ������" + ontunEqviCls.getLabel("") + "\r\n";

		}
		System.out.println(ontCls.toString() + "�ķǵȼ����У�\r\n" + str);

		return ontCls;
	}

}
