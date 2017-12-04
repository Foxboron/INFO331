package no.uib.info331.util;


/**
 * Validator class for checking that string input fields are used correctly
 *
 * Created by perni on 04/12/2017.
 */

public class TextValidator {

    public Boolean completeCheck(String input){
        if(!isTooLong(input) && !isTooShort(input) && !isEmpty(input)){
            return true;
        }
        return false;
    }

    /**
     * Checks if the input text is too long
     *
     * @param input the actual string
     * @return whether it's too long or not
     */
    public Boolean isTooLong(String input){
        if(!isEmpty(input)){
            if(input.length() > 18){
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the input is too short
     *
     * @param input the actual string
     * @return whether it's too short or not
     */
    public Boolean isTooShort(String input){
        if(!isEmpty(input)){
            if(input.length() > 1){
                return false;
            }
        }
        return true;
    }

    /**
     * Checks it the input string is empty
     *
     * @param input the actual string
     * @return whether it's empty or not
     */
    public Boolean isEmpty(String input) {
        if(input == null || input.equals("") || input.equals(" ")){
            return true;
        }
        return false;
    }
}
