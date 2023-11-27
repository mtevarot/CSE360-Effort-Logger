package application;



public class UserStory {
    private String title;
    private String keywords;
    private String description;

    public UserStory(String title, String keywords, String description) {
        this.title = title;
        this.keywords = keywords;
        this.description = description;
    }

    public String getTitle() { return title; }
    public String getKeywords() { return keywords; }
    public String getDescription() { return description; }
}

