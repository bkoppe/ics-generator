package de.augsburg1871.handball.icsgenerator;

import java.io.File;
import java.util.List;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.dsl.file.Files;

import de.augsburg1871.handball.icsgenerator.config.FilterConfig;
import de.augsburg1871.handball.icsgenerator.model.Spiel;
import de.augsburg1871.handball.icsgenerator.service.api.SpielService;

@Configuration
@EnableConfigurationProperties(FilterConfig.class)
public class FlowConfig
{

	// Datei holen
	// Datei lesen -> mappen
	// sortieren
	// ics schreiben

	@Bean
	IntegrationFlow readFile(final SpielService spielService, final IcsGenerator icsGenerator,
			final SpielFilterList spielFilterList)
	{
		final File directory =
				new File("D:\\dev\\eclipse_workspaces\\sortiment\\ics-generator\\src\\main\\resources\\");
		return IntegrationFlows.from(Files.inboundAdapter(directory)
				.patternFilter("_w*.csv"),
				c -> c.poller(Pollers.fixedDelay(1000)))
				.transform(new CsvToSpielTransformer())
				.handle(spielFilterList)
				.<List<Spiel>> handle((spiel, h) -> spielService.save(spiel))
				.handle(icsGenerator)
				.get();
	}

}
