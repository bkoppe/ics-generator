package de.augsburg1871.handball.icsgenerator;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import com.google.common.collect.Lists;

import de.augsburg1871.handball.icsgenerator.config.FilterConfig;
import de.augsburg1871.handball.icsgenerator.model.Spiel;
import de.augsburg1871.handball.icsgenerator.model.Team;

@EnableConfigurationProperties(FilterConfig.class)
@MessageEndpoint
public class SpielFilterList
{
	private final List<String> ligen;
	private final List<String> staffeln;
	private final List<String> mannschaften;

	public SpielFilterList(final FilterConfig filterConfig)
	{
		final List<Team> teams = filterConfig.getTeams();
		ligen = teams.stream().map(Team::getLiga).collect(Collectors.toList());
		staffeln = teams.stream().map(Team::getStaffelKurzBezeichnung).collect(Collectors.toList());
		mannschaften = teams.stream().map(Team::getMannschaft).collect(Collectors.toList());

	}

	@ServiceActivator
	public List<Spiel> filter(final List<Spiel> spiele)
	{
		final List<Spiel> filteredSpiele = Lists.newArrayList();

		for (final Spiel spiel : spiele) {
			if (isSpielOfInterest(spiel)) {
				filteredSpiele.add(spiel);
			}
		}

		return filteredSpiele;
	}

	private boolean isSpielOfInterest(final Spiel spiel)
	{
		if (spiel == null) {
			return false;
		}

		if (!ligen.contains(spiel.getLiga())) {
			return false;
		}

		if (!staffeln.contains(spiel.getStaffelKurzBezeichnung())) {
			return false;
		}

		if (!mannschaften.contains(spiel.getHeim()) && !mannschaften.contains(spiel.getGast())) {
			return false;
		}

		return true;
	}
}
