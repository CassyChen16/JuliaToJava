package com.kalepso.util;

import java.util.Iterator;
import static java.lang.Math.pow;

public class GosperList<Type> implements Iterable<Type> {

    
    private Integer n;
    private Integer k;
    private Integer state1;
    private Integer state2;

    public GosperList(Integer n, Integer k) {
        this.n = n;
        this.k = k;
        this.state1 = (int)Math.pow(2,k)-1;
        this.state2 = (int)Math.pow(2,n)-1;
    }
    
    @Override
    public Iterator<Type> iterator() {
        Iterator<Type> it = new Iterator<Type>() {

            @Override
            public boolean hasNext() {
                return state1<=state2;
            }

            @Override
            public Type next() {
                Integer last = state1;
                int o = state1;
                int u = state1 & -state1;
                int v = u + state1;
                int y = v + ( ((v^state1) /u ) >> 2 );
                state1 = y;
                return (Type)last;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return it;
    }
    
    public static void main(String args[]) {

        //Example:
        //  gosper(3,2) == [3, 5, 6]
        //  gosper(5,3) == [7, 11, 13, 14, 19, 21, 22, 25, 26, 28]
        GosperList<Integer> gosper = new GosperList<Integer>(3,2);

        for(Integer num : gosper) {
            System.out.println(num);
        }
       }
}
