package com.mustafa.foodapp.util;

import java.util.List;

public class StringUtils {
    public static String getStringIngredients(List<String> list) {
        System.out.println("FUCK OFFFFFFFFFFFFFFF");
        final StringBuilder stringBuilder = new StringBuilder();
        int i = 1;
        if (list != null) {
            if (list.size() > 0) {
                for (String ingredient : list) {
                    stringBuilder.append(i + ") ");
                    stringBuilder.append(ingredient);
                    stringBuilder.append("\n");
                    i++;
                }
            }
        }
        return stringBuilder.toString();
    }

}
