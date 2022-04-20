
package com.google.android.ads.nativetemplates;

import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;

public class NativeTemplateStyle {

   private Typeface callToActionTextTypeface;

   private float callToActionTextSize;

  private int callToActionTypefaceColor;

  private ColorDrawable callToActionBackgroundColor;

   private Typeface primaryTextTypeface;

  private float primaryTextSize;

   private int primaryTextTypefaceColor;

  private ColorDrawable primaryTextBackgroundColor;

    private Typeface secondaryTextTypeface;

  private float secondaryTextSize;

  private int secondaryTextTypefaceColor;

   private ColorDrawable secondaryTextBackgroundColor;

   private Typeface tertiaryTextTypeface;

  private float tertiaryTextSize;

  private int tertiaryTextTypefaceColor;

  private ColorDrawable tertiaryTextBackgroundColor;

  private ColorDrawable mainBackgroundColor;

  public Typeface getCallToActionTextTypeface() {
    return callToActionTextTypeface;
  }

  public float getCallToActionTextSize() {
    return callToActionTextSize;
  }

  public int getCallToActionTypefaceColor() {
    return callToActionTypefaceColor;
  }

  public ColorDrawable getCallToActionBackgroundColor() {
    return callToActionBackgroundColor;
  }

  public Typeface getPrimaryTextTypeface() {
    return primaryTextTypeface;
  }

  public float getPrimaryTextSize() {
    return primaryTextSize;
  }

  public int getPrimaryTextTypefaceColor() {
    return primaryTextTypefaceColor;
  }

  public ColorDrawable getPrimaryTextBackgroundColor() {
    return primaryTextBackgroundColor;
  }

  public Typeface getSecondaryTextTypeface() {
    return secondaryTextTypeface;
  }

  public float getSecondaryTextSize() {
    return secondaryTextSize;
  }

  public int getSecondaryTextTypefaceColor() {
    return secondaryTextTypefaceColor;
  }

  public ColorDrawable getSecondaryTextBackgroundColor() {
    return secondaryTextBackgroundColor;
  }

  public Typeface getTertiaryTextTypeface() {
    return tertiaryTextTypeface;
  }

  public float getTertiaryTextSize() {
    return tertiaryTextSize;
  }

  public int getTertiaryTextTypefaceColor() {
    return tertiaryTextTypefaceColor;
  }

  public ColorDrawable getTertiaryTextBackgroundColor() {
    return tertiaryTextBackgroundColor;
  }

  public ColorDrawable getMainBackgroundColor() {
    return mainBackgroundColor;
  }


  public static class Builder {

    private NativeTemplateStyle styles;

    public Builder() {
      this.styles = new NativeTemplateStyle();
    }

    public Builder withCallToActionTextTypeface(Typeface callToActionTextTypeface) {
      this.styles.callToActionTextTypeface = callToActionTextTypeface;
      return this;
    }

    public Builder withCallToActionTextSize(float callToActionTextSize) {
      this.styles.callToActionTextSize = callToActionTextSize;
      return this;
    }

    public Builder withCallToActionTypefaceColor(int callToActionTypefaceColor) {
      this.styles.callToActionTypefaceColor = callToActionTypefaceColor;
      return this;
    }

    public Builder withCallToActionBackgroundColor(ColorDrawable callToActionBackgroundColor) {
      this.styles.callToActionBackgroundColor = callToActionBackgroundColor;
      return this;
    }

    public Builder withPrimaryTextTypeface(Typeface primaryTextTypeface) {
      this.styles.primaryTextTypeface = primaryTextTypeface;
      return this;
    }

    public Builder withPrimaryTextSize(float primaryTextSize) {
      this.styles.primaryTextSize = primaryTextSize;
      return this;
    }

    public Builder withPrimaryTextTypefaceColor(int primaryTextTypefaceColor) {
      this.styles.primaryTextTypefaceColor = primaryTextTypefaceColor;
      return this;
    }

    public Builder withPrimaryTextBackgroundColor(ColorDrawable primaryTextBackgroundColor) {
      this.styles.primaryTextBackgroundColor = primaryTextBackgroundColor;
      return this;
    }

    public Builder withSecondaryTextTypeface(Typeface secondaryTextTypeface) {
      this.styles.secondaryTextTypeface = secondaryTextTypeface;
      return this;
    }

    public Builder withSecondaryTextSize(float secondaryTextSize) {
      this.styles.secondaryTextSize = secondaryTextSize;
      return this;
    }

    public Builder withSecondaryTextTypefaceColor(int secondaryTextTypefaceColor) {
      this.styles.secondaryTextTypefaceColor = secondaryTextTypefaceColor;
      return this;
    }

    public Builder withSecondaryTextBackgroundColor(ColorDrawable secondaryTextBackgroundColor) {
      this.styles.secondaryTextBackgroundColor = secondaryTextBackgroundColor;
      return this;
    }

    public Builder withTertiaryTextTypeface(Typeface tertiaryTextTypeface) {
      this.styles.tertiaryTextTypeface = tertiaryTextTypeface;
      return this;
    }

    public Builder withTertiaryTextSize(float tertiaryTextSize) {
      this.styles.tertiaryTextSize = tertiaryTextSize;
      return this;
    }

    public Builder withTertiaryTextTypefaceColor(int tertiaryTextTypefaceColor) {
      this.styles.tertiaryTextTypefaceColor = tertiaryTextTypefaceColor;
      return this;
    }

    public Builder withTertiaryTextBackgroundColor(ColorDrawable tertiaryTextBackgroundColor) {
      this.styles.tertiaryTextBackgroundColor = tertiaryTextBackgroundColor;
      return this;
    }

    public Builder withMainBackgroundColor(ColorDrawable mainBackgroundColor) {
      this.styles.mainBackgroundColor = mainBackgroundColor;
      return this;
    }

    public NativeTemplateStyle build() {
      return styles;
    }
  }
}
