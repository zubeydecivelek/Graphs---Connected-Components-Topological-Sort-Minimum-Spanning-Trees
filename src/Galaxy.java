import java.util.*;

public class Galaxy {

    private final List<Planet> planets;
    private List<SolarSystem> solarSystems;

    private SolarSystem solarSystem = new SolarSystem();

    public Galaxy(List<Planet> planets) {
        this.planets = planets;
    }

    /**
     * Using the galaxy's list of Planet objects, explores all the solar systems in the galaxy.
     * Saves the result to the solarSystems instance variable and returns a shallow copy of it.
     *
     * @return List of SolarSystem objects.
     */
    public List<SolarSystem> exploreSolarSystems() {
        solarSystems = new ArrayList<>();
        // TODO: YOUR CODE HERE
        if (planets.size()>0) {
            findSolarSystems(planets.get(0));
            solarSystems.add(solarSystem);
            for (Planet planet:planets){
                if (!solarSystem.hasPlanet(planet.getId()) && planet.getNeighbors().size()>0 && contains(planet.getNeighbors()))
                    solarSystem.addPlanet(planet);
                else if(!solarSystem.hasPlanet(planet.getId())){
                    solarSystem = new SolarSystem();
                    findSolarSystems(planet);
                    solarSystems.add(solarSystem);

                }
            }
        }
        return solarSystems;
    }

    public boolean contains(List<String> neighbors){
        for (String id:neighbors)
            for (SolarSystem solarSystem:solarSystems)
                for (Planet planet: solarSystem.getPlanets())
                    if(planet.getId().equals(id))
                        return true;

        return false;
    }

    public void findSolarSystems(Planet planet){
        solarSystem.addPlanet(planet);
        for (String neighborID:planet.getNeighbors()){
            if (!solarSystem.hasPlanet(neighborID)){
                findSolarSystems(findPlanet(neighborID));;
            }
        }

    }

    public Planet findPlanet(String id){
        for (Planet planet:planets)
            if (planet.getId().equals(id))
                return planet;
        return null;
    }

    public List<SolarSystem> getSolarSystems() {
        return solarSystems;
    }

    // FOR TESTING
    public List<Planet> getPlanets() {
        return planets;
    }

    // FOR TESTING
    public int getSolarSystemIndexByPlanetID(String planetId) {
        for (int i = 0; i < solarSystems.size(); i++) {
            SolarSystem solarSystem = solarSystems.get(i);
            if (solarSystem.hasPlanet(planetId)) {
                return i;
            }
        }
        return -1;
    }

    public void printSolarSystems(List<SolarSystem> solarSystems) {
        System.out.printf("%d solar systems have been discovered.%n", solarSystems.size());
        for (int i = 0; i < solarSystems.size(); i++) {
            SolarSystem solarSystem = solarSystems.get(i);
            List<Planet> planets = new ArrayList<>(solarSystem.getPlanets());
            Collections.sort(planets);
            System.out.printf("Planets in Solar System %d: %s", i + 1, planets);
            System.out.println();
        }
    }
}

