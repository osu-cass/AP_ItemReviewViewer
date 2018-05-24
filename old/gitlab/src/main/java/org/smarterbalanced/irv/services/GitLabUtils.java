/**
 * 
 */
package org.smarterbalanced.irv.services;

import org.smarterbalanced.irv.config.SettingsReader;
import org.springframework.util.StringUtils;

/**
 * @author kthotti
 *
 */
public class GitLabUtils {

	/**
	 * 
	 */
	public GitLabUtils() {
		// TODO Auto-generated constructor stub
	}
	
	
	public static String getGitLabItemUrl(String itemName) {
    	try {
    		String[] parts = itemName.split("-");
    		
    		String versionNumber = ""; 
    		if(parts.length > 3)
    			versionNumber = parts[3];
    		
    		String baseItemName = parts[0] + "-" + parts[1] + "-" + parts[2]; 
    		
    		if(StringUtils.isEmpty(versionNumber))
    			return getItemUrl(baseItemName);
    			
    		return getItemCommitUrl(baseItemName, versionNumber);
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
    	return "";
    }

	
    public static String getItemUrl(String itemNumber) {
		return getGitLabUrl() + itemNumber + "/repository/archive.zip?private_token=" + getPrivateToken();
    }
    
    public static String getItemCommitUrl(String itemName, String commitId) {
		return getGitLabUrl() + itemName + "/repository/archive.zip?private_token=" + getPrivateToken() + "&sha=" + commitId;

    }

    public static String getItemCommitsUrl(String itemName) {
    	 return getGitLabUrl() + itemName + "/repository/commits?private_token=" + getPrivateToken();
    }

    public static String getItemTagsUrl(String itemName) {
    	return getGitLabUrl() + itemName + "/repository/tags?private_token=" + getPrivateToken();
    }
    
    public static String getGitLabUrl() {
    	return SettingsReader.get("gitlab.url");
    }

    public static String getPrivateToken() {
    	return SettingsReader.get("gitlab.private.token");
    }



}
