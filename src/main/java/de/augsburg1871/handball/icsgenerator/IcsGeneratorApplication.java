package de.augsburg1871.handball.icsgenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import de.augsburg1871.handball.icsgenerator.config.FilterConfig;

@SpringBootApplication
@EnableConfigurationProperties({ FilterConfig.class })
public class IcsGeneratorApplication // implements CommandLineRunner
{

	public static void main(final String[] args)
	{
		SpringApplication.run(IcsGeneratorApplication.class, args);
	}

	// @Override
	// public void run(final String... args) throws Exception
	// {
	// // for (final Team item : filterConfig.getTeams()) {
	// // System.out.println(item);
	// // }
	// //
	// // for (final Entry<String, List<Altersklasse>> fileMapping : filterConfig.getFiles().entrySet()) {
	// // final String files = StringUtils.join(fileMapping.getValue());
	// // System.out.println(fileMapping.getKey() + " " + files);
	// // }
	// }

}
