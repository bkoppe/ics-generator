package de.augsburg1871.handball.icsgenerator;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.integration.annotation.Filter;
import org.springframework.integration.annotation.MessageEndpoint;

import de.augsburg1871.handball.icsgenerator.config.FilterConfig;
import de.augsburg1871.handball.icsgenerator.model.Spiel;
import de.augsburg1871.handball.icsgenerator.model.Team;

@EnableConfigurationProperties(FilterConfig.class)
@MessageEndpoint
public class Spielfilter
{

	private final List<String> ligen;
	private final List<String> staffeln;
	private final List<String> mannschaften;

	public Spielfilter(final FilterConfig filterConfig)
	{
		final List<Team> teams = filterConfig.getTeams();
		ligen = teams.stream().map(Team::getLiga).collect(Collectors.toList());
		staffeln = teams.stream().map(Team::getStaffelKurzBezeichnung).collect(Collectors.toList());
		mannschaften = teams.stream().map(Team::getMannschaft).collect(Collectors.toList());

	}

	@Filter
	public boolean filter(final Spiel spiel)
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

		// if (isTeamOfIntereset(spiel.getHeim()) || isTeamOfIntereset(spiel.getGast())) {
		// return true;
		// }

		return true;
	}

	private boolean isTeamOfIntereset(final String team)
	{
		if ("Augsburg 1871".equals(team)) {
			return true;
		}

		if ("Augsburg 1871 II".equals(team)) {
			return true;
		}

		if ("SG Augsburg-Gersthofen".equals(team)) {
			return true;
		}

		return false;
	}

}
