package ch.hsr.apparch.kitchendeviceservice.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.*;

import java.io.Serializable;

@Entity
@EqualsAndHashCode(of = {"name", "function", "available"})
@ToString(exclude = "id")
public class KitchenDevice implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Getter
	@Setter
	private String name;

	@Getter
	@Setter
	private String function;

	@Getter
	@Setter
	private boolean available;
}
