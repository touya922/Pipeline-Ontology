package semCal;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.db.DBConnection;
import com.hp.hpl.jena.db.IDBConnection;
import com.hp.hpl.jena.ontology.*;

public class OntOperation {

	// 数据库连接参数配置
	public static final String strDriver = "com.mysql.jdbc.Driver"; // path_of_driver_class
	public static final String strURL = "jdbc:mysql://localhost/rqOntDB"; // URL_of_database
	public static final String strUser = "pipeOntUser"; // database user id
	public static final String strPassWord = "orcl"; // database password
	public static final String strDB = "MySQL"; // database type

	/*
	 * 函数功能说明
	 * 
	 * 创建本体模型，然后读取.owl文件，加载模型
	 * 
	 */
	public OntModel createOnt(String owlpath) {

		// 创建一个数据库连接
		IDBConnection conn = new DBConnection(strURL, strUser, strPassWord, strDB);
		// 加载数据库驱动类，需要处理异常
		try {
			Class.forName(strDriver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			// 使用数据库连接参数创建一个模型制造器
			ModelMaker maker = ModelFactory.createModelRDBMaker(conn);
			// 创建一个默认模型
			Model base = maker.createDefaultModel();
			OntModel ontmodel = ModelFactory.createOntologyModel(getModelSpec(maker), base);
			// 读取文件，加载模型
			// ontmodel.read(new FileInputStream(owlpath), "");

			return ontmodel;
		} catch (Exception e) {
			System.out.println("读取失败" + e.getMessage());

			return null;
		}

	}

	/*
	 * 函数功能说明
	 * 
	 * 获取数据库中本体模型
	 * 
	 */
	public OntModel getOntModel() {

		// 创建一个数据库连接
		IDBConnection conn = new DBConnection(strURL, strUser, strPassWord, strDB);

		try {
			// 加载数据库驱动类，需要处理异常
			Class.forName(strDriver);

			// 使用数据库连接参数创建一个模型制造器
			ModelMaker maker = ModelFactory.createModelRDBMaker(conn);
			// 创建一个默认模型
			Model base = maker.createDefaultModel();
			OntModel ontmodel = ModelFactory.createOntologyModel(getModelSpec(maker), base);

			return ontmodel;

		} catch (ClassNotFoundException e) {
			e.printStackTrace();

			return null;
		} catch (Exception e) {
			System.out.println("读取失败" + e.getMessage());

			return null;
		}

	}

	/*
	 * 函数功能说明
	 * 
	 * 创建并获取OntModelSpec
	 * 
	 */
	public static OntModelSpec getModelSpec(ModelMaker maker) {

		OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM);

		spec.setImportModelMaker(maker);

		return spec;
	}

	/*
	 * 函数功能说明
	 * 
	 * 通过名称来获取Class
	 * 
	 */
	public OntClass getClassByName(String owlpath, String conceptName) {

		OntModel ontModel = getOntModel();

		OntClass ontCls;

		// OntModel ontModel = this.createOnt(owlpath);
		String str = "";
		ontCls = ontModel.getOntClass(conceptName);

		// 获取该class的所有属性
		for (Iterator<OntProperty> allPros = ontCls.listDeclaredProperties(); allPros.hasNext();) {

			OntProperty ontPros = (OntProperty) allPros.next();

			// str+=superCls.toString()+"/r/n";
			str += ontPros.getLocalName() + "\r\n--别名：" + ontPros.getLabel("") + "\r\n--属性类型："
					+ ontPros.getRDFType().getLocalName() + "\r\n";

			// 获取该属性的定义域
			String strDomain = "--定义域：";
			for (Iterator<? extends OntResource> allDomains = ontPros.listDomain(); allDomains.hasNext();) {
				OntResource domain = (OntResource) allDomains.next();
				strDomain += domain.getLocalName() + "，";
			}
			strDomain = strDomain.substring(0, strDomain.length() - 1);
			strDomain += "\r\n";

			// 获取该属性的值域
			String strRange = "--值域：";
			for (Iterator<? extends OntResource> allRanges = ontPros.listRange(); allRanges.hasNext();) {
				OntResource range = (OntResource) allRanges.next();
				strRange += range.getLocalName() + ",";
			}
			strRange = strRange.substring(0, strRange.length() - 1);
			strRange += "\r\n";

			str += strDomain + strRange;

		}

		System.out.println(ontCls.toString() + "的属性有：\r\n" + str);

		str = "";
		// 获取该class的所有等价类
		for (Iterator<OntClass> ontEqviClses = ontCls.listEquivalentClasses(); ontEqviClses.hasNext();) {
			OntClass ontEqviCls = (OntClass) ontEqviClses.next();

			str += ontEqviCls.getLocalName() + "  别名：" + ontEqviCls.getLabel("") + "\r\n";

		}
		System.out.println(ontCls.toString() + "的等价类有：\r\n" + str);

		str = "";
		// 获取该class的所有非等价类
		for (Iterator<OntClass> ontunEqviClses = ontCls.listDisjointWith(); ontunEqviClses.hasNext();) {
			OntClass ontunEqviCls = (OntClass) ontunEqviClses.next();

			str += ontunEqviCls.getLocalName() + "  别名：" + ontunEqviCls.getLabel("") + "\r\n";

		}
		System.out.println(ontCls.toString() + "的非等价类有：\r\n" + str);

		return ontCls;
	}

