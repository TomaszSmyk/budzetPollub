package com.example.homebudgetpollub;

public class ImagesProvider {

    private ImagesProvider() {
    }

    public static int provideItemsWithImages(Data data) {
        switch (data.getItem()) {
            case "Transport":
                return R.drawable.ic_transport;
            case "Food":
                return R.drawable.ic_food;
            case "House":
                return R.drawable.ic_house;
            case "Entertainment":
                return R.drawable.ic_entertainment;
            case "Education":
                return R.drawable.ic_education;
            case "Charity":
                return R.drawable.ic_consultancy;
            case "Apparel":
                return R.drawable.ic_shirt;
            case "Health":
                return R.drawable.ic_health;
            case "Personal":
                return R.drawable.ic_personalcare;
            case "Other":
                return R.drawable.ic_other;
        }
        return R.drawable.ic_other;
    }
}
