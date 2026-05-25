package co.edu.javeriana.as.personapp.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelefonoResponse {
	private String number;
	private String company;
	private String ownerCc;
	private String ownerName;
	private String database;
	private String status;
}
