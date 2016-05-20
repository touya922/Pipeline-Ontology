package semCal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;


public class SemiCalculation {

	public static List<PointPair> lstPointPairs = null;// 待计算语义相似度的管点匹配对
	public static Double coefDescri = 0.5;// 描述相似度系数
	public static Double coefStruct = 0.3;// 结构相似度系数
	public static Double coefHirechi = 0.2;// 层次相似度系数

	/**
	 * 计算语义相似度
	 */
	public static void semCal() {

		if (lstPointPairs.size() <= 0 || lstPointPairs == null)
			return;

		System.out.println("开始计算匹配对管点间的语义相似度....\r\n");
		System.out.println("综合管点      专业管点      语义相似度      描述相似度      结构相似度      层次相似度\r\n");

		try {
			for (int i = 0; i < lstPointPairs.size(); i++) {
				PointPair pPointPair = lstPointPairs.get(i);

				ZHPoint pZHPoint = pPointPair.zhPoint;
				ZYPoint pZYPoint = pPointPair.zyPoint;

				// 计算描述相似度、结构相似度、层次相似度
				Double dDescriSemi = DescriSemi.DescriSemiCal(pZHPoint, pZYPoint);
				Double dStructSemi = StructSemi.StructSemi(pZHPoint, pZYPoint);
				Double dHirechiSemi = HirechiSemi.HirechiSemi(pZHPoint, pZYPoint);

				// 将描述相似度、结构相似度、层次相似度值重新赋给PointPair
				pPointPair.descriSemi = dDescriSemi;
				pPointPair.structSemi = dStructSemi;
				pPointPair.hirechiSemi = dHirechiSemi;

				Double dSemi = coefDescri * dDescriSemi + coefStruct * dStructSemi + coefHirechi * dHirechiSemi;

				pPointPair.Semi = dSemi;

				System.out.println(pZHPoint.PointName + pZHPoint.ObjectID + "   " + pZYPoint.PointName
						+ pZYPoint.ObjectID + "   " + pPointPair.Semi + "   " + pPointPair.descriSemi + "   "
						+ pPointPair.structSemi + "   " + pPointPair.hirechiSemi+"\r\n");

			}

		} catch (Exception e) {
			System.out.println(e);
		}

		// System.out.println("开始计算" + concept1 + "和" + concept2 +
		// "之间的语义相似度....\r\n");
		// OntClass ontCls1, ontCls2;

	}

	private static String ReadTxt() {

		File file = new File("data/pointpairs3.txt");
		BufferedReader reader = null;
		String tempString = null;
		String strResult = "";

		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			while ((tempString = reader.readLine()) != null) {
				strResult += tempString + "\r\n";
			}
			reader.close();
			return strResult;
		} catch (FileNotFoundException e) {
			e.printStackTrace();

			return null;
		} catch (IOException e) {
			e.printStackTrace();

			return null;
		} finally {
			if (reader != null) {
				try {
					reader.close();

					return strResult;
				} catch (IOException e) {
					e.printStackTrace();

					return null;
				}
			}
		}
	}

	/**
	 * 将object转换成jsonstring 属性中包含实体类等 如List<Student> 而Student中含有属性List<Teacher>
	 * 
	 * @param obj
	 *            需要序列化的obj对象
	 * @return json字符串
	 */
	public static String ObjectToJSonString(Object obj) {
		String jsonStr = "";
		ObjectMapper mapper = new ObjectMapper();

		try {
			// Convert object to JSON string
			jsonStr = mapper.writeValueAsString(obj);
			System.out.println(jsonStr);

		} catch (Exception e) {
			System.out.println(e);
		}
		return jsonStr;
	}

	/**
	 * 将jsonstring转换成object 属性中包含实体类等 如List<Student> 而Student中含有属性List<Teacher>
	 * 
	 * @param jsonStr
	 *            需要序列化的obj对象
	 * @return obj对象
	 */
	@SuppressWarnings("unchecked")
	public static Object JSonStringToObject(String jsonStr, Class classProto) {
		Object obj = null;
		ObjectMapper mapper = new ObjectMapper();

		try {
			// Convert Json string to Object
			obj = mapper.readValue(jsonStr, classProto);
			System.out.println(obj);

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
		return obj;
	}

	/**
	 * 将jsonstring转换成objectList 属性中包含实体类等 如List<Student> 而Student中含有属性List
	 * <Teacher>
	 * 
	 * @param jsonStr
	 *            需要序列化的obj对象
	 * @return obj对象
	 */
	public static List<PointPair> JSonStringToObjectList(String jsonStr) {
		List<PointPair> lstPPairs = null;
		ObjectMapper mapper = new ObjectMapper();

		try {
			// Convert Json string to Object
			lstPPairs = mapper.readValue(jsonStr, new TypeReference<List<PointPair>>() {
			});
			System.out.println(lstPPairs);

		} catch (Exception e) {
			System.out.println(e);
		}
		return lstPPairs;
	}

	public static void main(String[] args) {

		String strResult = ReadTxt();
		System.out.println(strResult);

		/*
		 * ZHPoint zhPoint = new ZHPoint(); ZYPoint zyPoint = new ZYPoint();
		 * PointPair ppair = new PointPair(); zhPoint.GDMS = 11.0; zhPoint.GXHY
		 * = "天然气"; zhPoint.ObjectID = "11"; zyPoint.GDMS = 12.0; zyPoint.GXHY =
		 * "天然气"; ppair.descriSemi = 0.2; ppair.hirechiSemi = 0.1; ppair.Semi =
		 * 0.1; ppair.structSemi = 0.34; ppair.zhPoint = zhPoint; ppair.zyPoint
		 * = zyPoint;
		 * 
		 * List<PointPair> lstPPair = new ArrayList<PointPair>();
		 * lstPPair.add(ppair); lstPPair.add(ppair);
		 * 
		 * String strJson = ObjectToJSonString(lstPPair);
		 */

		// 将json字符串转成list对象
		lstPointPairs = (List<PointPair>) JSonStringToObjectList(strResult);

		semCal();

	}

}
