package semCal;

public class PointPairResult {
	
	public String ZHPointName;//综合管点名称
	public String ZHPointObj;//综合管点obj
	public String ZYPointName;//专业管点名称
	public String ZYPointObj;//专业管点obj
	public double descriSemi;//描述相似度
	public double structSemi;//结构相似度
	public double hirechiSemi;//层次相似度
	public double Semi;//语义相似度（综合语义相似度=a*描述相似度+b*结构相似度+c*层次相似度）

}
