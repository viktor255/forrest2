package cz.muni.fi.pv168.forrest;

import java.util.List;

/**
 * @author Viktor Lehotsky on 08.03.2017.
 */
public interface PotManager {

    /**
     * Stores new pot into database. Id for the new pot is automatically
     * generated and stored into id attribute.
     *
     * @param pot pot to be created.
     */
    void createPot(Pot pot) throws PotException;


    /**
     * Updates pot in database.
     *
     * @param pot updated pot to be stored into database.
     */
    void updatePot(Pot pot) throws PotException;

    /**
     * Deletes pot from database.
     *
     * @param pot pot to be deleted from db.
     */
    void deletePot(Pot pot) throws PotException;

    /**
     * Returns list of all pots in the database.
     *
     * @return list of all pots in database.
     */
    List<Pot> findAllPots();

    /**
     * Return pot of given id.
     *
     * @param id given id.
     * @return Pot with given id.
     */
    Pot findPotById(Long id) throws PotException;






}
