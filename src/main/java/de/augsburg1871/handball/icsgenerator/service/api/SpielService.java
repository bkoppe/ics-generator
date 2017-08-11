package de.augsburg1871.handball.icsgenerator.service.api;

import java.util.List;

import de.augsburg1871.handball.icsgenerator.model.Altersklasse;
import de.augsburg1871.handball.icsgenerator.model.Spiel;

public interface SpielService
{

	List<Spiel> findBy(Altersklasse altersklasse);

	Spiel save(final Spiel spiel);

	List<Spiel> save(final List<Spiel> spiele);

	List<Spiel> findBy(List<Altersklasse> altersklassen);

}
