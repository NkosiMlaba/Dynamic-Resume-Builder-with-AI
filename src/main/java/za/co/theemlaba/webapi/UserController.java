package za.co.theemlaba.webapi;

import java.util.HashMap;
import java.util.Map;

import za.co.theemlaba.database.*;
import za.co.theemlaba.domain.models.*;
import za.co.theemlaba.domain.resumeGenerator.*;

public class UserController {
    private UserManager database = new UserManager();
    private Llama3Request request = new Llama3Request();
    private GenerateResume generator = new GenerateResume();
    private GenerateCoverLetter generatorCoverLetter = new GenerateCoverLetter();
    private String defaultDocType = ".docx";


    public UserController() {
        
    }


    /**
     * Handles a command by creating a new command object, executing it, and returning the response as a JSON string.
     *
     * @param request The command to be executed.
     * @return The response to the command as a JSON string.
     */
    public String handleCommand(String request) {
        String responseJsonString = "";
        try {
            return responseJsonString;
        } catch (IllegalArgumentException e) {
            return responseJsonString;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String handleStoreResumeData(Map<String, String> receivedData) {
        try {
            String resume = receivedData.get("resume");
            String email = receivedData.get("email");
            database.updateUserResume(email, resume);
            return email;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String authenticateUser(Map<String, String> receivedData) {
        try {
            if (database.isExistingUser(receivedData.get("email"))) {
                String password = database.fetchPassword(receivedData.get("email"));
                if (password.equals(receivedData.get("password"))) {
                    return receivedData.get("email");
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String registerUser (Map<String, String> receivedData) {
        try {
            if (!receivedData.get("password").equals(receivedData.get("confirmpassword"))) {
                return null;
            } else if (database.isExistingUser(receivedData.get("email"))) {
                return null;
            } else {
                database.storeUser(receivedData.get("firstname"), receivedData.get("lastname"), receivedData.get("email"), receivedData.get("password"));
                database.createEntries(receivedData.get("email"), defaultDocType);
                return receivedData.get("email");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String generateResumeAsString (String existingResume, String jobDescription) {
        return request.generateCVFromLlama(existingResume, jobDescription);
    }

    public void generateResumeAsDocument (String email, String stringCV) {
        generator.generateCV(email, stringCV);
    }

    public String generateCoverLetterAsString (String existingResume, String jobDescription, boolean includesDescription) {
        return request.generateCoverLetterFromLlama(existingResume, jobDescription, includesDescription);
    }

    public void generateCoverLetterAsDocument (String email, String stringCoverLetter) {
        generatorCoverLetter.generateCoverLetter(email, stringCoverLetter);
    }

    public String handleJobDescription (Map<String, String> receivedData) {
        try {
            String jobDescription = receivedData.get("jobdescription");
            jobDescription = cleanData(jobDescription);
            String email = receivedData.get("email");
            String existingResume = database.fetchUserResume(email);
            existingResume = cleanData(existingResume);
            String stringCV = generateResumeAsString(existingResume, jobDescription);
            generateResumeAsDocument(email, stringCV);
            database.updateUserJobDescription(email, jobDescription);
            return email;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String handleCoverLetterDescription (Map<String, String> receivedData) {
        try {
            String coverLetterDescription = receivedData.get("coveletterdescription");
            coverLetterDescription = cleanData(coverLetterDescription);
            String email = receivedData.get("email");
            String existingResume = database.fetchUserResume(email);
            existingResume = cleanData(existingResume);
            String stringCoverLetter = generateCoverLetterAsString(existingResume, coverLetterDescription, true);
            generateCoverLetterAsDocument(email, stringCoverLetter);
            return email;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String updatePreferredFormat (String email, String formatData) {
        switch (formatData) {
            case "docx":
                database.updateUserDocTypePreference(email, ".docx");
                break;
            case "pdf":
                database.updateUserDocTypePreference(email, ".pdf");
                break;
            case "txt":
                database.updateUserDocTypePreference(email, ".txt");
                break;
            default:
                break;
        }
        return null;
    }

    public static String cleanData(String data) {
        data = data.replace("\r", " ");
        data = data.replaceAll("\\n{2,}", " ");
        data = data.replace("\"", " ")
                   .replace("\\", " ")
                   .replace("\n", " ");
        return data;
    }
    

    public String getResumeFilePath(String email) {
        String preferrefFormat = database.fetchUserDocTypePreference(email);
        return "src/main/resources/resumes/" + email + "/resume" + preferrefFormat;
    }

    public String getCoverLetterFilePath(String email) {
        String preferrefFormat = database.fetchUserDocTypePreference(email);
        return "src/main/resources/resumes/" + email + "/coverletter" + preferrefFormat;
    }

    public String regenerateResume (String email) {
        String existingResume = database.fetchUserResume(email);
        existingResume = cleanData(existingResume);
        String jobDescription = database.fetchUserJobDescription(email);
        String stringCV = generateResumeAsString(existingResume, jobDescription);
        generateResumeAsDocument(email, stringCV);
        return email;
    }

    public String generateCoverLetter (String email) {
        String existingResume = database.fetchUserResume(email);
        existingResume = cleanData(existingResume);
        String stringCV = generateCoverLetterAsString(existingResume, "", false);
        generateCoverLetterAsDocument(email, stringCV);
        return email;
    }

    public String generateCoverLetterFromDescription (String email) {
        String existingResume = database.fetchUserResume(email);
        String coverLetterDescription = database.fetchUserJobDescription(email);
        existingResume = cleanData(existingResume);
        coverLetterDescription = cleanData(coverLetterDescription);
        String stringCV = generateCoverLetterAsString(existingResume, coverLetterDescription, true);
        generateCoverLetterAsDocument(email, stringCV);
        return email;
    }

    public Map<String, Object> hasLastJobDescription (String email) {
        Map<String, Object> model = new HashMap<>();
        
        if (database.hasExistingJobDescription(email)) {
            String lastJobDescription = database.fetchUserJobDescription(email);
            model.put("hasLastJobDescription", lastJobDescription!= null);
            model.put("lastJobDescription", lastJobDescription);
        } else {
            model.put("hasLastJobDescription", false);
            model.put("lastJobDescription", null);
        }
        
        return model;
    }
    

    public Map<String, Object> hasLastResume (String email) {
        Map<String, Object> model = new HashMap<>();
        
        if (database.hasExistingResume(email)) {
            String lastResume = database.fetchUserResume(email);
            model.put("hasResume", lastResume!= null);
            model.put("lastResume", lastResume);
        } else {
            model.put("hasResume", false);
            model.put("lastResume", null);
        }
        
        return model;
    }
}