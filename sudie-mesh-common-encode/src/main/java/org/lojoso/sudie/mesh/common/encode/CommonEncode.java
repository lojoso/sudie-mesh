package org.lojoso.sudie.mesh.common.encode;

import org.lojoso.sudie.mesh.common.encode.encoder.ConsumerRequestEncoder;

public class CommonEncode {

    public static void main(String[] args) throws NoSuchMethodException {
        System.out.println("");
        new ConsumerRequestEncoder().encode(CommonEncode.class.getMethod("test", String.class,String.class), new Object[]{"123","456"}, 1);
    }


    public void test(String a, String b){
        System.out.printf("%s %s \n", a, b);
    }

}
