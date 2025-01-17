package babelnet;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.babelnet.BabelSynsetID;
import it.uniroma1.lcl.babelnet.data.BabelCategory;
import it.uniroma1.lcl.babelnet.resources.WikipediaID;
import it.uniroma1.lcl.jlt.util.Language;
import it.uniroma1.lcl.kb.Domain;
import model.twitter.WikiPageModel;
import io.IndexedSerializable;

import java.util.*;
import java.util.stream.Collectors;

public class BabelnetInterface implements IndexedSerializable {
    private BabelnetInterface() {
    }
    public static String getSynset(WikiPageModel pageModel) {
        assert pageModel != null;

        BabelSynset synId = getSynsetObj(pageModel);

        return synId == null ? null : synId.getID().getID();
    }

    public static Set<String> getCategories(WikiPageModel pageModel) {
        assert pageModel != null;
        return getCategories(getSynsetObj(pageModel));
    }

    public static Set<String> getDomains(WikiPageModel pageModel) {
        assert pageModel != null;
        return getDomains(getSynsetObj(pageModel));
    }

    public static Collection<String> getCategories(Collection<String> synsetId) {
        return synsetId.stream().flatMap(x -> getCategories(x).stream()).collect(Collectors.toList());
    }

    public static Set<String> getCategories(String synsetId) {
        return getCategories(getSynsetObj(synsetId));
    }

    public static Collection<String> getDomains(Collection<String> synsetId) {
        return synsetId.stream().flatMap(x -> getDomains(x).stream()).collect(Collectors.toList());
    }

    public static Set<String> getDomains(String synsetId) {
        return getDomains(getSynsetObj(synsetId));
    }


//  caching to improve performances.

//    private static Int2ObjectOpenHashMap<BabelSynset> wikiToSynsetObjCache = new Int2ObjectOpenHashMap<>();
//    private static Object2ObjectOpenHashMap<BabelSynset, Set<String>> synsetToCategoriesObjCache = new Object2ObjectOpenHashMap<>();
//    private static Object2ObjectOpenHashMap<BabelSynset, Set<String>> synsetToDomainObjCache = new Object2ObjectOpenHashMap<>();

    private static BabelSynset getSynsetObj(WikiPageModel wikiPage) {
        assert (wikiPage != null);
//
//        if (wikiToSynsetObjCache.containsKey(wikiPage.getId())) {
//            return wikiToSynsetObjCache.get(wikiPage.getId());
//        }

        BabelSynset synset = BabelNet.getInstance().getSynset(new WikipediaID(wikiPage.getSimpleName(), Language.EN));
//        wikiToSynsetObjCache.put(wikiPage.getId(), synset);

        return synset;
    }

    private static BabelSynset getSynsetObj(String synsetId) {
        BabelSynset syn = BabelNet.getInstance().getSynset(new BabelSynsetID(synsetId));
        return syn;
    }

    private static Set<String> getCategories(BabelSynset synset) {
//        if (synsetToCategoriesObjCache.containsKey(synset)) {
//            return synsetToCategoriesObjCache.get(synset);
//        }

        HashSet<String> catSet = new HashSet<>();

        if (synset != null) {
            Set<BabelCategory> cats = synset.getCategories().stream().filter(x -> x.getLanguage().equals(Language.EN)).collect(Collectors.toSet());
            for (BabelCategory b : cats) {
                catSet.add(b.toString());
            }
        }

//        synsetToCategoriesObjCache.put(synset, catSet);
        return catSet;
    }

    private static Set<String> getDomains(BabelSynset synset) {
//        if (synsetToDomainObjCache.containsKey(synset)) {
//            return synsetToDomainObjCache.get(synset);
//        }

        HashSet<String> domSet = new HashSet<>();

        if (synset != null) {
            Set<Domain> doms = synset.getDomains().keySet();
            for (Domain b : doms) {
                domSet.add(b.toString());
            }
        }

//        synsetToDomainObjCache.put(synset, domSet);
        return domSet;
    }

    public static void main(String[] args) {
        BabelSynset v = BabelnetInterface.getSynsetObj("bn:00015427n");
        ArrayList<String> a = new ArrayList<>();
        a.add("bn:00015427n");
        a.add("bn:00015427n");
        System.out.println(BabelnetInterface.getDomains(a));

        BabelSynset synset = BabelnetInterface.getSynsetObj("WIKI:EN:Casey_Neistat");
        System.out.println(synset);

        System.out.println(BabelnetInterface.getCategories(synset));

    }
}
