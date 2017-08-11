package de.augsburg1871.handball.icsgenerator.model;

public enum Altersklasse
{

	minis("Minis"),
	gE("gemischte E-Jugend"),
	mD("männliche D-Jugend"),
	mC("männliche C-Jugend"),
	mA("männliche A-Jugend"),
	frauen("Frauen"),
	maenner1("Männer I"),
	maenner2("Männer II");

	private String teamName;

	private Altersklasse(final String teamName)
	{
		this.teamName = teamName;
	}

	public String getName()
	{
		return this.teamName;
	}

}
