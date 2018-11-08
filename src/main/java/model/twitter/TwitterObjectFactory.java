package model.twitter;

import constants.DatasetName;
import model.twitter.*;

/**
 * Creates twitter objects.
 * If an object with a given identifier already exists, it is simply returned, otherwise it is created.
 */
public class TwitterObjectFactory {

    private final Dataset dataset;

    public TwitterObjectFactory(Dataset dataset) {
        this.dataset = dataset;
    }

    public BabelCategoryModel getCategory(String id) {
        assert id != null && !id.equals("");

        if (!dataset.exixstObj(id)) {
            int i = dataset.getNextId(id);
            dataset.babelCategories.put(i, new BabelCategoryModel(i));
        }
        BabelCategoryModel res = dataset.babelCategories.get(dataset.getIntegerId(id));

        assert res != null;
        return res;
    }

    public BabelDomainModel getDomain(String id) {
        assert id != null && !id.equals("");

        if (!dataset.exixstObj(id)) {
            int i = dataset.getNextId(id);
            dataset.babelDomains.put(i, new BabelDomainModel(i));
        }
        BabelDomainModel res = dataset.babelDomains.get(dataset.getIntegerId(id));

        assert res != null;
        return res;
    }

    /**
     * Returns a UserModel with the given id
     *
     * @param id the id of the user
     * @return the specified UserModel
     */
    public UserModel getUser(String id, DatasetName dn) {
        assert id != null && !id.equals("");

        if (!dataset.exixstObj(id)) {
            int i = dataset.getNextId(id);
            dataset.users.put(i, new UserModel(i, dn));
        }
        UserModel res = dataset.users.get(dataset.getIntegerId(id));

        assert res != null;
        return res;
    }

    public UserModel getUser(int i, DatasetName dn) {
        return getUser(dataset.getStringId(i), dn);
    }

    /**
     * Returns a TweetModel with the given id
     *
     * @param id the id of the tweet
     * @return the specified TweetModel
     */
    public TweetModel getTweet(String id) {
        assert id != null && !id.equals("");

        if (!dataset.exixstObj(id)) {
            int i = dataset.getNextId(id);
            dataset.tweets.put(i, new TweetModel(i));
        }
        TweetModel res = dataset.tweets.get(dataset.getIntegerId(id));

        assert res != null;
        return res;
    }

    /**
     * Returns a InterestModel with the given id
     *
     * @param id the id of the interest
     * @return the specified InterestModel
     */
    public InterestModel getInterest(String id) {
        assert id != null && !id.equals("");

        if (!dataset.exixstObj(id)) {
            int i = dataset.getNextId(id);
            dataset.interests.put(i, new InterestModel(i));
        }
        InterestModel res = dataset.interests.get(dataset.getIntegerId(id));

        assert res != null;
        return res;
    }

    /**
     * Returns a WikiPageModel with the given name
     *
     * @param name the name of the wikipedia page (not the url)
     * @return the specified WikiPageModel
     */
    public  WikiPageModel getWikiPage(String name) {
        assert name != null && !name.equals("");

        if (!dataset.exixstObj(name)) {
            int i = dataset.getNextId(name);
            dataset.wikiPages.put(i, new WikiPageModel(i, name));
        }
        WikiPageModel res = dataset.wikiPages.get(dataset.getIntegerId(name));

        assert res != null;
        return res;
    }

    /**
     * Return the static field dataset
     * @return a dataset
     */
    public Dataset getDataset(){return dataset;}
}
