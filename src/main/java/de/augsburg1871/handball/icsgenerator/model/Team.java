package de.augsburg1871.handball.icsgenerator.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Team
{

	private Altersklasse altersklasse;
	private String liga;
	private String staffelKurzBezeichnung;
	private String mannschaft;

	public Altersklasse getAltersklasse()
	{
		return altersklasse;
	}

	public void setAltersklasse(final Altersklasse altersklasse)
	{
		this.altersklasse = altersklasse;
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

	public String getMannschaft()
	{
		return mannschaft;
	}

	public void setMannschaft(final String mannschaft)
	{
		this.mannschaft = mannschaft;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (!(obj instanceof Team)) {
			return false;
		}

		if (obj == this) {
			return true;
		}

		final Team rhs = (Team) obj;
		return new EqualsBuilder()
				.append(liga, rhs.liga)
				.append(staffelKurzBezeichnung, rhs.staffelKurzBezeichnung)
				.append(mannschaft, rhs.mannschaft)
				.isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder()
				.append(liga)
				.append(staffelKurzBezeichnung)
				.append(mannschaft)
				.build();
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this)
				.append(liga)
				.append(mannschaft)
				.build();
	}

}
