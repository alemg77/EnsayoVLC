/*****************************************************************************
 * LibVlcUtil.java
 *****************************************************************************
 * Copyright © 2011-2013 VLC authors and VideoLAN
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston MA 02110-1301, USA.
 *****************************************************************************/

package org.videolan.libvlc.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import android.util.Log;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Locale;

public class VLCUtil {



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static String[] getABIList21() {
        final String[] abis = Build.SUPPORTED_ABIS;
        if (abis == null || abis.length == 0)
            return getABIList();
        return abis;
    }

    @SuppressWarnings("deprecation")
    public static String[] getABIList() {
        final String[] abis = new String[2];
        abis[0] = Build.CPU_ABI;
        abis[1] = Build.CPU_ABI2;
        return abis;
    }




    /** '*' prefix means it's unsupported */
    private final static String[] CPU_archs = {"*Pre-v4", "*v4", "*v4T",
            "v5T", "v5TE", "v5TEJ",
            "v6", "v6KZ", "v6T2", "v6K", "v7",
            "*v6-M", "*v6S-M", "*v7E-M", "*v8"};


    private static final String URI_AUTHORIZED_CHARS = "'()*";

    /**
     * VLC authorize only "-._~" in Mrl format, android Uri authorize "_-!.~'()*".
     * Therefore, decode the characters authorized by Android Uri when creating an Uri from VLC.
     */
    public static Uri UriFromMrl(String mrl) {
        final char array[] = mrl.toCharArray();
        final StringBuilder sb = new StringBuilder(array.length*2);
        for (int i = 0; i < array.length; ++i) {
            final char c = array[i];
            if (c == '%') {
                if (array.length - i >= 3) {
                    try {
                        final int hex = Integer.parseInt(new String(array, i + 1, 2), 16);
                        if (URI_AUTHORIZED_CHARS.indexOf(hex) != -1) {
                            sb.append((char) hex);
                            i += 2;
                            continue;
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
            sb.append(c);
        }

        return Uri.parse(sb.toString());
    }

    public static String encodeVLCUri(@NonNull Uri uri) {
        return encodeVLCString(uri.toString());
    }

    /**
     * VLC only acccepts "-._~" in Mrl format, android Uri accepts "_-!.~'()*".
     * Therefore, encode the characters authorized by Android Uri when creating a mrl from an Uri.
     */
    public static String encodeVLCString(@NonNull String mrl) {
        final char[] array = mrl.toCharArray();
        final StringBuilder sb = new StringBuilder(array.length * 2);

        for (final char c : array) {
            if (URI_AUTHORIZED_CHARS.indexOf(c) != -1)
                sb.append("%").append(Integer.toHexString(c));
            else
                sb.append(c);
        }
        return sb.toString();
    }

}
