import java.util.*;

public class SubspaceCommunicationNetwork {

    private List<SolarSystem> solarSystems;
    private List<Planet> maxLevelPlanets;
    private List<HyperChannel> allChannels = new ArrayList<>();

    /**
     * Perform initializations regarding your implementation if necessary
     * @param solarSystems a list of SolarSystem objects
     */
    public SubspaceCommunicationNetwork(List<SolarSystem> solarSystems) {
        // TODO: YOUR CODE HERE
        this.solarSystems = solarSystems;
        maxLevelPlanets = new ArrayList<>();
        for (SolarSystem solarSystem:solarSystems){
            Planet maxLevelPlanet = new Planet("",0,null);
            for(Planet planet:solarSystem.getPlanets()){
                if (maxLevelPlanet.getTechnologyLevel() < planet.getTechnologyLevel())
                    maxLevelPlanet = planet;
            }
            maxLevelPlanets.add(maxLevelPlanet);
        }

        for (int i = 0;i<maxLevelPlanets.size();i++){
            for (int j = i+1;j<maxLevelPlanets.size();j++){
                allChannels.add(new HyperChannel(maxLevelPlanets.get(i), maxLevelPlanets.get(j), calculateCost(maxLevelPlanets.get(i), maxLevelPlanets.get(j))));
               }
        }
    }

    public double calculateCost(Planet planet1, Planet planet2){
        double average = (planet1.getTechnologyLevel() + planet2.getTechnologyLevel()) / 2d;
        return Constants.SUBSPACE_COMMUNICATION_CONSTANT/average;
    }

    /**
     * Using the solar systems of the network, generates a list of HyperChannel objects that constitute the minimum cost communication network.
     * @return A list HyperChannel objects that constitute the minimum cost communication network.
     */
    public List<HyperChannel> getMinimumCostCommunicationNetwork() {
        List<HyperChannel> minimumCostCommunicationNetwork = new ArrayList<>();
        // TODO: YOUR CODE HERE
        Collections.sort(allChannels, Comparator.comparing(HyperChannel::getWeight));

        List<Planet> addedPlanets = new ArrayList<>();
        for (HyperChannel hyperChannel:allChannels){
            if (addedPlanets.contains(hyperChannel.getFrom()) &&addedPlanets.contains(hyperChannel.getTo()))
                continue;
            if (minimumCostCommunicationNetwork.size() == solarSystems.size()-1)
                break;
            addedPlanets.add(hyperChannel.getTo());
            addedPlanets.add(hyperChannel.getFrom());
            minimumCostCommunicationNetwork.add(hyperChannel);


        }
        return minimumCostCommunicationNetwork;
    }


    public void printMinimumCostCommunicationNetwork(List<HyperChannel> network) {
        double sum = 0;
        for (HyperChannel channel : network) {
            Planet[] planets = {channel.getFrom(), channel.getTo()};
            Arrays.sort(planets);
            System.out.printf("Hyperchannel between %s - %s with cost %f", planets[0], planets[1], channel.getWeight());
            System.out.println();
            sum += channel.getWeight();
        }
        System.out.printf("The total cost of the subspace communication network is %f.", sum);
        System.out.println();
    }
}
