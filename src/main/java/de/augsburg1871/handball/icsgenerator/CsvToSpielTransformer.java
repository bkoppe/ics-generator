package de.augsburg1871.handball.icsgenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.integration.annotation.Transformer;

import com.google.common.collect.Lists;

import de.augsburg1871.handball.icsgenerator.model.Spiel;

public class CsvToSpielTransformer
{

	private static final Log log = LogFactory.getLog(CsvToSpielTransformer.class);

	private final CSVFormat csvFormat;

	public CsvToSpielTransformer()
	{
		csvFormat = CSVFormat.DEFAULT
				.withDelimiter(';')
				.withFirstRecordAsHeader()
				.withIgnoreEmptyLines();
	}

	@Transformer
	public List<Spiel> transformToEntities(final File file)
	{
		final List<Spiel> entities = Lists.newArrayList();

		try (CSVParser parser = CSVParser.parse(file, StandardCharsets.ISO_8859_1, csvFormat)) {
			for (final CSVRecord csvRecord : parser) {
				entities.add(SpielCreator.from(csvRecord));
			}
		} catch (final FileNotFoundException e) {
			log.error("Die Datei ['" + file + "'] wurde nicht gefunden.", e);
		} catch (final IOException e) {
			log.error("Fehler beim Verarbeiten der Datei ['" + file + "'].", e);
		}

		return entities;
	}

}
