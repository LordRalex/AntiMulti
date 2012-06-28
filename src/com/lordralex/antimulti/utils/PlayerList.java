package com.lordralex.antimulti.utils;

import java.util.ArrayList;

/**
 * This is a modified ArrayList which specifically is made for AntiMulti. This
 * includes modified methods which help find objects in the list to improve
 * speed in searches and fetching
 *
 * @version 1.0
 * @author Joshua
 * @since 1.2
 * @see ArrayList
 */
public class PlayerList<T> extends ArrayList {

    @Override
    public T get(int index) {
        return (T) super.get(index);
    }

    /**
     * This finds the AMPlayer who's name matches the parameter. The AMPlayer
     * can be passed but only the name will be used to find the correct person
     *
     * @param o The String or AMPlayer which contains the name of the target to
     * get
     * @return The object that is in the list which matches the passed object,
     * or null if none is found
     */
    public T get(Object o) {
        if (o instanceof AMPlayer) {
            String targetName = ((AMPlayer) o).getName();
            for (int i = 0; i < this.size(); i++) {
                Object obj = this.get(i);
                if (obj instanceof AMPlayer) {
                    AMPlayer test = (AMPlayer) obj;
                    if (test.getName().equalsIgnoreCase(targetName)) {
                        return (T) test;
                    }
                }
            }
        } else if (o instanceof String) {
            String targetName = (String) o;
            for (int i = 0; i < this.size(); i++) {
                Object obj = this.get(i);
                if (obj instanceof AMPlayer) {
                    AMPlayer test = (AMPlayer) obj;
                    if (test.getName().equalsIgnoreCase(targetName)) {
                        return (T) test;
                    }
                }
            }
        }
        return null;
    }

    /**
     * This finds the index of a player who matches the parameter. This is used
     * to get the direct index of the object, which is useful to see if the list
     * contains a value and can be used to get the object without performing
     * another call to the list to find it.
     *
     * @param o The String or AMPlayer which contains the name of the target to
     * get
     * @return The index where the player is located in the list, or -1 if none
     * is in the list
     */
    public int find(Object o) {
        if (o instanceof AMPlayer) {
            String targetName = ((AMPlayer) o).getName();
            for (int i = 0; i < this.size(); i++) {
                Object obj = this.get(i);
                if (obj instanceof AMPlayer) {
                    AMPlayer test = (AMPlayer) obj;
                    if (test.getName().equalsIgnoreCase(targetName)) {
                        return i;
                    }
                }
            }
        } else if (o instanceof String) {
            String targetName = (String) o;
            for (int i = 0; i < this.size(); i++) {
                Object obj = this.get(i);
                if (obj instanceof AMPlayer) {
                    AMPlayer test = (AMPlayer) obj;
                    if (test.getName().equalsIgnoreCase(targetName)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * This is a modified version of the {@link ArrayList}. This allows for a
     * less complete AMPlayer or a String to be passed by checking to see if the
     * name matches any in the list, otherwise will check to see if the objects
     * are directly equal.
     *
     * @param o The String or AMPlayer which contains the name of the target to
     * get
     * @return True if the object is in the list, otherwise false
     */
    @Override
    public boolean contains(Object o) {
        if (o instanceof AMPlayer) {
            String targetName = ((AMPlayer) o).getName();
            for (int i = 0; i < this.size(); i++) {
                Object obj = this.get(i);
                if (obj.equals(o)) {
                    return true;
                }
                if (obj instanceof AMPlayer) {
                    AMPlayer test = (AMPlayer) obj;
                    if (test.getName().equalsIgnoreCase(targetName)) {
                        return true;
                    }
                }
            }
        } else if (o instanceof String) {
            String targetName = (String) o;
            for (int i = 0; i < this.size(); i++) {
                Object obj = this.get(i);
                if (obj instanceof AMPlayer) {
                    AMPlayer test = (AMPlayer) obj;
                    if (test.getName().equalsIgnoreCase(targetName)) {
                        return true;
                    }
                }
            }
        }
        return super.contains(o);
    }
}
