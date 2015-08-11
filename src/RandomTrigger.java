import java.util.Random;

public class RandomTrigger {

	private Random random;
	private int inicioRango;
	private int longitudRango;
	private int tiempoRestante;
	private boolean toggle;
	private boolean trigger = true;

	public RandomTrigger(int min, int max) {
		this(min, max, false);
	}

	public RandomTrigger(int min, int max, boolean toggle) {
		random = new Random();
		set(min, max);
		setToggle(toggle);
	}

	public boolean test() {
		if (tiempoRestante-- == 0) {
			reset();

			boolean resultado = trigger;
			if (toggle)
				trigger = !trigger;
			return resultado;
		}

		return !trigger;
	}

	public void reset() {
		tiempoRestante = inicioRango + random.nextInt(longitudRango);
	}

	public void set(int min, int max) {
		inicioRango = min;
		longitudRango = max - min + 1;
		reset();
	}

	public void setToggle(boolean toggle) {
		if (!toggle)
			trigger = true;
		this.toggle = toggle;
	}

	public boolean getEstado() {
		return tiempoRestante == 0 ? trigger : !trigger;
	}
}
