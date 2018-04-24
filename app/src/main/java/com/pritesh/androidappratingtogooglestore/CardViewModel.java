package com.pritesh.androidappratingtogooglestore;

/**
 * Created by pritesh.patel on 2018-01-04, 10:52 AM.
 * ADESA, Canada
 */

class CardViewModel
{
    String cardName;
    int imageResourceId;
    int isfav;
    int isturned;

    public String getCardName()
    {
        return cardName;
    }

    public void setCardName(String cardName)
    {
        this.cardName = cardName;
    }

    public int getImageResourceId()
    {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId)
    {
        this.imageResourceId = imageResourceId;
    }

    public int getIsfav()
    {
        return isfav;
    }

    public void setIsfav(int isfav)
    {
        this.isfav = isfav;
    }

    public int getIsturned()
    {
        return isturned;
    }

    public void setIsturned(int isturned)
    {
        this.isturned = isturned;
    }
}
