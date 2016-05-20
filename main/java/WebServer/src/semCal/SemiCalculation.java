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

	public static List<PointPair> lstPointPairs = null;// �������������ƶȵĹܵ�ƥ���
	public static Double coefDescri = 0.5;// �������ƶ�ϵ��
	public static Double coefStruct = 0.3;// �ṹ���ƶ�ϵ��
	public static Double coefHirechi = 0.2;// ������ƶ�ϵ��

	/**
	 * �����������ƶ�
	 */
	public static void semCal() {

		if (lstPointPairs.size() <= 0 || lstPointPairs == null)
			return;

		System.out.println("��ʼ����ƥ��Թܵ����������ƶ�....\r\n");
		System.out.println("�ۺϹܵ�      רҵ�ܵ�      �������ƶ�      �������ƶ�      �ṹ���ƶ�      ������ƶ�\r\n");

		try {
			for (int i = 0; i < lstPointPairs.size(); i++) {
				PointPair pPointPair = lstPointPairs.get(i);

				ZHPoint pZHPoint = pPointPair.zhPoint;
				ZYPoint pZYPoint = pPointPair.zyPoint;

				// �����������ƶȡ��ṹ���ƶȡ�������ƶ�
				Double dDescriSemi = DescriSemi.DescriSemiCal(pZHPoint, pZYPoint);
				Double dStructSemi = StructSemi.StructSemi(pZHPoint, pZYPoint);
				Double dHirechiSemi = HirechiSemi.HirechiSemi(pZHPoint, pZYPoint);

				// ���������ƶȡ��ṹ���ƶȡ�������ƶ�ֵ���¸���PointPair
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

		// System.out.println("��ʼ����" + concept1 + "��" + concept2 +
		// "֮����������ƶ�....\r\n");
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
	 * ��objectת����jsonstring �����а���ʵ����� ��List<Student> ��Student�к�������List<Teacher>
	 * 
	 * @param obj
	 *            ��Ҫ���л���obj����
	 * @return json�ַ���
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
	 * ��jsonstringת����object �����а���ʵ����� ��List<Student> ��Student�к�������List<Teacher>
	 * 
	 * @param jsonStr
	 *            ��Ҫ���л���obj����
	 * @return obj����
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
	 * ��jsonstringת����objectList �����а���ʵ����� ��List<Student> ��Student�к�������List
	 * <Teacher>
	 * 
	 * @param jsonStr
	 *            ��Ҫ���л���obj����
	 * @return obj����
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
		 * = "��Ȼ��"; zhPoint.ObjectID = "11"; zyPoint.GDMS = 12.0; zyPoint.GXHY =
		 * "��Ȼ��"; ppair.descriSemi = 0.2; ppair.hirechiSemi = 0.1; ppair.Semi =
		 * 0.1; ppair.structSemi = 0.34; ppair.zhPoint = zhPoint; ppair.zyPoint
		 * = zyPoint;
		 * 
		 * List<PointPair> lstPPair = new ArrayList<PointPair>();
		 * lstPPair.add(ppair); lstPPair.add(ppair);
		 * 
		 * String strJson = ObjectToJSonString(lstPPair);
		 */

		// ��json�ַ���ת��list����
		lstPointPairs = (List<PointPair>) JSonStringToObjectList(strResult);

		semCal();

	}

}
