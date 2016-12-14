import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


class Player {

    private int[] lGuess;
    private ArrayList<HMM>[] birdHMMs;
    private HMM[] models;
    private int correctGuesses;
    private int failedGuesses;
    
	public Player() {
		birdHMMs = new ArrayList[Constants.COUNT_SPECIES];
    	models = new HMM[Constants.COUNT_SPECIES];
    	initialize(models);
    	for(int i = 0; i < Constants.COUNT_SPECIES;i++){
    		//System.err.println(i);
    		birdHMMs[i]=new ArrayList();
    		birdHMMs[i].add(models[i]);
    	}
    	correctGuesses = 0;
    	failedGuesses = 0;
		
    	
    } 
    /**
     * Shoot!
     *
     * This is the function where you start your work.
     *
     * You will receive a variable pState, which contains information about all
     * birds, both dead and alive. Each bird contains all past moves.
     *
     * The state also contains the scores for all players and the number of
     * time steps elapsed since the last time this function was called.
     *
     * @param pState the GameState object with observations etc
     * @param pDue time before which we must have returned
     * @return the prediction of a bird we want to shoot at, or cDontShoot to pass
     */
    
    public Action shoot(GameState pState, Deadline pDue) {
    	

    	//int tid
    	/*
         * Here you should write your clever algorithms to get the best action.
         * This skeleton never shoots.
         */

        // This line chooses not to shoot.
        //return cDontShoot;

        // This line would predict that bird 0 will move right and shoot at it.
    	
        // return new Action(0, Constants.MOVE_RIGHT);
    	
    	//final int numberOfBirds = pState.getNumBirds();
    	
    	
    	
    	
    	return new Action(-1,-1);
    
    }

    /**
     * Guess the species!
     * This function will be called at the end of each round, to give you
     * a chance to identify the species of the birds for extra points.
     *
     * Fill the vector with guesses for the all birds.
     * Use SPECIES_UNKNOWN to avoid guessing.
     *
     * @param pState the GameState object with observations etc
     * @param pDue time before which we must have returned
     * @return a vector with guesses for all the birds
     */
    public int[] guess(GameState pState, Deadline pDue) {
        /*
         * Here you should write your clever algorithms to guess the species of
         * each bird. This skeleton makes no guesses, better safe than sorry!
         */
    	// kan bli fel här pågrund av null
    	
    	int[] obs = null;
    	//lGuess = null;
    	
		
        lGuess = new int[pState.getNumBirds()];
        Random rand = new Random();
        
    	if(pState.getRound() == 0){
    		for (int i = 0; i < pState.getNumBirds(); ++i){
    			lGuess[i] = rand.nextInt(Constants.COUNT_SPECIES-1);
    		}
    	}
    		

    	else{
    		for (int i = 0; i < pState.getNumBirds(); ++i){
    			obs = Observations(pState.getBird(i));
    			//lGuess[i] = Identification(obs);
    			if(obs.length == 0){
    			lGuess[i] = rand.nextInt(Constants.COUNT_SPECIES-1);
    			} else {
        			lGuess[i] = Identification(obs);
        			if(0>lGuess[i] || lGuess[i] > 5){
        			System.err.println(lGuess[i]);}
        			    			}
    		}
            	//Constants.SPECIES_UNKNOWN;
    	}
        return lGuess;
    }
    
    public int[] Observations(Bird bird){
    	
    	int i=0;
    	int[] obsSeq = null;
    	
    	

    	
    	
    	if(bird.isAlive()){
    		obsSeq = new int[bird.getSeqLength()];
    		for(int j = 0; j<obsSeq.length; j++){obsSeq[j]=bird.getObservation(j);}
    			
    	}
    	
    	else{
    		ArrayList<Integer> aliveObservations = new ArrayList<Integer>();
    		for(i = 0; i < bird.getSeqLength(); i++){
    			if(bird.wasAlive(i)){aliveObservations.add(bird.getObservation(i));}
    		}
    		obsSeq = new int[aliveObservations.size()];
    		for(int j = 0;j < obsSeq.length;j++){
    			obsSeq[j] = aliveObservations.get(j);
    		}
    	/*while((bird.wasAlive(i) == true) && (bird.getLastObservation() != bird.getObservation(i))) {
    	
    
    		i++;
    	} 
    	i++;
    	obsSeq = new int[i];
    	for(int j = 0; j<i; j++){
    		obsSeq[j] = bird.getObservation(j);
    	}
    	*/
    	}
    	
    	return obsSeq;
    }
    // if bird cannot be identified will Id be set to unknown
    public int Identification(int[] Observation){
    	int Id,i;
    	double prob, max;
    	Iterator<HMM> bM;
    	Random rand = new Random();
    	Id = rand.nextInt(Constants.COUNT_SPECIES-1);
    	//HMM[] models = new HMM[Constants.COUNT_SPECIES];
    	max = 0.0;
    	// System.err.println(Observation.length+2);
    	for(i=0; i<birdHMMs.length;i++){
    		//System.err.println(i);
    		bM = birdHMMs[i].iterator();
    		while(bM.hasNext()){
	    	prob = bM.next().estimateProbabilityOfEmissionSequence(Observation);
	    	//if(prob > 0.0){ System.err.println(prob);}
	    		if(prob>max){
	    			max = prob;
	    			Id = i;
		    	}
    		} //System.err.println("new");
    	}
    	//System.err.println(Id);
    	return Id;
    }
    /**
     * If you hit the bird you were trying to shoot, you will be notified
     * through this function.
     *
     * @param pState the GameState object with observations etc
     * @param pBird the bird you hit
     * @param pDue time before which we must have returned
     */
    public void hit(GameState pState, int pBird, Deadline pDue) {
        System.err.println("HIT BIRD!!!");
    }

    /**
     * If you made any guesses, you will find out the true species of those
     * birds through this function.
     *
     * @param pState the GameState object with observations etc
     * @param pSpecies the vector with species
     * @param pDue time before which we must have returned
     */
    public void reveal(GameState pState, int[] pSpecies, Deadline pDue) {
    	for(int i=0; i<pSpecies.length;i++){
    		if(pSpecies[i] != Constants.SPECIES_UNKNOWN){
    			HMM model = new HMM(Constants.COUNT_SPECIES,Constants.COUNT_MOVE);		
    			train(model,pState.getBird(i));
    			birdHMMs[pSpecies[i]].add(model);
    			
    			if(lGuess[i] == pSpecies[i]){correctGuesses++;} else {failedGuesses++;}
    		}
    	}
    	System.err.println("Guess statistics, correct guesses " + correctGuesses + " failed guesses " + failedGuesses + " guess rate " + (new Double(correctGuesses)/(failedGuesses + correctGuesses)));
    }
    
    public void initialize(HMM[]models){
    	for(int i=0;i<models.length;i++){
    		models[i] = new HMM(Constants.COUNT_SPECIES,Constants.COUNT_MOVE);
    		
    	}
    
    }
    
    
    public void train(HMM model, Bird bird){
    
    	int[] obs;
    	obs = Observations(bird);
    	model.estimateModel(obs);
    	
    	
    	
    	
    }
    public static final Action cDontShoot = new Action(-1, -1);
}
