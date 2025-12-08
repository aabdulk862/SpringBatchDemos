package com.infy.batch.bean;

public class Claim {
	private Integer claimId;
	private String claimName;
	private Integer requestedBy;
	private Integer claimAmount;
	public Integer getClaimId() {
		return claimId;
	}
	public void setClaimId(Integer claimId) {
		this.claimId = claimId;
	}
	public String getClaimName() {
		return claimName;
	}
	public void setClaimName(String claimName) {
		this.claimName = claimName;
	}
	public Integer getRequestedBy() {
		return requestedBy;
	}
	public void setRequestedBy(Integer requestedBy) {
		this.requestedBy = requestedBy;
	}
	public Integer getClaimAmount() {
		return claimAmount;
	}
	public void setClaimAmount(Integer claimAmount) {
		this.claimAmount = claimAmount;
	}
	@Override
	public String toString() {
		return "Claim [claimId=" + claimId + ", claimName=" + claimName + ", requestedBy=" + requestedBy
				+ ", claimAmount=" + claimAmount + "]";
	}
	

}
