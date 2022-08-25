package com.maxlab.storyhub;

public class StoryModel {

    private  String storyId, storyName, storyImage;

    public StoryModel(String storyId, String storyName, String storyImage) {
        this.storyId = storyId;
        this.storyName = storyName;
        this.storyImage = storyImage;
    }

    public StoryModel() {
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getStoryName() {
        return storyName;
    }

    public void setStoryName(String storyName) {
        this.storyName = storyName;
    }

    public String getStoryImage() {
        return storyImage;
    }

    public void setStoryImage(String storyImage) {
        this.storyImage = storyImage;
    }
}
