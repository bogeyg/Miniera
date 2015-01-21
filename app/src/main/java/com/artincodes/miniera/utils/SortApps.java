package com.artincodes.miniera.utils;

import com.artincodes.miniera.fragments.LauncherFragment;

/**
 * Created by jayadeep on 24/12/14.
 */
public class SortApps {
    public void exchange_sort(LauncherFragment.Pac[] pacs){

        int i, j;
        LauncherFragment.Pac temp;  //be sure that the temp variable is the same "type" as the array
        for ( i = 0; i < pacs.length - 1; i ++ )
        {
            for ( j = i + 1; j < pacs.length; j ++ )
            {
                if( pacs[ i ].label.compareToIgnoreCase(pacs[j].label) > 0 )         //sorting into descending order
                {
                    temp = pacs[ i ];   //swapping
                    pacs[ i ] = pacs[ j ];
                    pacs[ j ] = temp;
                }
            }
        }

    }
}
