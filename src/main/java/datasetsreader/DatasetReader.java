package datasetsreader;
import constants.DatasetInfo;
import constants.DatasetName;
import twittermodel.*;
import io.TsvFileReader;

import java.util.ArrayList;

/**
 * This class use a TsvFileReader to read a dataset and describes it building and managing a Dataset object
 */
public class DatasetReader
{


    /**
     * From a couple userId-fFriendId creates two users and add them in the dataset object
     * @param user_to_friend the couple that should be added to the dataset, where the user follows the friend
     * @param dataset the object that stores all the informations
     */
    static void addRow_friendBased_dataset(ArrayList<String> user_to_friend, Dataset dataset)
    {
        assert user_to_friend.size()==2;

        UserModel user = ModelFactory.getUser(user_to_friend.get(0));
        UserModel followed = ModelFactory.getUser(user_to_friend.get(1));

        user.addFollowOut(followed);

        dataset.addUser(user);
        dataset.addUser(followed);
    }

    /**
     * From a couple userId-wikiPage creates a user and its wikepedia page
     * @param user_to_wikipage the couple of a user and the wikipedia page which represents it
     * @param dataset the object that stores all the informations
     */
    public static void addRow_friendBased_interest(ArrayList<String> user_to_wikipage, Dataset dataset)
    {
        assert user_to_wikipage.size()==2;

        UserModel user = ModelFactory.getUser(user_to_wikipage.get(0));
        WikiPageModel wikiPage = ModelFactory.getWikiPage(user_to_wikipage.get(1));

        user.addWikiPageAboutUser(wikiPage);

        dataset.addPage(wikiPage);
        dataset.addUser(user);
    }

    /**
     * Creates a user that makes a tweet about a specified interest. Note that the interest must already been created
     * @param user_tweet_interest_interestUrl An array of 4 strings which represent: the user id, the tweet id, the interest id and
     *                             the url of the tweet
     * @param dataset the object that stores all the informations
     */
    public static void addRow_messageBased_dataset(ArrayList<String> user_tweet_interest_interestUrl, Dataset dataset)
    {
        assert user_tweet_interest_interestUrl.size() == 4;

        UserModel user = ModelFactory.getUser(user_tweet_interest_interestUrl.get(0));

        InterestModel interest = ModelFactory.getInterest(user_tweet_interest_interestUrl.get(2));

        TweetModel tweet = ModelFactory.getTweet(user_tweet_interest_interestUrl.get(1));
        tweet.setInterestId(interest);
        tweet.setInterestUrl(user_tweet_interest_interestUrl.get(3));

        user.addTweet(tweet);

        dataset.addUser(user);
        dataset.addInterest(interest);
        dataset.addTweet(tweet);
    }

    /**
     * Creates a new interest with the related wikipedia page in the specified dataset
     * @param interest_platform_wikipage An array of 3 strings which represent: the interest id, the type of the platform and
     *                             wikipedia page id
     * @param dataset  the object that stores all the informations
     */
    public static void addRow_messageBased_interest(ArrayList<String> interest_platform_wikipage, Dataset dataset)
    {
        assert interest_platform_wikipage.size() == 3;

        WikiPageModel page = ModelFactory.getWikiPage(interest_platform_wikipage.get(2));

        InterestModel interest = ModelFactory.getInterest(interest_platform_wikipage.get(0));
        interest.setPlatform(InterestModel.PlatformType.fromString(interest_platform_wikipage.get(1)));
        interest.setWikiPageId(page);

        dataset.addPage(page);
        dataset.addInterest(interest);
    }

    /**
     * Creates a new user and add it in the specified dataset
     * @param user the id of the user to add
     * @param dataset the object that stores all the informations
     */
    public static void addRow_S21(ArrayList<String> user, Dataset dataset){
        assert user.size() == 1;

        UserModel u = ModelFactory.getUser(user.get(0));

        dataset.addUser(u);
    }