	/*
	 * 函数功能说明
	 * 
	 * 通过名称来获取Class
	 * 
	 */
	public OntClass getClassByName(String conceptName) {
		OntClass ontCLS = null;
		OntModel ontModel = null;

		try {
			// 获取本体模型
			ontModel = getOntModel();
			// 通过概念名称从本体模型中获取概念类
			ontCLS = ontModel.getOntClass(conceptName);

		} catch (Exception e) {
			System.out.println(e);

		}

 		return ontCLS;
	}

	/*
	 * 函数功能说明
	 * 
	 * 通过名称来获取Class
	 * 
	 */
	public List<OntProperty> getPropertiesByClass(OntClass ontCLS) {

		List<OntProperty> lstOntProps = new ArrayList<OntProperty>();

		try {
			// 获取该class的所有属性
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
	 * 函数功能说明
	 * 
	 * 获取某一属性的定义域
	 * 
	 */
	public List<OntResource> getPropertyDomains(OntProperty ontPros) {

		List<OntResource> lstOntResources = new ArrayList<OntResource>();

		try {
			// 获取该属性的定义域
			String strDomain = "--定义域：";
			for (Iterator<? extends OntResource> allDomains = ontPros.listDomain(); allDomains.hasNext();) {
				OntResource domain = (OntResource) allDomains.next();
				lstOntResources.add(domain);
				
				strDomain += domain.getLocalName() + "，";
			}
			strDomain = strDomain.substring(0, strDomain.length() - 1);
			strDomain += "\r\n";

		} catch (Exception e) {
			System.out.println(e);
		}

		return lstOntResources;
	}

	/*
	 * 函数功能说明
	 * 
	 * 获取某一属性的值域
	 * 
	 */
	public List<OntResource> getPropertyRange(OntProperty ontPros) {

		List<OntResource> lstOntResources = new ArrayList<OntResource>();

		try {
			// 获取该属性的值域
			String strRange = "--值域：";
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
	 * 函数功能说明
	 * 
	 * 通过名称来获取Class
	 * 
	 */
	public OntClass getClassByName1(String conceptName) {

		OntModel ontModel = getOntModel();

		OntClass ontCls;
		String str = "";
		ontCls = ontModel.getOntClass(conceptName);

		// 获取该class的所有属性
		for (Iterator<OntProperty> allPros = ontCls.listDeclaredProperties(); allPros.hasNext();) {

			OntProperty ontPros = (OntProperty) allPros.next();

			// str+=superCls.toString()+"/r/n";
			str += ontPros.getLocalName() + "\r\n--别名：" + ontPros.getLabel("") + "\r\n--属性类型："
					+ ontPros.getRDFType().getLocalName() + "\r\n";

			// 获取该属性的定义域
			String strDomain = "--定义域：";
			for (Iterator<? extends OntResource> allDomains = ontPros.listDomain(); allDomains.hasNext();) {
				OntResource domain = (OntResource) allDomains.next();
				strDomain += domain.getLocalName() + "，";
			}
			strDomain = strDomain.substring(0, strDomain.length() - 1);
			strDomain += "\r\n";

			// 获取该属性的值域
			String strRange = "--值域：";
			for (Iterator<? extends OntResource> allRanges = ontPros.listRange(); allRanges.hasNext();) {
				OntResource range = (OntResource) allRanges.next();
				strRange += range.getLocalName() + ",";
			}
			strRange = strRange.substring(0, strRange.length() - 1);
			strRange += "\r\n";

			str += strDomain + strRange;

		}

		System.out.println(ontCls.toString() + "的属性有：\r\n" + str);

		str = "";
		// 获取该class的所有等价类
		for (Iterator<OntClass> ontEqviClses = ontCls.listEquivalentClasses(); ontEqviClses.hasNext();) {
			OntClass ontEqviCls = (OntClass) ontEqviClses.next();

			str += ontEqviCls.getLocalName() + "  别名：" + ontEqviCls.getLabel("") + "\r\n";

		}
		System.out.println(ontCls.toString() + "的等价类有：\r\n" + str);

		str = "";
		// 获取该class的所有非等价类
		for (Iterator<OntClass> ontunEqviClses = ontCls.listDisjointWith(); ontunEqviClses.hasNext();) {
			OntClass ontunEqviCls = (OntClass) ontunEqviClses.next();

			str += ontunEqviCls.getLocalName() + "  别名：" + ontunEqviCls.getLabel("") + "\r\n";

		}
		System.out.println(ontCls.toString() + "的非等价类有：\r\n" + str);

		return ontCls;
	}

}
