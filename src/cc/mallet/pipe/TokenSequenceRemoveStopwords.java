/* Copyright (C) 2002 Univ. of Massachusetts Amherst, Computer Science Dept.
   This file is part of "MALLET" (MAchine Learning for LanguagE Toolkit).
   http://www.cs.umass.edu/~mccallum/mallet
   This software is provided under the terms of the Common Public License,
   version 1.0, as published by http://www.opensource.org.  For further
   information, see the file `LICENSE' included with this distribution. */





package cc.mallet.pipe;


import java.util.HashSet;
import java.util.ArrayList;
import java.io.*;

import cc.mallet.types.FeatureSequenceWithBigrams;
import cc.mallet.types.Instance;
import cc.mallet.types.Token;
import cc.mallet.types.TokenSequence;
/**
 * Remove tokens from the token sequence in the data field whose text is in the stopword list.
   @author Andrew McCallum <a href="mailto:mccallum@cs.umass.edu">mccallum@cs.umass.edu</a>
 */

public class TokenSequenceRemoveStopwords extends Pipe implements Serializable
{
	// xxx Use a gnu.trove collection instead
	HashSet stoplist = newDefaultStopList();
	boolean caseSensitive = true;
	boolean markDeletions = false;

  private HashSet newDefaultStopList ()
  {
    HashSet sl = new HashSet();
    for (int i = 0; i < stopwords.length; i++)
      sl.add (stopwords[i]);
    return sl;
  }


  public TokenSequenceRemoveStopwords (boolean caseSensitive, boolean markDeletions)
	{
		this.caseSensitive = caseSensitive;
		this.markDeletions = markDeletions;
	}

  public TokenSequenceRemoveStopwords (boolean caseSensitive)
	{
		this.caseSensitive = caseSensitive;
	}

	public TokenSequenceRemoveStopwords ()
	{
		this (false);
	}

	public TokenSequenceRemoveStopwords setCaseSensitive (boolean flag)
	{
		this.caseSensitive = flag;
		return this;
	}

	public TokenSequenceRemoveStopwords setMarkDeletions (boolean flag)
	{
		this.markDeletions = flag;
		return this;
	}

  public TokenSequenceRemoveStopwords addStopWords (String[] words)
  {
    for (int i = 0; i < words.length; i++)
      stoplist.add (words[i]);
    return this;
  }


  public TokenSequenceRemoveStopwords removeStopWords (String[] words)
  {
    for (int i = 0; i < words.length; i++)
      stoplist.remove (words[i]);
    return this;
  }

  /** Add whitespace-separated tokens in file "wordlist" to the stoplist. */
  public TokenSequenceRemoveStopwords removeStopWords (File wordlist)
  {
    this.removeStopWords (fileToStringArray(wordlist));
    return this;
  }

  /** Add whitespace-separated tokens in file "wordlist" to the stoplist. */
  public TokenSequenceRemoveStopwords addStopWords (File wordlist)
  {
    if (wordlist != null)
      this.addStopWords (fileToStringArray(wordlist));
    return this;
  }


