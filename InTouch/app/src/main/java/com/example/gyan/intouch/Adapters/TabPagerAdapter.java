package com.example.gyan.intouch.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.gyan.intouch.fragments.ChatsFrag;
import com.example.gyan.intouch.fragments.FriendsFrag;
import com.example.gyan.intouch.fragments.RequestFrag;

/**
 * Created by Gyan on 9/28/2017.
 */

public class TabPagerAdapter extends FragmentPagerAdapter{


    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new  RequestFrag();
            case 1:
                return new ChatsFrag();
            case 2:
                return new FriendsFrag();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "Requests";
            case 1:
                return "Chats";
            case 2:
                return "Friends";
            default:
                return null;
        }
    }
}
