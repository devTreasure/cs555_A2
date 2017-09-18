package Discovery;

public class RingNodes {
	
	public int  ringNodeID;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ringNodeID;
		result = prime * result + ((ringNodeIP == null) ? 0 : ringNodeIP.hashCode());
		result = prime * result + ringnodePORT;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RingNodes other = (RingNodes) obj;
		if (ringNodeID != other.ringNodeID)
			return false;
		if (ringNodeIP == null) {
			if (other.ringNodeIP != null)
				return false;
		} else if (!ringNodeIP.equals(other.ringNodeIP))
			return false;
		if (ringnodePORT != other.ringnodePORT)
			return false;
		return true;
	}
	public String ringNodeIP;
	public int ringnodePORT;
	
}
