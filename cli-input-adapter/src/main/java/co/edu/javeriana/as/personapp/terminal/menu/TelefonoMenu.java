package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.TelefonoInputAdapterCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TelefonoMenu {

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

	public void iniciarMenu(TelefonoInputAdapterCli adapter, Scanner keyboard) {
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
					adapter.setPhoneOutputPortInjection("MARIA");
					menuOpciones(adapter, keyboard);
					break;
				case PERSISTENCIA_MONGODB:
					adapter.setPhoneOutputPortInjection("MONGO");
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

	private void menuOpciones(TelefonoInputAdapterCli adapter, Scanner keyboard) {
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
					adapter.buscarUno(leerTexto(keyboard, "Ingrese el numero: "));
					break;
				case OPCION_CREAR: {
					String numero = leerTexto(keyboard, "Ingrese el numero: ");
					String operador = leerTexto(keyboard, "Ingrese el operador: ");
					Integer duenioCc = leerEntero(keyboard, "Ingrese la cc del duenio: ");
					adapter.crear(numero, operador, duenioCc);
					break;
				}
				case OPCION_EDITAR: {
					String numero = leerTexto(keyboard, "Ingrese el numero a editar: ");
					String operador = leerTexto(keyboard, "Ingrese el nuevo operador: ");
					Integer duenioCc = leerEntero(keyboard, "Ingrese la cc del nuevo duenio: ");
					adapter.editar(numero, operador, duenioCc);
					break;
				}
				case OPCION_ELIMINAR:
					adapter.eliminar(leerTexto(keyboard, "Ingrese el numero a eliminar: "));
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
		System.out.println(OPCION_VER_TODO + " para ver todos los telefonos");
		System.out.println(OPCION_BUSCAR + " para buscar un telefono por numero");
		System.out.println(OPCION_CREAR + " para crear un telefono");
		System.out.println(OPCION_EDITAR + " para editar un telefono");
		System.out.println(OPCION_ELIMINAR + " para eliminar un telefono");
		System.out.println(OPCION_CONTAR + " para contar los telefonos");
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
