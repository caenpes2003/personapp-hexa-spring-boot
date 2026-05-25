package co.edu.javeriana.as.personapp.terminal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudioModelCli {
	private Integer personaCc;
	private String personaNombre;
	private Integer profesionId;
	private String profesionNombre;
	private String fechaGraduacion;
	private String universidad;
}
