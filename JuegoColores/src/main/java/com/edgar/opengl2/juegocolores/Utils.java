package com.edgar.opengl2.juegocolores;

/**
 * Created by edgar on 12/26/13.
 */
public class Utils {

    public static float[] randPos(float [] verts){
        float v[] = new float[verts.length];
        float where = (float) Math.random();
        float rdm = (float)Math.random();
        float rdm1, rdm2;
        if(where<0.25f){
            rdm1 = rdm;
            rdm2 = rdm;
        } else if (0.25f<=where && where<0.5f){
            rdm1 = -rdm;
            rdm2 = rdm;
        } else if(0.5f<where && where<0.75f){
            rdm1 = rdm;
            rdm2 = -rdm;
        } else {
            rdm1 = -rdm;
            rdm2 = -rdm;
        }

        for(int i =0; i <(verts.length/3); i++){
            v[(i * 3)+ 0] = verts[(i * 3)+ 0] + rdm1;
            v[(i * 3)+ 1] = verts[(i * 3)+ 1] + rdm2;
            v[(i * 3) + 2] =verts[(i * 3) + 2];
        }
        return v;
    }

    public static float randSize(float size){
        float val = size;
        while(val<0.05 || val>0.3)
            val = (float) Math.random();
        return val;
    }

}
