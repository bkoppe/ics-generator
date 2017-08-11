package de.augsburg1871.handball.icsgenerator.config;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.augsburg1871.handball.icsgenerator.model.Altersklasse;
import de.augsburg1871.handball.icsgenerator.model.Team;

@ConfigurationProperties
public class FilterConfig
{

	private List<Team> teams = Lists.newArrayList();
	private Map<String, List<Altersklasse>> files = Maps.newHashMap();

	public List<Team> getTeams()
	{
		return teams;
	}

	public void setTeams(final List<Team> teams)
	{
		this.teams = teams;
	}

	public Map<String, List<Altersklasse>> getFiles()
	{
		return files;
	}

	public void setFiles(final Map<String, List<Altersklasse>> files)
	{
		this.files = files;
	}

}