  private String[] fileToStringArray (File f)
  {
    ArrayList wordarray = new ArrayList();
    try {
      BufferedReader input = new BufferedReader (new FileReader (f));
      String line;
      while (( line = input.readLine()) != null) {
        String[] words = line.split ("\\s+");
        for (int i = 0; i < words.length; i++)
          wordarray.add (words[i]);
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("Trouble reading file "+f);
    }
    return (String[]) wordarray.toArray(new String[]{});
  }

  public Instance pipe (Instance carrier)
	{
		TokenSequence ts = (TokenSequence) carrier.getData();
		// xxx This doesn't seem so efficient.  Perhaps have TokenSequence
		// use a LinkedList, and remove Tokens from it? -?
		// But a LinkedList implementation of TokenSequence would be quite inefficient -AKM
		TokenSequence ret = new TokenSequence ();
		Token prevToken = null;
		for (int i = 0; i < ts.size(); i++) {
			Token t = ts.getToken(i);
			if (! stoplist.contains (caseSensitive ? t.getText() : t.getText().toLowerCase())) {
				// xxx Should we instead make and add a copy of the Token?
				ret.add (t);
				prevToken = t;
			} else if (markDeletions && prevToken != null)
				prevToken.setProperty (FeatureSequenceWithBigrams.deletionMark, t.getText());
		}
		carrier.setData(ret);
		return carrier;
	}

	// Serialization 
	
	private static final long serialVersionUID = 1;
	private static final int CURRENT_SERIAL_VERSION = 2;
	
	private void writeObject (ObjectOutputStream out) throws IOException {
		out.writeInt (CURRENT_SERIAL_VERSION);
		out.writeBoolean(caseSensitive);
		out.writeBoolean(markDeletions);
    out.writeObject(stoplist); // New as of CURRENT_SERIAL_VERSION 2
  }
	
	private void readObject (ObjectInputStream in) throws IOException, ClassNotFoundException {
		int version = in.readInt ();
		caseSensitive = in.readBoolean();
		if (version > 0)
			markDeletions = in.readBoolean();
    if (version > 1)
      stoplist = (HashSet) in.readObject();

  }

	
	static final String[] stopwords =
	{
		"a",
		"able",
		"about",
		"above",
		"according",
		"accordingly",
		"across",
		"actually",
		"after",
		"afterwards",
		"again",
		"against",
		"all",
		"allow",
		"allows",
		"almost",
		"alone",
		"along",
		"already",
		"also",
		"although",
		"always",
		"am",
		"among",
		"amongst",
		"an",
		"and",
		"another",
		"any",
		"anybody",
		"anyhow",
		"anyone",
		"anything",
		"anyway",
		"anyways",
		"anywhere",
		"apart",
		"appear",
		"appreciate",
		"appropriate",
		"are",
		"around",
		"as",
		"aside",
		"ask",
		"asking",
		"associated",
		"at",
		"available",
		"away",
		"awfully",
		"b",
		"be",
		"became",
		"because",
		"become",
		"becomes",
		"becoming",
		"been",
		"before",
		"beforehand",
		"behind",
		"being",
		"believe",
		"below",
		"beside",
		"besides",
		"best",
		"better",
		"between",
		"beyond",
		"both",
		"brief",
		"but",
		"by",
		"c",
		"came",
		"can",
		"cannot",
		"cant",
		"cause",
		"causes",
		"certain",
		"certainly",
		"changes",
		"clearly",
		"co",
		"com",
		"come",
		"comes",
		"concerning",
		"consequently",
		"consider",
		"considering",
		"contain",
		"containing",
		"contains",
		"corresponding",
		"could",
		"course",
		"currently",
		"d",
		"definitely",
		"described",
		"despite",
		"did",
		"different",
		"do",
		"does",
		"doing",
		"done",
		"down",
		"downwards",
		"during",
		"e",
		"each",
		"edu",
		"eg",
		"eight",
		"either",
		"else",
		"elsewhere",
		"enough",
		"entirely",
		"especially",
		"et",
		"etc",
		"even",
		"ever",
		"every",
		"everybody",
		"everyone",
		"everything",
		"everywhere",
		"ex",
		"exactly",
		"example",
		"except",
		"f",
		"far",
		"few",
		"fifth",
		"first",
		"five",
		"followed",
		"following",
		"follows",
		"for",
		"former",
		"formerly",
		"forth",
		"four",
		"from",
		"further",
		"furthermore",
		"g",
		"get",
		"gets",
		"getting",
		"given",
		"gives",
		"go",
		"goes",
		"going",
		"gone",
		"got",
		"gotten",
		"greetings",
		"h",
		"had",
		"happens",
		"hardly",
		"has",
		"have",
		"having",
		"he",
		"hello",
		"help",
		"hence",
		"her",
		"here",
		"hereafter",
		"hereby",
		"herein",
		"hereupon",
		"hers",
		"herself",
		"hi",
		"him",
		"himself",
		"his",
		"hither",
		"hopefully",
		"how",
		"howbeit",
		"however",
		"i",
		"ie",
		"if",
		"ignored",
		"immediate",
		"in",
		"inasmuch",
		"inc",
		"indeed",
		"indicate",
		"indicated",
		"indicates",
		"inner",
		"insofar",
		"instead",
		"into",
		"inward",
		"is",
		"it",
		"its",
		"itself",
		"j",
		"just",
		"k",
		"keep",
		"keeps",
		"kept",
		"know",
		"knows",
		"known",
		"l",
		"last",
		"lately",
		"later",
		"latter",
		"latterly",
		"least",
		"less",
		"lest",
		"let",
		"like",
		"liked",
		"likely",
		"little",
		"look",
		"looking",
		"looks",
		"ltd",
		"m",
		"mainly",
		"many",
		"may",
		"maybe",
		"me",
		"mean",
		"meanwhile",
		"merely",
		"might",
		"more",
		"moreover",
		"most",
		"mostly",
		"much",
		"must",
		"my",
		"myself",
		"n",
		"name",
		"namely",
		"nd",
		"near",
		"nearly",
		"necessary",
		"need",
		"needs",
		"neither",
		"never",
		"nevertheless",
		"new",
		"next",
		"nine",
		"no",
		"nobody",
		"non",
		"none",
		"noone",
		"nor",
		"normally",
		"not",
		"nothing",
		"novel",
		"now",
		"nowhere",
		"o",
		"obviously",
		"of",
		"off",
		"often",
		"oh",
		"ok",
		"okay",
		"old",
		"on",
		"once",
		"one",
		"ones",
		"only",
		"onto",
		"or",
		"other",
		"others",
		"otherwise",
		"ought",
		"our",
		"ours",
		"ourselves",
		"out",
		"outside",
		"over",
		"overall",
		"own",
		"p",
		"particular",
		"particularly",
		"per",
		"perhaps",
		"placed",
		"please",
		"plus",
		"possible",
		"presumably",
		"probably",
		"provides",
		"q",
		"que",
		"quite",
		"qv",
		"r",
		"rather",
		"rd",
		"re",
		"really",
		"reasonably",
		"regarding",
		"regardless",
		"regards",
		"relatively",
		"respectively",
		"right",
		"s",
		"said",
		"same",
		"saw",
		"say",
		"saying",
		"says",
		"second",
		"secondly",
		"see",
		"seeing",
		"seem",
		"seemed",
		"seeming",
		"seems",
		"seen",
		"self",
		"selves",
		"sensible",
		"sent",
		"serious",
		"seriously",
		"seven",
		"several",
		"shall",
		"she",
		"should",
		"since",
		"six",
		"so",
		"some",
		"somebody",
		"somehow",
		"someone",
		"something",
		"sometime",
		"sometimes",
		"somewhat",
		"somewhere",
		"soon",
		"sorry",
		"specified",
		"specify",
		"specifying",
		"still",
		"sub",
		"such",
		"sup",
		"sure",
		"t",
		"take",
		"taken",
		"tell",
		"tends",
		"th",
		"than",
		"thank",
		"thanks",
		"thanx",
		"that",
		"thats",
		"the",
		"their",
		"theirs",
		"them",
		"themselves",
		"then",
		"thence",
		"there",
		"thereafter",
		"thereby",
		"therefore",
		"therein",
		"theres",
		"thereupon",
		"these",
		"they",
		"think",
		"third",
		"this",
		"thorough",
		"thoroughly",
		"those",
		"though",
		"three",
		"through",
		"throughout",
		"thru",
		"thus",
		"to",
		"together",
		"too",
		"took",
		"toward",
		"towards",
		"tried",
		"tries",
		"truly",
		"try",
		"trying",
		"twice",
		"two",
		"u",
		"un",
		"under",
		"unfortunately",
		"unless",
		"unlikely",
		"until",
		"unto",
		"up",
		"upon",
		"us",
		"use",
		"used",
		"useful",
		"uses",
		"using",
		"usually",
		"uucp",
		"v",
		"value",
		"various",
		"very",
		"via",
		"viz",
		"vs",
		"w",
		"want",
		"wants",
		"was",
		"way",
		"we",
		"welcome",
		"well",
		"went",
		"were",
		"what",
		"whatever",
		"when",
		"whence",
		"whenever",
		"where",
		"whereafter",
		"whereas",
		"whereby",
		"wherein",
		"whereupon",
		"wherever",
		"whether",
		"which",
		"while",
		"whither",
		"who",
		"whoever",
		"whole",
		"whom",
		"whose",
		"why",
		"will",
		"willing",
		"wish",
		"with",
		"within",
		"without",
		"wonder",
		"would",
		"would",
		"x",
		"y",
		"yes",
		"yet",
		"you",
		"your",
		"yours",
		"yourself",
		"yourselves",
		"z",
		"zero",
		// stop words for paper abstracts
		//		"abstract",
		//"paper",
		//"presents",
		//"discuss",
		//"discusses",
		//"conclude",
		//"concludes",
		//"based",
		//"approach"
	};


}
