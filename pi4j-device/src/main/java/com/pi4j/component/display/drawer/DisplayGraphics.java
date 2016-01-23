package com.pi4j.component.display.drawer;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

import com.pi4j.component.display.Display;
import com.pi4j.component.display.utils.ImageUtils;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  DisplayGraphics.java  
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
 * A Graphics implementation for any Display type
 */
public class DisplayGraphics extends Graphics2D {
    
    private Display display;

    private BufferedImage bufferedImage;
    private Graphics2D graphics;
    
    public enum ColorType { 
        BINARY(BufferedImage.TYPE_BYTE_BINARY),
        RGB(BufferedImage.TYPE_3BYTE_BGR),
        ARGB(BufferedImage.TYPE_4BYTE_ABGR),
        GRAY(BufferedImage.TYPE_BYTE_GRAY);
        
        private final int type;

        private ColorType(int type) {
            this.type = type;
        }
        
        public int getType() {
            return type;
        }
    }

    public DisplayGraphics(Display display, Color initialColor, ColorType type) {
        this.display = display;

        this.bufferedImage = new BufferedImage(display.getWidth(), display.getHeight(), type.getType());
        this.graphics = initGraphics(bufferedImage.createGraphics(), initialColor);
        
        this.setColor(initialColor);
    }

    private Graphics2D initGraphics(Graphics2D graphics, Color initialColor) {
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        graphics.setBackground(Color.WHITE);
        graphics.fillRect(0, 0, this.display.getWidth(), this.display.getHeight());
        graphics.setColor(initialColor);

        return graphics;
    }

    public Display getDisplay() {
        return display;
    }

    public void clear() {
        this.graphics.clearRect(0, 0, display.getWidth(), display.getHeight());
    }

    @Override
    public void clearRect(int x, int y, int width, int height) {
        this.graphics.clearRect(x, y, width, height);
    }

    @Override
    public void clipRect(int x, int y, int width, int height) {
        this.graphics.clipRect(x, y, width, height);
    }

