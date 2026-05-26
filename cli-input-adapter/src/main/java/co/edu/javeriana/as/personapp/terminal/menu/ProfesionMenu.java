package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.ProfesionInputAdapterCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProfesionMenu {

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

	public void iniciarMenu(ProfesionInputAdapterCli adapter, Scanner keyboard) {
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
					adapter.setProfessionOutputPortInjection("MARIA");
					menuOpciones(adapter, keyboard);
					break;
				case PERSISTENCIA_MONGODB:
					adapter.setProfessionOutputPortInjection("MONGO");
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

	private void menuOpciones(ProfesionInputAdapterCli adapter, Scanner keyboard) {
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
				case OPCION_BUSCAR:
					adapter.buscarUno(leerEntero(keyboard, "Ingrese el id: "));
					break;
				case OPCION_CREAR: {
					Integer id = leerEntero(keyboard, "Ingrese el id: ");
					String nombre = leerTexto(keyboard, "Ingrese el nombre: ");
					String descripcion = leerTexto(keyboard, "Ingrese la descripcion: ");
					adapter.crear(id, nombre, descripcion);
					break;
				}
				case OPCION_EDITAR: {
					Integer id = leerEntero(keyboard, "Ingrese el id a editar: ");
					String nombre = leerTexto(keyboard, "Ingrese el nuevo nombre: ");
					String descripcion = leerTexto(keyboard, "Ingrese la nueva descripcion: ");
					adapter.editar(id, nombre, descripcion);
					break;
				}
				case OPCION_ELIMINAR:
					adapter.eliminar(leerEntero(keyboard, "Ingrese el id a eliminar: "));
					break;
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
		System.out.println(OPCION_VER_TODO + " para ver todas las profesiones");
		System.out.println(OPCION_BUSCAR + " para buscar una profesion por id");
		System.out.println(OPCION_CREAR + " para crear una profesion");
		System.out.println(OPCION_EDITAR + " para editar una profesion");
		System.out.println(OPCION_ELIMINAR + " para eliminar una profesion");
		System.out.println(OPCION_CONTAR + " para contar las profesiones");
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
