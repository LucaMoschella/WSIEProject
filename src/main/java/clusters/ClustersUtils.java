package clusters;

import twittermodel.Dataset;
import twittermodel.TweetModel;
import twittermodel.UserModel;
import twittermodel.WikiPageModel;
import utils.Counter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ClustersUtils {
    private ClustersUtils() {
    }


    /**
     * Associa ad ogni utente il counter delle categorie che gli piacciono
     *
     * @param pageToCat: may be synToCategory or synToDomain from the wikimapping
     */
    public static Map<UserModel, Counter<String>> getUserToCatCounter(Dataset dataset, Map<Integer, Set<String>> pageToCat) {
        HashMap<UserModel, Counter<String>> userTocatCounter = new HashMap<>();
        System.out.println(dataset);
        for (UserModel user : dataset.getUsers().values()) {
            for (Integer tweetID : user.getTweetsIds()) {
                TweetModel tweet = user.getTweetModel(dataset.getTweets(), tweetID);
                WikiPageModel page = tweet.getWikiPageModel(dataset.getInterests(), dataset.getWikiPages());
                if (page == null) continue; // il synset non aveva associato niente

                Set<String> possibleClusters = pageToCat.get(page.getId());
                if (possibleClusters == null) {continue;}

                userTocatCounter.putIfAbsent(user, new Counter<>());
                userTocatCounter.get(user).increment(possibleClusters);
            }
        }
        return userTocatCounter;
    }

}
