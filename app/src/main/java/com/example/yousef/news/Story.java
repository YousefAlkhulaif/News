package com.example.yousef.news;

     class Story {
    private String id;
    private String type;
    private String sectionName;
    private String date;
    private String webTitle;
    private String webUrl;
    private String formattedDate;
    private String author;

    Story(String id, String type, String sectionName, String date, String webTitle, String webUrl, String author) {
        this.id = id;
        this.type = type;
        this.sectionName = sectionName;
        this.date = date;
        this.webTitle = webTitle;
        this.webUrl = webUrl;
        formattedDate = format(date);
        this.author = author;
    }

     String getFormattedDate() {
        return formattedDate;
    }

    private String format(String date) {
        String[] dateAndTime = date.split("T");
        String[] dateAndTimeForm = dateAndTime[1].split("Z");
        String dateAndTimeFormatted = dateAndTime[0] += ", ";
        dateAndTimeFormatted += dateAndTimeForm[0];
        return dateAndTimeFormatted;
    }

     String getId() {
        return id;
    }

     String getType() {
        return type;
    }

     String getSectionName() {
        return sectionName;
    }

     String getDate() {
        return date;
    }

     String getWebTitle() {
        return webTitle;
    }

     String getWebUrl() {
        return webUrl;
    }

     String getAuthor() {
        return author;
    }
}
