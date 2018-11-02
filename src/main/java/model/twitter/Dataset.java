package model.twitter;


import constants.DatasetName;
import io.Config;
import model.ObjectCollection;
import model.ObjectModel;
import model.clusters.Cluster;
import utils.Counter;
import utils.IndexedSerializable;
import utils.Statistics;

import java.util.*;
import java.util.stream.Collectors;

public class Dataset extends ObjectCollection {
    private DatasetName name;
    private Config.Dimension dimension;

    HashMap<Integer, InterestModel> interests;
    HashMap<Integer, TweetModel> tweets;
    HashMap<Integer, UserModel> users;
    HashMap<Integer, WikiPageModel> wikiPages;
    HashMap<Integer, BabelCategoryModel> babelCategories;
    HashMap<Integer, BabelDomainModel> babelDomains;

    @Override
    public String toString() {
        return String.format("(name: %s, users: %s, tweets: %s, interest: %s, wikiPages: %s)",
                name, users.size(), tweets.size(), interests.size(), wikiPages.size());
    }

    public Dataset(DatasetName name, Config.Dimension limit) {
        this.name = name;
        this.dimension = limit;

        interests = new HashMap<>();
        tweets = new HashMap<>();
        users = new HashMap<>();
        wikiPages = new HashMap<>();
        babelCategories = new HashMap<>();
        babelDomains = new HashMap<>();

    }

    public DatasetName getName() {
        return name;
    }


    public InterestModel getInterest(Integer id) {
        assert interests.containsKey(id);

        return interests.get(id);
    }

    public TweetModel getTweet(Integer id) {
        assert tweets.containsKey(id);

        return tweets.get(id);
    }

    public UserModel getUser(Integer id) {
        assert users.containsKey(id);

        return users.get(id);
    }

    public BabelCategoryModel getCategory(Integer id) {
        assert babelCategories.containsKey(id);

        return babelCategories.get(id);
    }

    public BabelDomainModel getDomain(Integer id) {
        assert babelDomains.containsKey(id);

        return babelDomains.get(id);
    }

    public WikiPageModel getPage(Integer id) {
        assert wikiPages.containsKey(id);

        return wikiPages.get(id);
    }

    public Map<Integer, InterestModel> getInterests() {
        return interests;
    }

    public Map<Integer, TweetModel> getTweets() {
        return tweets;
    }

    public Map<Integer, UserModel> getUsers() {
        return users;
    }

    public Map<Integer, WikiPageModel> getWikiPages() {
        return wikiPages;
    }

    public HashMap<Integer, BabelCategoryModel> getBabelCategories() {
        return babelCategories;
    }

    public HashMap<Integer, BabelDomainModel> getBabelDomains() {
        return babelDomains;
    }

    public void addInterest(InterestModel interest) {
        interests.putIfAbsent(interest.getId(), interest);
    }

    public void addTweet(TweetModel tw) {
        tweets.putIfAbsent(tw.getId(), tw);
    }

    public void addUser(UserModel usr) {
        users.putIfAbsent(usr.getId(), usr);
    }

    public void addWikiPage(WikiPageModel pg) {
        wikiPages.putIfAbsent(pg.getId(), pg);
    }

    public void addCategory(BabelCategoryModel categoryModel) {
        babelCategories.putIfAbsent(categoryModel.getId(), categoryModel);
    }

    public void addDomain(BabelDomainModel domainModel) {
        babelDomains.putIfAbsent(domainModel.getId(), domainModel);
    }

// TODO: 02/11/18 cannot remove things, or id becomes not coherent.
//    public void removeInterest(InterestModel interest) {
//        assert interests.containsKey(interest.getId());
//        interests.remove(interest.getId());
//    }
//
//    public void removeTweet(TweetModel tw) {
//        assert tweets.containsKey(tw.getId());
//        tweets.remove(tw.getId());
//    }
//
//    public void removeUser(UserModel usr) {
//        assert users.containsKey(usr.getId());
//        users.remove(usr.getId());
//    }
//
//    public void removePage(WikiPageModel pg) {
//        assert wikiPages.containsKey(pg.getId());
//        wikiPages.remove(pg.getId());
//    }

