package co.edu.javeriana.as.personapp.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudioResponse {
	private String personCc;
	private String personName;
	private String professionId;
	private String professionName;
	private String graduationDate;
	private String universityName;
	private String database;
	private String status;
}