    /**
     * Creates a new user and a new wikipedia page that the user appreciates.
     * Both of them are added in the specified dataset
     *
     * @param user_to_wikipage the couple of the user id and of the wikipedia page id
     * @param dataset the object that stores all the informations
     */
    public static void addRow_S22_S23(ArrayList<String> user_to_wikipage, Dataset dataset){
        assert user_to_wikipage.size()==2;

        UserModel user = ModelFactory.getUser(user_to_wikipage.get(0));
        WikiPageModel wikiPage = ModelFactory.getWikiPage(user_to_wikipage.get(1));

        user.addWikiPagesOfLikedItemsIds(wikiPage);

        dataset.addUser(user);
        dataset.addPage(wikiPage);
    }

    public static Dataset readDataset(DatasetName name) {
        assert name != null;

        Dataset dataset = new Dataset(name);
        DatasetReader.fillDataset(name, dataset);
        return dataset;
    }

    /**Reads datas from a dataset file using a TsvFileReader
     *
     * @param name the dataset to read
     * @param dataset the datset to fill
     */
    public static void fillDataset(DatasetName name, Dataset dataset)
    {
        assert name != null;
        assert dataset != null;

        // todo: remove magic number
        switch (name) {
            case WIKIMID:
                TsvFileReader.splitByChar(TsvFileReader.readText(DatasetInfo.WIKIMID_MESSAGE_BASED_INTEREST_INFO.getPath())).forEach(
                        s -> addRow_messageBased_interest(s, dataset));
                TsvFileReader.splitByChar(TsvFileReader.readText(DatasetInfo.WIKIMID_MESSAGE_BASED_DATASET.getPath())).forEach(
                        s -> addRow_messageBased_dataset(s, dataset));

                TsvFileReader.splitByChar(TsvFileReader.readText(DatasetInfo.WIKIMID_FRIEND_BASED_DATASET.getPath())).forEach(
                        s -> addRow_friendBased_dataset(s, dataset));
                TsvFileReader.splitByChar(TsvFileReader.readText(DatasetInfo.WIKIMID_FRIEND_BASED_INTEREST_INFO.getPath())).forEach(
                        s -> addRow_friendBased_interest(s, dataset));
                break;
            case S21:
                TsvFileReader.splitByChar(TsvFileReader.readText(DatasetInfo.S21_DATASET.getPath())).forEach(
                        s -> addRow_S21(s, dataset));
                break;
            case S22:
                TsvFileReader.splitByChar(TsvFileReader.readText(DatasetInfo.S22_DATASET.getPath())).forEach(
                        s -> addRow_S22_S23(s, dataset));
                break;
            case S23:
                TsvFileReader.splitByChar(TsvFileReader.readText(DatasetInfo.S23_DATASET.getPath())).forEach(
                        s -> addRow_S22_S23(s, dataset));
                break;
        }
    }
//
//    /**
//     * Stores a list of object taken from
//     * @param dataset
//     * @param readedData
//     * @param datasetConstants the datasetConstant to work upon
//     */
//    public static  void addDataInDataset(Dataset dataset, ArrayList<ArrayList<String>> readedData, DatasetInfo datasetConstants)
//    {
//        switch(datasetConstants.getType()){
//            case FRIENDBASED_DATASET:
//                for (ArrayList<String> userFriend : readedData)
//                {
//                    addRow_friendBased_dataset(userFriend, dataset);
//                }
//                break;
//            case FRIENDBASED_INTEREST:
//                for (ArrayList<String> frienWikiPage : readedData)
//                {
//                    addRow_friendBased_interest(frienWikiPage, dataset);
//                }
//                break;
//            case MESSAGEBASED_DATASET:
//                for (ArrayList<String> userTweetIntURL : readedData)
//                {
//                    addRow_messageBased_dataset(userTweetIntURL, dataset);
//                }
//                break;
//            case MESSAGEBASED_INTEREST:
//                for (ArrayList<String> intPlatPage : readedData)
//                {
//                    addRow_messageBased_interest(intPlatPage, dataset);
//                }
//                break;
//            case S21:
//                for (ArrayList<String> user : readedData)
//                {
//                    addRow_S21(user, dataset);
//                }
//                break;
//            case S22_S23:
//                for (ArrayList<String> userPage : readedData)
//                {
//                    addRow_S22_S23(userPage, dataset);
//                }
//                break;
//        }
//    }
}