    @Override
    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
        this.graphics.copyArea(x, y, width, height, dx, dy);
    }

    @Override
    public Graphics create() {
        return this.graphics.create();
    }

    //FIXME - This is a gambiarra (technical deficit). The java API doesn't works in this mode
    @Override
    public void dispose() {
        //this.graphics.dispose();

        this.updateDisplay();
    }

    private void updateDisplay() {
        Color[][] pixels = ImageUtils.getPixelsOf(this.bufferedImage);
        this.drawDisplay(pixels, 0, 0);
        display.redraw();
    }

    private void drawDisplay(Color[][] pixels, int x, int y) {
        int height = pixels.length;
        int width = pixels[0].length;

        for (int yImage = 0; yImage < height; yImage++)
            for (int xImage = 0; xImage < width; xImage++)
                display.setPixel(x+xImage, y+yImage, pixels[yImage][xImage]);
    }

    @Override
    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        this.graphics.drawArc(x, y, width, height, startAngle, arcAngle);
    }

    @Override
    public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
        return graphics.drawImage(img, x, y, observer);
    }

    @Override
    public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
        return this.graphics.drawImage(img, x, y, bgcolor, observer);
    }

    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
        return this.graphics.drawImage(img, x, y, width, height, observer);
    }

    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
        return this.graphics.drawImage(img, x, y, width, height, bgcolor, observer);
    }

    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
        return this.graphics.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
    }

    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
        return this.graphics.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, observer);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        this.graphics.drawLine(x1, y1, x2, y2);
    }

    @Override
    public void drawOval(int x, int y, int width, int height) {
        this.graphics.drawOval(x, y, width, height);
    }

    @Override
    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        this.graphics.drawPolygon(xPoints, yPoints, nPoints);
    }

    @Override
    public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
        this.graphics.drawPolyline(xPoints, yPoints, nPoints);
    }

    @Override
    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        this.graphics.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    @Override
    public void drawString(String str, int x, int y) {
        this.graphics.drawString(str, x, y);
    }

    @Override
    public void drawString(AttributedCharacterIterator iterator, int x, int y) {
        this.graphics.drawString(iterator, x, y);
    }

    @Override
    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        this.graphics.fillArc(x, y, width, height, startAngle, arcAngle);
    }

    @Override
    public void fillOval(int x, int y, int width, int height) {
        this.graphics.fillOval(x, y, width, height);
    }

    @Override
    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        this.graphics.fillPolygon(xPoints, yPoints, nPoints);
    }

    @Override
    public void fillRect(int x, int y, int width, int height) {
        this.graphics.fillRect(x, y, width, height);
    }

    @Override
    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        this.graphics.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    @Override
    public Shape getClip() {
        return this.graphics.getClip();
    }

    @Override
    public Rectangle getClipBounds() {
        return this.graphics.getClipBounds();
    }

    @Override
    public Color getColor() {
        return this.graphics.getColor();
    }

    @Override
    public Font getFont() {
        return this.graphics.getFont();
    }

    @Override
    public FontMetrics getFontMetrics(Font f) {
        return this.graphics.getFontMetrics(f);
    }

    @Override
    public void setClip(Shape clip) {
        this.graphics.setClip(clip);
    }

    @Override
    public void setClip(int x, int y, int width, int height) {
        this.graphics.setClip(x, y, width, height);
    }

    @Override
    public void setColor(Color c) {
        this.graphics.setColor(c);
    }

    @Override
    public void setFont(Font font) {
        this.graphics.setFont(font);
    }

    @Override
    public void setPaintMode() {
        this.graphics.setPaintMode();
    }

    @Override
    public void setXORMode(Color c1) {
        this.graphics.setXORMode(c1);
    }

    @Override
    public void translate(int x, int y) {
        this.graphics.translate(x, y);
    }

    @Override
    public void addRenderingHints(Map<?, ?> hints) {
        this.graphics.addRenderingHints(hints);
    }

    @Override
    public void clip(Shape s) {
        this.graphics.clip(s);
    }

    @Override
    public void draw(Shape s) {
        this.graphics.draw(s);
    }

    @Override
    public void drawGlyphVector(GlyphVector g, float x, float y) {
        this.graphics.drawGlyphVector(g, x, y);
    }

    @Override
    public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
        return this.graphics.drawImage(img, xform, obs);
    }

    @Override
    public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
        this.graphics.drawImage(img, op, x, y);
    }

    @Override
    public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
        this.graphics.drawRenderableImage(img, xform);
    }

    @Override
    public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
        this.graphics.drawRenderedImage(img, xform);        
    }

    @Override
    public void drawString(String str, float x, float y) {
        this.graphics.drawString(str, x, y);
    }

    @Override
    public void drawString(AttributedCharacterIterator iterator, float x, float y) {
        this.graphics.drawString(iterator, x, y);
    }

    @Override
    public void fill(Shape s) {
        this.graphics.fill(s);
    }

    @Override
    public Color getBackground() {
        return this.graphics.getBackground();
    }

    @Override
    public Composite getComposite() {
        return this.graphics.getComposite();
    }

    @Override
    public GraphicsConfiguration getDeviceConfiguration() {
        return this.graphics.getDeviceConfiguration();
    }

    @Override
    public FontRenderContext getFontRenderContext() {
        return this.graphics.getFontRenderContext();
    }

    @Override
    public Paint getPaint() {
        return this.graphics.getPaint();
    }

    @Override
    public Object getRenderingHint(Key hintKey) {
        return this.graphics.getRenderingHint(hintKey);
    }

    @Override
    public RenderingHints getRenderingHints() {
        return this.graphics.getRenderingHints();
    }

    @Override
    public Stroke getStroke() {
        return this.graphics.getStroke();
    }

    @Override
    public AffineTransform getTransform() {
        return this.graphics.getTransform();
    }

    @Override
    public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
        return this.graphics.hit(rect, s, onStroke);
    }

    @Override
    public void rotate(double theta) {
        this.graphics.rotate(theta);
    }

    @Override
    public void rotate(double theta, double x, double y) {
        this.graphics.rotate(theta, x, y);
    }

    @Override
    public void scale(double sx, double sy) {
        this.graphics.scale(sx, sy);
    }

    @Override
    public void setBackground(Color color) {
        this.graphics.setBackground(color);
    }

    @Override
    public void setComposite(Composite comp) {
        this.graphics.setComposite(comp);
    }

    @Override
    public void setPaint(Paint paint) {
        this.graphics.setPaint(paint);
    }

    @Override
    public void setRenderingHint(Key hintKey, Object hintValue) {
        this.graphics.setRenderingHint(hintKey, hintValue);
    }

    @Override
    public void setRenderingHints(Map<?, ?> hints) {
        this.graphics.setRenderingHints(hints);
    }

    @Override
    public void setStroke(Stroke s) {
        this.graphics.setStroke(s);
    }

    @Override
    public void setTransform(AffineTransform Tx) {
        this.graphics.setTransform(Tx);
    }

    @Override
    public void shear(double shx, double shy) {
        this.graphics.shear(shx, shy);
    }

    @Override
    public void transform(AffineTransform Tx) {
        this.graphics.transform(Tx);
    }

    @Override
    public void translate(double tx, double ty) {
        this.graphics.translate(tx, ty);
    }
}
