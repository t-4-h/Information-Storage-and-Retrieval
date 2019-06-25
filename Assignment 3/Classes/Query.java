package Classes;

public class Query {
	//you can modify this class

	private String queryContent;	
	private String topicId;	
	
	public Query(String content, String id) {
		this.queryContent = content;
		this.topicId = id;
	}
	
	public String GetQueryContent() {
		return queryContent;
	}
	public String GetTopicId() {
		return topicId;
	}
	public void SetQueryContent(String content){
		queryContent=content;
	}	
	public void SetTopicId(String id){
		topicId=id;
	}
}

