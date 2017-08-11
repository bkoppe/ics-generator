package de.augsburg1871.handball.icsgenerator.model;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Spiel
{

	private LocalDateTime datum;
	private String liga;
	private String staffelKurzBezeichnung;
	private String hallennummer;
	private String halle;
	private String spielNummer;
	private String heim;
	private String gast;

	public LocalDateTime getDatum()
	{
		return datum;
	}

	public void setDatum(final LocalDateTime datum)
	{
		this.datum = datum;
	}

	public String getLiga()
	{
		return liga;
	}

	public void setLiga(final String liga)
	{
		this.liga = liga;
	}

	public String getStaffelKurzBezeichnung()
	{
		return staffelKurzBezeichnung;
	}

	public void setStaffelKurzBezeichnung(final String staffelKurzBezeichnung)
	{
		this.staffelKurzBezeichnung = staffelKurzBezeichnung;
	}

	public String getHallennummer()
	{
		return hallennummer;
	}

	public void setHallennummer(final String hallennummer)
	{
		this.hallennummer = hallennummer;
	}

	public String getHalle()
	{
		return halle;
	}

	public void setHalle(final String halle)
	{
		this.halle = halle;
	}

	public String getSpielNummer()
	{
		return spielNummer;
	}

	public void setSpielNummer(final String spielNummer)
	{
		this.spielNummer = spielNummer;
	}

	public String getHeim()
	{
		return heim;
	}

	public void setHeim(final String heim)
	{
		this.heim = heim;
	}

	public String getGast()
	{
		return gast;
	}

	public void setGast(final String gast)
	{
		this.gast = gast;
	}

	public boolean isHeimspiel()
	{
		return this.heim.equals("Augsburg 1871")
				|| this.heim.equals("Augsburg 1871 II")
				|| this.heim.equals("SG Augsburg-Gersthofen");
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this)
				.append(datum)
				.append(heim)
				.append(gast)
				.build();
	}

}
