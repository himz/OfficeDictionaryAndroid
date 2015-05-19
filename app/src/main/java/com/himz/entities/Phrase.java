package com.himz.entities;

public class Phrase {
    int id;
    String objectId;
    String phraseText;
	String meaning;
    String usage;

    int upVotes;
    int downVotes;

    public Phrase(){
        super();
    }

    public Phrase(String objectId,String phraseText, String meaning, String usage){
        this.objectId = objectId;
        this.phraseText = phraseText;
        this.meaning = meaning;
        this.usage = usage;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }


    public int getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }

    public int getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(int downVotes) {
        this.downVotes = downVotes;
    }

    public String getPhraseText() {
        return phraseText;
    }

    public void setPhraseText(String phraseText) {
        this.phraseText = phraseText;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