    public Config.Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Config.Dimension dimension) {
        this.dimension = dimension;
    }

    public String report() {
        return String.format("%s\n\n%s\n\n%s\n\n%s\n\n%s\n\n%s\n\n%s\n\n%s\n\n%s",
                stats(),
                tweeetStats(),
                friendStats(),
                followInStats(),
                followOutStats(),
                categoriesStats(),
                domainsStats(),
                categoriesDistribution(10),
                domainsDistribution(10));
    }

    public String stats(){
        return String.format("[STATS DATASET %s]\n" +
                        "\tunique number of users: %s\n" +
                        "\tunique number of tweets: %s\n" +
                        "\tunique number of interests: %s\n" +
                        "\tunique number of wikipages: %s\n" +
                        "\tunique number of categories: %s\n" +
                        "\tunique number of domains: %s\n",
                name.name().toUpperCase(),
                users.size(), tweets.size(), interests.size(),
                wikiPages.size(), babelCategories.size(), babelDomains.size());
    }

    public String tweeetStats() {
        double[] tweets_sizes = users.values().stream().mapToDouble(x -> x.getTweetsIds().size()).toArray();
        Statistics stat = new Statistics(tweets_sizes);

        return stat.report(
                        "TWEETS STATS",
                        "total number of tweets",
                        "total number of users",
                        "greatest #tweets per user",
                        "#greatest #tweets per users",
                        "smallest #tweets per user",
                        "#smallest #tweets per user",
                        "median #tweets per user",
                        "#median #tweets per users",
                        "mean #tweets per user",
                        "#tweets per user variance",
                        "#tweets per user stddev"
                );
    }

    public String friendStats() {
        double[] in_sizes = users.values().stream().mapToDouble(x -> {
            HashSet<Integer> s = new HashSet<>(x.getFollowInIds());
            s.addAll(x.getFollowOutIds());
            return s.size();
        }).toArray();
        Statistics stat = new Statistics(in_sizes);

        return stat.report("FRIEND (IN/OUT) STATS",
                "total number of friends",
                "total number of users",
                "greatest #friends per user",
                "#greatest #friends per users",
                "smallest #friends per user",
                "#smallest #friends per user",
                "median #friends per user",
                "#median #friends per users",
                "mean #friends per user",
                "#friends per user variance",
                "#friends per user stddev");
    }

    public String followInStats() {
        double[] in_sizes = users.values().stream().mapToDouble(x -> x.getFollowInIds().size()).toArray();
        Statistics stat = new Statistics(in_sizes);

        return stat.report("FOLLOW-IN STATS",
                "total number of followIn",
                "total number of users",
                "greatest #followIn per user",
                "#greatest #followIn per users",
                "smallest #followIn per user",
                "#smallest #followIn per user",
                "median #followIn per user",
                "#median #followIn per users",
                "mean #followIn per user",
                "#followIn per user variance",
                "#followIn per user stddev");
    }

    public String followOutStats() {
        double[] in_sizes = users.values().stream().mapToDouble(x -> x.getFollowOutIds().size()).toArray();
        Statistics stat = new Statistics(in_sizes);

        return stat.report("FOLLOW-OUT STATS",
                "total number of followOut",
                "total number of users",
                "greatest #followOut per user",
                "#greatest #followOut per users",
                "smallest #followOut per user",
                "#smallest #followOut per user",
                "median #followOut per user",
                "#median #followOut per users",
                "mean #followOut per user",
                "#followOut per user variance",
                "#followOut per user stddev");
    }

    public String categoriesStats() {
        StringBuilder s = new StringBuilder();

        double[] catUsers = users.values().stream().mapToDouble(x ->
                x.getTweetsCategories(tweets, interests, wikiPages, babelCategories).size()).toArray();
        Statistics stat = new Statistics(catUsers);
        s.append(stat.report(
                "CATEGORIES STATS PER USER",
                "total number of categories in users",
                "total number of users",
                "greatest #categories per user",
                "#greatest #categories per users",
                "smallest #categories per user",
                "#smallest #categories per user",
                "median #categories per user",
                "#median #categories per users",
                "mean #categories per user",
                "#categories per user variance",
                "#categories per user stddev"));

        s.append("\n");

        double[] catTweet = tweets.values().stream().mapToDouble(x ->
                x.getWikiPageModel(interests, wikiPages).getBabelCategories().size()).toArray();
        stat = new Statistics(catTweet);
        s.append(stat.report(
                "CATEGORIES STATS PER TWEET",
                "total number of categories in tweets",
                "total number of tweets",
                "greatest #categories per tweet",
                "#greatest #categories per tweets",
                "smallest #categories per tweet",
                "#smallest #categories per tweet",
                "median #categories per tweet",
                "#median #categories per tweets",
                "mean #categories per tweet",
                "#categories per tweet variance",
                "#categories per tweet stddev"));
        return s.toString();
    }

    public String domainsStats() {

        StringBuilder s = new StringBuilder();

        double[] domUsers = users.values().stream().mapToDouble(x ->
                x.getTweetsDomains(tweets, interests, wikiPages, getBabelDomains()).size()).toArray();
        Statistics stat = new Statistics(domUsers);
        s.append(stat.report(
                "DOMAINS STATS PER USER",
                "total number of domains in users",
                "total number of users",
                "greatest #domains per tweet",
                "#greatest #domains per tweet",
                "smallest #domains per tweet",
                "#smallest #domains per tweet",
                "median #domains per tweet",
                "#median #domains per tweet",
                "mean #domains per tweet",
                "#domains per user variance",
                "#domains per user stddev"));

        s.append("\n");

        double[] domTweet = tweets.values().stream().mapToDouble(x ->
                x.getWikiPageModel(interests, wikiPages).getBabelDomains().size()).toArray();
        stat = new Statistics(domTweet);
        s.append(stat.report(
                "DOMAINS STATS PER TWEET",
                "total number of domains in tweets",
                "total number of tweets",
                "greatest #domains per tweet",
                "#greatest #domains per tweets",
                "smallest #domains per tweet",
                "#smallest #domains per tweet",
                "median #domains per tweet",
                "#median #domains per tweets",
                "mean #domains per tweet",
                "#domains per tweet variance",
                "#domains per tweet stddev"));
        return s.toString();
    }

    public String categoriesDistribution(int k) {
        Counter<String> catCounter = new Counter<>();

        for (WikiPageModel page : wikiPages.values()) {
            catCounter.increment(page.getCategoriesModel(babelCategories).stream().map(ObjectModel::getIdString).collect(Collectors.toSet()));
        }

        return String.format("[CATEGORIES DISTRIBUTION]\n%s\n", catCounter.getDistribution(k));
    }

    public String domainsDistribution(int k) {
        Counter<String> catCounter = new Counter<>();

        for (WikiPageModel page : wikiPages.values()) {
            catCounter.increment(page.getDomainsModel(babelDomains).stream().map(ObjectModel::getIdString).collect(Collectors.toSet()));
        }

        return String.format("[DOMAINS DISTRIBUTION]\n%s\n", catCounter.getDistribution(k));
    }
}