package de.augsburg1871.handball.icsgenerator;

import java.time.LocalDateTime;
import java.time.Month;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;

import de.augsburg1871.handball.icsgenerator.model.Spiel;

public class SpielCreator
{

	public static class CsvHeaders
	{

		public final static String DATUM = "Datum";
		public final static String ZEIT = "Zeit";
		public final static String HALLENNUMMER = "Hallennummer";
		public final static String HALLE = "Inhalt Tooltip Halle";
		public final static String SPIELNUMMER = "Spielnummer";
		public final static String LIGA = "Liga";
		public final static String STAFFEL = "Staffelkurzbezeichnung";
		public final static String HEIM = "Heimmannschaft";
		public final static String GAST = "Gastmannschaft";

	}

	public static Spiel from(final CSVRecord csvRecord)
	{
		final String[] datum = StringUtils.split(csvRecord.get(CsvHeaders.DATUM), ".");
		final String[] zeit = StringUtils.split(csvRecord.get(CsvHeaders.ZEIT), ":");

		final int dayOfMonth = Integer.valueOf(datum[0]);
		final Month month = Month.of(Integer.valueOf(datum[1]));
		final int year = Integer.valueOf(datum[2]);
		final int hour = Integer.valueOf(zeit[0]);
		final int minute = Integer.valueOf(zeit[1]);

		final Spiel spiel = new Spiel();
		spiel.setDatum(LocalDateTime.of(year, month, dayOfMonth, hour, minute));
		spiel.setHallennummer(csvRecord.get(CsvHeaders.HALLENNUMMER));
		spiel.setHalle(csvRecord.get(CsvHeaders.HALLE));
		spiel.setSpielNummer(csvRecord.get(CsvHeaders.SPIELNUMMER));
		spiel.setLiga(csvRecord.get(CsvHeaders.LIGA));
		spiel.setStaffelKurzBezeichnung(csvRecord.get(CsvHeaders.STAFFEL));
		spiel.setHeim(csvRecord.get(CsvHeaders.HEIM));
		spiel.setGast(csvRecord.get(CsvHeaders.GAST));

		return spiel;
	}

}
