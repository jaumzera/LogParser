package com.ef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "blocked_ips")
public class BlockedIp {

	public static BlockedIp of(String ip, Long totalRequests, String reason) {
		return new BlockedIp(ip, totalRequests, reason);
	}

	private BlockedIp(String ip, Long totalRequests, String reason) {
		this.ip = ip;
		this.reason = reason;
		this.totalRequests = totalRequests;

	}

	private BlockedIp() {
		// Required by Hibernate
	}

	@Id
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String ip;

	@Column(name = "total_requests")
	private Long totalRequests;

	private String reason;
	
	@Override
	public String toString() {
		return reason;
	}

}
