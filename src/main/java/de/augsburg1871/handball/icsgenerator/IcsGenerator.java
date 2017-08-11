package de.augsburg1871.handball.icsgenerator;

import static de.augsburg1871.handball.icsgenerator.model.Altersklasse.mD;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.util.CollectionUtils;

import de.augsburg1871.handball.icsgenerator.config.FilterConfig;
import de.augsburg1871.handball.icsgenerator.model.Altersklasse;
import de.augsburg1871.handball.icsgenerator.model.Spiel;
import de.augsburg1871.handball.icsgenerator.model.Team;
import de.augsburg1871.handball.icsgenerator.service.api.SpielService;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.model.property.XProperty;
import net.fortuna.ical4j.validate.ValidationException;

@MessageEndpoint
public class IcsGenerator
{

	// private final List<String> ligen;
	// private final List<String> staffeln;
	// private final List<String> mannschaften;
	//
	// public IcsGenerator(final FilterConfig filterConfig)
	// {
	// final List<Team> teams = filterConfig.getTeams();
	// ligen = teams.stream().map(Team::getLiga).collect(Collectors.toList());
	// staffeln = teams.stream().map(Team::getStaffelKurzBezeichnung).collect(Collectors.toList());
	// mannschaften = teams.stream().map(Team::getMannschaft).collect(Collectors.toList());
	//
	// }

	Log log = LogFactory.getLog(IcsGenerator.class);

	private final List<Team> teams;
	private final Map<String, List<Altersklasse>> files;
	private final SpielService spielService;

	public IcsGenerator(final SpielService spielService, final FilterConfig filterConfig)
	{
		teams = filterConfig.getTeams();
		this.files = filterConfig.getFiles();
		this.spielService = spielService;
	}

	@ServiceActivator
	public void writeIcsFiles(final List<Spiel> spiele) throws ParseException, ValidationException, IOException
	{
		for (final Map.Entry<String, List<Altersklasse>> icsFile : files.entrySet()) {
			final String fileName = icsFile.getKey();
			final List<Spiel> data = spielService.findBy(icsFile.getValue());
			data.sort(Comparator.comparing(Spiel::getDatum));

			log.info("writing calendar '" + fileName + "' including " + icsFile.getValue());

			final Calendar calendar = new Calendar();
			calendar.getProperties().add(new ProdId("-//TSV 1871 Augsburg e.V.//Abt. Handball"));
			calendar.getProperties().add(Version.VERSION_2_0);
			// calendar.getProperties().add(Method.PUBLISH);
			calendar.getProperties().add(CalScale.GREGORIAN);
			calendar.getProperties().add(new XProperty("X-WR-CALNAME", determineCalendarTitle(icsFile.getValue())));
			calendar.getProperties().add(new XProperty("X-WR-TIMEZONE", "Europe/Berlin"));
			final ComponentList<CalendarComponent> events = calendar.getComponents();

			for (final Spiel spiel : data) {
				final String date = spiel.getDatum().format(DateTimeFormatter.ISO_DATE_TIME);
				final Date start = new DateTime(date, "yyyy-MM-dd'T'HH:mm:ss", false);
				final Date end = determineEndDate(spiel);
				final String summary = createSummary(spiel);
				final VEvent event = new VEvent(start, end, summary);
				event.getProperties().add(new Uid(spiel.getSpielNummer()));
				event.getProperties().add(new Location(spiel.getHalle()));
				event.getProperties().add(new Description(createDescription(spiel)));
				events.add(event);
			}

			final FileOutputStream fout = new FileOutputStream(fileName);
			final CalendarOutputter outputter = new CalendarOutputter();
			outputter.output(calendar, fout);

			// try {
			// FileUtils.writeLines(new File(fileName), data);
			// } catch (final IOException e) {
			// e.printStackTrace();
			// }
		}
	}

	private DateTime determineEndDate(final Spiel spiel) throws ParseException
	{
		final Optional<Team> team = teams.stream()
				.filter(t -> t.getLiga().equals(spiel.getLiga()))
				.findFirst();

		final Altersklasse altersklasse = team.get().getAltersklasse();
		final long duration = (altersklasse == mD) ? 45 : 90;

		return new DateTime(spiel.getDatum().plusMinutes(duration)
				.format(DateTimeFormatter.ISO_DATE_TIME), "yyyy-MM-dd'T'HH:mm:ss", false);
	}

	private String createDescription(final Spiel spiel)
	{
		String description;

		if (spiel.isHeimspiel()) {
			description = "Heimspiel: ";
		} else {
			description = "Auswärtsspiel: ";
		}

		return description + createSummary(spiel) + "\r\n"
				+ spiel.getHalle() + "\r\n"
				+ "Halennummer: " + spiel.getHallennummer();
	}

	private String createSummary(final Spiel spiel)
	{
		final Optional<Team> team = teams.stream()
				.filter(t -> t.getLiga().equals(spiel.getLiga()))
				.findFirst();

		if (spiel.isHeimspiel()) {
			return team.get().getAltersklasse().getName() + " - " + spiel.getGast();
		} else {
			return spiel.getHeim() + " - " + team.get().getAltersklasse().getName();
		}

	}

	private String determineCalendarTitle(final List<Altersklasse> altersklassesn)
	{
		if (CollectionUtils.isEmpty(altersklassesn)) {
			return null;
		} else if (altersklassesn.size() == 1) {
			return ("Spielplan 1871 Saison 17/18 " + altersklassesn.get(0).getName());
		} else {
			return "Spielplan 1871 Saison 17/18";
		}
	}

	// @ServiceActivator
	// public void createIcsFiles(final List<Spiel> spiele)
	// {
	// final List<Spiel> filteredSpiele = spiele.stream()
	// .filter(s -> ligen.contains(s.getLiga()))
	// .filter(s -> staffeln.contains(s.getStaffelKurzBezeichnung()))
	// .filter(s -> (mannschaften.contains(s.getHeim()) || mannschaften.contains(s.getGast())))
	// .collect(Collectors.toList());
	//
	// final Map<String, List<Spiel>> spielePerLigen =
	// filteredSpiele.stream().collect(Collectors.groupingBy(Spiel::getLiga));
	//
	// for (final Map.Entry<String, List<Spiel>> spielePerLiga : spielePerLigen.entrySet()) {
	// final String liga = spielePerLiga.getKey();
	// final List<Spiel> ligaSpiele = spielePerLiga.getValue();
	// ligaSpiele.sort(Comparator.comparing(Spiel::getDatum));
	// try {
	// FileUtils.writeLines(new File(liga + ".txt"), ligaSpiele);
	// } catch (final IOException e) {
	// e.printStackTrace();
	// }
	//
	// }

}
