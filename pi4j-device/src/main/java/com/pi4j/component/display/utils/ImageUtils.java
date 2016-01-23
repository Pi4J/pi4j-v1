package com.pi4j.component.display.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  PCD8544DisplayComponent.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2016 Pi4J
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 * 
 * @author SrMouraSilva
 */

/**
 * Utils methods to viabilize Java Graphics abstraction
 */
public class ImageUtils {

	/**
	 * Convert a Image in a Color[][]
	 */
    public static Color[][] getPixelsOf(Image image) {
        BufferedImage bufferedImage = convertToBufferedImage(image);

        final boolean hasAlphaChannel = bufferedImage.getAlphaRaster() != null;

        if (hasAlphaChannel)
            return getPixelsARGB(bufferedImage);
        else if (bufferedImage.getType() == BufferedImage.TYPE_3BYTE_BGR)
            return getPixelsRGB(bufferedImage);
        else
            return getSlowBinaryPixels(bufferedImage);
    }

    private static Color[][] getPixelsARGB(BufferedImage bufferedImage) {
        byte[] pixels = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();

        final int width = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();

        Color[][] result = new Color[height][width];

        final int pixelLength = 4;
        for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {

            int argb = 0;
            argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
            argb += ((int) pixels[pixel + 1] & 0xff); // blue
            argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
            argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red

            result[row][col] = new Color(argb, true);
            col++;

            if (col == width) {
                col = 0;
                row++;
            }
        }
        
        return result;
    }

    private static Color[][] getSlowBinaryPixels(BufferedImage bufferedImage) {
        final int width = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();

        Color[][] result = new Color[height][width];

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                result[y][x] = new Color(bufferedImage.getRGB(x, y));

        return result;
    }

    private static Color[][] getPixelsRGB(BufferedImage bufferedImage) {
        byte[] pixels = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();

        final int width = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();

        Color[][] result = new Color[height][width];

        final int pixelLength = 3;
        for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {

            int blue  = pixels[pixel] & 0xff;
            int green = (pixels[pixel + 1] & 0xff);
            int red   = (pixels[pixel + 2] & 0xff);

            result[row][col] = new Color(red, green, blue);

            col++;
            if (col == width) {
                col = 0;
                row++;
            }
        }
        
        return result;
    }

    private static BufferedImage convertToBufferedImage(Image img) {
        if (img instanceof BufferedImage)
            return (BufferedImage) img;

        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        return bimage;
    }
}
