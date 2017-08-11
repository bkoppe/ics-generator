package de.augsburg1871.handball.icsgenerator.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.augsburg1871.handball.icsgenerator.config.FilterConfig;
import de.augsburg1871.handball.icsgenerator.model.Altersklasse;
import de.augsburg1871.handball.icsgenerator.model.Spiel;
import de.augsburg1871.handball.icsgenerator.model.Team;
import de.augsburg1871.handball.icsgenerator.service.api.SpielService;

@EnableConfigurationProperties(FilterConfig.class)
@Service
public class SpielServiceImpl implements SpielService
{

	private final Map<Altersklasse, List<Spiel>> SPIELE_PER_LIGA = Maps.newHashMap();
	final List<Team> teams;

	public SpielServiceImpl(final FilterConfig filterConfig)
	{
		this.teams = filterConfig.getTeams();
	}

	@Override
	public Spiel save(final Spiel spiel)
	{
		final Altersklasse altersklasse = determineFrom(spiel.getLiga());

		if (SPIELE_PER_LIGA.containsKey(altersklasse)) {
			SPIELE_PER_LIGA.get(altersklasse).add(spiel);
		} else {
			SPIELE_PER_LIGA.put(altersklasse, Lists.newArrayList(spiel));
		}

		return spiel;
	}

	@Override
	public List<Spiel> save(final List<Spiel> spiele)
	{
		final List<Spiel> saved = Lists.newArrayList();
		for (final Spiel spiel : spiele) {
			saved.add(save(spiel));
		}
		return saved;
	}

	@Override
	public List<Spiel> findBy(final Altersklasse altersklasse)
	{
		return SPIELE_PER_LIGA.get(altersklasse);
	}

	@Override
	public List<Spiel> findBy(final List<Altersklasse> altersklassen)
	{
		final List<Spiel> spiele = Lists.newArrayList();
		for (final Altersklasse altersklasse : altersklassen) {
			final List<Spiel> found = findBy(altersklasse);
			if (CollectionUtils.isEmpty(found)) {
				continue;
			}
			spiele.addAll(found);
		}

		return spiele;
	}

	private Altersklasse determineFrom(final String liga)
	{
		final Optional<Team> orElse = teams.stream()
				.filter(team -> team.getLiga().equals(liga))
				.findFirst();

		if (!orElse.isPresent()) {
			return null;
		}

		return orElse.get().getAltersklasse();
	}

}
