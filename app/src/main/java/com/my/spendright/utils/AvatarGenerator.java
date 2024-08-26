package com.my.spendright.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class AvatarGenerator {

    // Method to generate avatar Bitmap
    public Bitmap generateAvatar(String username, int imageSize) {
        // Generate random background color
        Random random = new Random();
        int backgroundColor =  Color.parseColor("#F5793B");        // getRandomColor(random);

        // Create bitmap and canvas
        Bitmap bitmap = Bitmap.createBitmap(imageSize, imageSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Draw background color
        canvas.drawColor(backgroundColor);

        // Draw text (first letter of username)
        if (username != null && !username.isEmpty()) {
            String firstLetter = username.substring(0, 2).toUpperCase();

            // Setup paint for drawing text
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(imageSize * 0.5f); // Adjust text size as needed
            paint.setTextAlign(Paint.Align.CENTER);

            // Calculate text position (centered)
            float x = canvas.getWidth() / 2f;
            float y = canvas.getHeight() / 2f - (paint.descent() + paint.ascent()) / 2f;

            // Draw text on canvas
            canvas.drawText(firstLetter, x, y, paint);
        }

        return bitmap;
    }

    // Helper method to generate a random color
    private int getRandomColor(Random random) {
        // Generate random RGB color
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return Color.rgb(r, g, b);
    }
}
