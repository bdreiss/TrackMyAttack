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
    noIntensity, 
    /**
     * Choose if intensity is low
     */
      low, 
      /**
       * Choose if intensity is medium
       */
      medium, 
      /**
       * Choose if intensity is high
       */
      high;
     

}
