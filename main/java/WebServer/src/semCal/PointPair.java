package semCal;

public class PointPair {
	public ZHPoint zhPoint;
	public ZYPoint zyPoint;
	public Double descriSemi;// 描述相似度
	public Double structSemi;// 结构相似度
	public Double hirechiSemi;// 层次相似度
	public Double Semi;// 语义相似度（综合语义相似度=a*描述相似度+b*结构相似度+c*层次相似度）

}
