package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.EstudioInputAdapterCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EstudioMenu {

	private static final int OPCION_REGRESAR_MODULOS = 0;
	private static final int PERSISTENCIA_MARIADB = 1;
	private static final int PERSISTENCIA_MONGODB = 2;

	private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
	private static final int OPCION_VER_TODO = 1;
	private static final int OPCION_BUSCAR = 2;
	private static final int OPCION_CREAR = 3;
	private static final int OPCION_EDITAR = 4;
	private static final int OPCION_ELIMINAR = 5;
	private static final int OPCION_CONTAR = 6;

	public void iniciarMenu(EstudioInputAdapterCli adapter, Scanner keyboard) {
		boolean isValid = false;
		do {
			try {
				mostrarMenuMotorPersistencia();
				int opcion = leerOpcion(keyboard);
				switch (opcion) {
				case OPCION_REGRESAR_MODULOS:
					isValid = true;
					break;
				case PERSISTENCIA_MARIADB:
					adapter.setStudyOutputPortInjection("MARIA");
					menuOpciones(adapter, keyboard);
					break;
				case PERSISTENCIA_MONGODB:
					adapter.setStudyOutputPortInjection("MONGO");
					menuOpciones(adapter, keyboard);
					break;
				default:
					log.warn("La opcion elegida no es valida.");
				}
			} catch (InvalidOptionException e) {
				log.warn(e.getMessage());
			}
		} while (!isValid);
	}

	private void menuOpciones(EstudioInputAdapterCli adapter, Scanner keyboard) {
		boolean isValid = false;
		do {
			try {
				mostrarMenuOpciones();
				int opcion = leerOpcion(keyboard);
				switch (opcion) {
				case OPCION_REGRESAR_MOTOR_PERSISTENCIA:
					isValid = true;
					break;
				case OPCION_VER_TODO:
					adapter.historial();
					break;
				case OPCION_BUSCAR: {
					Integer cc = leerEntero(keyboard, "Ingrese la cc de la persona: ");
					Integer idProf = leerEntero(keyboard, "Ingrese el id de la profesion: ");
					adapter.buscarUno(cc, idProf);
					break;
				}
				case OPCION_CREAR: {
					Integer cc = leerEntero(keyboard, "Ingrese la cc de la persona: ");
					Integer idProf = leerEntero(keyboard, "Ingrese el id de la profesion: ");
					String fecha = leerTexto(keyboard, "Ingrese la fecha de graduacion (AAAA-MM-DD): ");
					String univer = leerTexto(keyboard, "Ingrese el nombre de la universidad: ");
					adapter.crear(cc, idProf, fecha, univer);
					break;
				}
				case OPCION_EDITAR: {
					Integer cc = leerEntero(keyboard, "Ingrese la cc de la persona: ");
					Integer idProf = leerEntero(keyboard, "Ingrese el id de la profesion: ");
					String fecha = leerTexto(keyboard, "Ingrese la nueva fecha (AAAA-MM-DD): ");
					String univer = leerTexto(keyboard, "Ingrese la nueva universidad: ");
					adapter.editar(cc, idProf, fecha, univer);
					break;
				}
				case OPCION_ELIMINAR: {
					Integer cc = leerEntero(keyboard, "Ingrese la cc de la persona: ");
					Integer idProf = leerEntero(keyboard, "Ingrese el id de la profesion: ");
					adapter.eliminar(cc, idProf);
					break;
				}
				case OPCION_CONTAR:
					adapter.contar();
					break;
				default:
					log.warn("La opcion elegida no es valida.");
				}
			} catch (InputMismatchException e) {
				log.warn("Solo se permiten numeros.");
				keyboard.next();
			}
		} while (!isValid);
	}

	private void mostrarMenuOpciones() {
		System.out.println("----------------------");
		System.out.println(OPCION_VER_TODO + " para ver todos los estudios");
		System.out.println(OPCION_BUSCAR + " para buscar un estudio por cc e id profesion");
		System.out.println(OPCION_CREAR + " para crear un estudio");
		System.out.println(OPCION_EDITAR + " para editar un estudio");
		System.out.println(OPCION_ELIMINAR + " para eliminar un estudio");
		System.out.println(OPCION_CONTAR + " para contar los estudios");
		System.out.println(OPCION_REGRESAR_MOTOR_PERSISTENCIA + " para regresar");
	}

	private void mostrarMenuMotorPersistencia() {
		System.out.println("----------------------");
		System.out.println(PERSISTENCIA_MARIADB + " para MariaDB");
		System.out.println(PERSISTENCIA_MONGODB + " para MongoDB");
		System.out.println(OPCION_REGRESAR_MODULOS + " para regresar");
	}

	private int leerOpcion(Scanner keyboard) {
		try {
			System.out.print("Ingrese una opcion: ");
			return keyboard.nextInt();
		} catch (InputMismatchException e) {
			log.warn("Solo se permiten numeros.");
			keyboard.next();
			return leerOpcion(keyboard);
		}
	}

	private Integer leerEntero(Scanner keyboard, String prompt) {
		System.out.print(prompt);
		while (!keyboard.hasNextInt()) {
			log.warn("Solo se permiten numeros.");
			keyboard.next();
			System.out.print(prompt);
		}
		int valor = keyboard.nextInt();
		keyboard.nextLine();
		return valor;
	}

	private String leerTexto(Scanner keyboard, String prompt) {
		System.out.print(prompt);
		return keyboard.nextLine();
	}
}
