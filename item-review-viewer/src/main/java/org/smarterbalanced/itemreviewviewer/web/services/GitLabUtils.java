package org.smarterbalanced.itemreviewviewer.web.services;

import com.amazonaws.services.ecs.model.KeyValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarterbalanced.itemreviewviewer.web.config.ItemBankConfig;
import org.apache.commons.lang.StringUtils;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class GitLabUtils {
    private static final Logger _logger = LoggerFactory.getLogger(GitLabUtils.class);

    // NOTE: This is hardcoded namespaces which do not have bankKey in Gitlab
    //       Remove this once IRiS can work with namespaces
    public static Hashtable<String, Integer> noBankKeyNamespaceHash = new Hashtable<String, Integer>() {{
        put("iat-development", 990);
        put("iat-staging", 991);
        put("iat-uat", 992);
    }};

    public static String namespaceToBankId(String namespace){
        int bankKey = (int) noBankKeyNamespaceHash.get(namespace);
        return Integer.toString(bankKey);
    }

    public static String bankKeyToNameSpace(String bankKey){
        int key = Integer.parseInt(bankKey);
        for(Object o:noBankKeyNamespaceHash.entrySet()){
            Map.Entry entry = (Map.Entry) o;
            if(entry.getValue() == key){
                return (String) entry.getKey();
            }
        }
        return null;
    }
    public static String getBankKeyByNamespace(String namespace) {
        if (GitLabUtils.noBankKeyNamespaceHash.containsKey(namespace)) {
            return String.valueOf(GitLabUtils.noBankKeyNamespaceHash.get(namespace));
        }

        return null;
    }

    //forms valid name for an item.
    public static String makeItemId(String bankKey, String itemKey){
        String itemId = null;
        if(StringUtils.isNotEmpty(bankKey)){
            itemId = "item-" + bankKey + "-" + itemKey;
        } else {
            itemId = itemKey;
        }
        return itemId;
    }

    //forms a stim id.
    public static String makeStimId(String bankKey, String itemKey){
        return "stim-" + bankKey + "-" + itemKey;
    }

    //takes a valid item id and makes a qualified item ID
    public static String makeQualifiedItemId(String itemId, String version){
        itemId = itemId.toLowerCase();
        if(itemId.contains("item-")){
            itemId = itemId.replace("item-",  "");
        }
        String qualifiedItemId = "i-" + itemId;
        if(StringUtils.isNotEmpty(version)){
            qualifiedItemId = qualifiedItemId + "-" + version;
        }
        return qualifiedItemId;
    }

    //forms valid name for an item directory.
    public static String makeDirId(String bankKey, String itemKey){
        return "Item-" + bankKey + "-" + itemKey;
    }

    public static String getGitLabItemUrl(String namespace, String itemName) {
        try {
            String[] parts = itemName.split("-");

            String versionNumber = "";
            if(parts.length > 3)
                versionNumber = parts[3];

            String baseItemName = parts[0] + "-" + parts[1] + "-" + parts[2] ;

            if(StringUtils.isEmpty(versionNumber))
                return _getItemUrl(namespace, baseItemName);

            return _getItemCommitUrl(namespace, baseItemName, versionNumber);
        } catch (Exception e) {
            _logger.error("Fail to get a GitLab item Url");
            throw new GitLabException(e);
        }
    }

    public static String extractItemId(String namespace, String itemName) {
        String[] parts = itemName.split("-");
        if (parts.length > 2 && noBankKeyNamespaceHash.containsKey(namespace)) {
            return parts[2];
        }
        return itemName;
    }


    private static String _getItemUrl(String namespace, String itemNumber) {
        itemNumber = extractItemId(namespace, itemNumber);
        return _getGitLabIrvUrl(namespace) + itemNumber + "/repository/archive.zip?private_token=" + _getPrivateToken();
    }

    private static String _getItemCommitUrl(String namespace, String itemName, String commitId) {
        return _getGitLabIrvUrl(namespace) + itemName + "/repository/archive.zip?private_token=" + _getPrivateToken() + "&sha=" + commitId;
    }

    public static String getItemCommitsUrl(String namespace, String itemNumber) {
        itemNumber = extractItemId(namespace, itemNumber);
        return _getGitLabIrvUrl(namespace) + itemNumber + "/repository/commits?private_token=" + _getPrivateToken();
    }
    /* NOTE: These two functions are not being used, Should they be removed?
    public static String getItemTagsUrl(String itemName) {
        return _getGitLabIrvUrl() + itemName + "/repository/tags?private_token=" + _getPrivateToken();
    }

    public static String getGroupsUrl(){
        return _getGitLabIrvUrl() + "/groups?all_available=true";
    }
    */
    public static String getNamespacesUrl(int pageSize, int page) {
        return _getGitLabBaseUrl() + "/namespaces?private_token=" + _getPrivateToken() + "&per_page=" + pageSize + "&page=" + page;
    }

    public static String getProjectsSearchUrl(String searchKey) {
        return _getGitLabBaseUrl() + "/projects?private_token=" + _getPrivateToken() + "&search=" + searchKey;
    }

    private static String _getGitLabBaseUrl() {
        return ItemBankConfig.get("gitlab.base.url");
    }

    private static String _getGitLabIrvUrl(String namespace) {
        return _getGitLabBaseUrl() + "/projects/" + namespace + "%2F";
    }

    public static String _getPrivateToken() {
        return ItemBankConfig.get("gitlab.private.token");
    }
}
