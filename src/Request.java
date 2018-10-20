import java.io.Serializable;

public class Request implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Atributes
	private int objectID;
	private String method;
	private String type;
	private Place place;

	// Constructors
	public Request(Place place) {
		this.place = place;
		this.type = "new";
	}

	public Request(int objectID, String method) {
		this.objectID = objectID;
		this.method = method;
		this.type = "invoke";
	}

	public int getObjectID() {
		return objectID;
	}

	public void setObjectID(int objectID) {
		this.objectID = objectID;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}
}
