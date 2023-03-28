package main.java.com.bdreiss.dataAPI;
/**
 * Represents the intensity of causes, symptoms and remedies.
 * 
 * <p>These enum of course only represent subjective measures and should be treated as such.</p>
 * 
 * @author Bernd Reiß bd_reiss@gmx.at
 */

public enum Intensity {
	/**
	 * Choose if intensity is not important
	 */
    NO_INTENSITY("No intensity"), 
    /**
     * Choose if intensity is low
     */
      LOW("low intensity"), 
      /**
       * Choose if intensity is medium
       */
      MEDIUM("medium intensity"), 
      /**
       * Choose if intensity is high
       */
      HIGH("high intensity");
    
	private String description;
	private Intensity(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return description;
	}
	
}
